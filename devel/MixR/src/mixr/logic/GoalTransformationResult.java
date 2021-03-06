/*
 * File name: GoalTransformationResult.java
 *    Author: Matej Urbas [matej.urbas@gmail.com]
 * 
 *  Copyright © 2012 Matej Urbas
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package mixr.logic;

import java.util.ArrayList;
import mixr.components.GoalTransformer;
import mixr.logic.Bundle;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.netbeans.api.annotations.common.NonNull;
import org.openide.util.NbBundle;
import propity.util.MovableArrayList;

/**
 * This inference step result is produced by
 * {@link GoalTransformer goal-transforming reasoners} by applying an inference
 * rule on the
 * {@link GoalTransformationResult#getOriginalGoals() original goals} and
 * producing
 * {@link GoalTransformationResult#getTransformedGoals() some transformed goals}.
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public class GoalTransformationResult implements InferenceStepResult {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private final GoalTransformer slaveReasoner;
    private final Goals originalGoals;
    /**
     * Semantics of the transformed goals list are as follows:
     *
     * <ul>
     *
     * <li>the size of this list will always be the same as the size of {@link GoalTransformationResult#getOriginalGoals() original
     * goals},</li>
     *
     * <li>if {@link List#get(int) transformedGoals.get(i)} returns {@code null}
     * then this indicates that the original goal at index <span
     * style="font-style:italic;">i</span> has not changed (i.e.:
     * {@link GoalTransformationResult#isGoalChanged(int) isGoalChanged(i)} will
     * return {@code false}),</li>
     *
     * <li>if {@link List#get(int) transformedGoals.get(i)} returns an empty
     * list then this indicates that the original goal at index <span
     * style="font-style:italic;">i</span> has been discharged (i.e.:
     * {@link GoalTransformationResult#isGoalChanged(int) isGoalChanged(i)} will
     * return {@code true}),</li>
     *
     * <li>if {@link List#get(int) transformedGoals.get(i)} returns a list
     * containing only the corresponding original goal (the one at index <span
     * style="font-style:italic;">i</span>), then this corresponding original
     * goal has not been changed (i.e.:
     * {@link GoalTransformationResult#isGoalChanged(int) isGoalChanged(i)} will
     * return {@code false}), and</li>
     *
     * <li>in any other case the goal at index <span
     * style="font-style:italic;">i</span> has changed (i.e.:
     * {@link GoalTransformationResult#isGoalChanged(int) isGoalChanged(i)} will
     * return {@code true})</li>
     *
     * </ul>
     *
     * This is an unmodifiable list of unmodifiable lists.
     */
    private final List<List<Goal>> transformedGoals;
    private AtomicInteger goalChangesCount = new AtomicInteger(-1);
    private ProofTrace proofTrace;
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    public GoalTransformationResult(@NonNull GoalTransformer slaveReasoner, @NonNull Goals originalGoals, MovableArrayList<Goal>... transformedGoals) {
        this(slaveReasoner, originalGoals, transformedGoals, null);
    }

    /**
     * Creates a new goal-transformation result.
     *
     * @param slaveReasoner the reasoner that transformed the goals.
     * @param originalGoals the goals that were targets of the transformation.
     * @param transformedGoals the resulting transformed goals. Must be of the
     * same or smaller size than {@code originalGoals} (any dangling goals will
     * be treated as <span style="font-style:italic;">unchanged</span>). For
     * further information see
     * {@link GoalTransformationResult#getTransformedGoals()}.
     */
    @NbBundle.Messages({
        "GTR_slave_reasoner_null=No slave reasoner specified. Goal transformation result must have an associated slave reasoner.",
        "GTR_original_goals_null=Original goals must not be null.",
        "GTR_transformed_goals_mismatch=The list of transformed goals must have at most as many elements as there are original goals."
    })
    public GoalTransformationResult(@NonNull GoalTransformer slaveReasoner, @NonNull Goals originalGoals, MovableArrayList<Goal>[] transformedGoals, ProofTrace proofTrace) {
        if (slaveReasoner == null) {
            throw new IllegalArgumentException(Bundle.GTR_slave_reasoner_null());
        }
        if (originalGoals == null) {
            throw new IllegalArgumentException(Bundle.GTR_original_goals_null());
        }
        if (transformedGoals != null && transformedGoals.length > originalGoals.size()) {
            throw new IllegalArgumentException(Bundle.GTR_transformed_goals_mismatch());
        }
        this.slaveReasoner = slaveReasoner;
        this.originalGoals = originalGoals;

        // If there are any transformed goals, we have to encapsulate them:
        @SuppressWarnings({"rawtypes", "unchecked"})
        List<Goal>[] tmp = new List[originalGoals.size()];
        if (transformedGoals != null && transformedGoals.length > 0) {
            // Take ownership of the transformed goals:
            for (int i = 0; i < transformedGoals.length; i++) {
                MovableArrayList<Goal> curTransformedGoals = transformedGoals[i];
                if (transformedGoals != null) {
                    tmp[i] = new MovableArrayList<>(curTransformedGoals, true).getReadOnlyStore();
                }
            }
        }
        this.transformedGoals = Collections.unmodifiableList(Arrays.asList(tmp));
        this.proofTrace = proofTrace;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Properties">
    /**
     * The transformed goals ({@link GoalTransformationResult#getOriginalGoals()
     * original goals} transformed by {@link
     * GoalTransformationResult#getSlaveReasoner() the slave reasoner}).
     *
     * <p>Semantics of the transformed goals list are as follows:
     *
     * <ul>
     *
     * <li>the size of this list will always be the same as the size of {@link GoalTransformationResult#getOriginalGoals() original
     * goals},</li>
     *
     * <li>if {@link List#get(int) transformedGoals.get(i)} returns {@code null}
     * then this indicates that the original goal at index <span
     * style="font-style:italic;">i</span> has not changed (i.e.:
     * {@link GoalTransformationResult#isGoalChanged(int) isGoalChanged(i)} will
     * return {@code false}),</li>
     *
     * <li>if {@link List#get(int) transformedGoals.get(i)} returns an empty
     * list then this indicates that the original goal at index <span
     * style="font-style:italic;">i</span> has been discharged (i.e.:
     * {@link GoalTransformationResult#isGoalChanged(int) isGoalChanged(i)} will
     * return {@code true} if the original goal at this index isn't itself
     * {@code null}; in the latter case it will return {@code false}),</li>
     *
     * <li>if {@link List#get(int) transformedGoals.get(i)} returns a list
     * containing only the corresponding original goal (the one at index <span
     * style="font-style:italic;">i</span>), then this corresponding original
     * goal has not been changed (i.e.:
     * {@link GoalTransformationResult#isGoalChanged(int) isGoalChanged(i)} will
     * return {@code false}), and</li>
     *
     * <li>in any other case the goal at index <span
     * style="font-style:italic;">i</span> has changed (i.e.:
     * {@link GoalTransformationResult#isGoalChanged(int) isGoalChanged(i)} will
     * return {@code true})</li>
     *
     * </ul></p>
     *
     * @return new goals (transformed original goals).
     */
    @Override
    public List<List<Goal>> getTransformedGoals() {
        return transformedGoals;
    }

    /**
     * The reasoner that produced the transformed goals.
     *
     * @return the reasoner that produced the transformed goals.
     */
    @Override
    public GoalTransformer getSlaveReasoner() {
        return slaveReasoner;
    }

    /**
     * The goals that were transformed.
     *
     * @return the goals that were transformed.
     */
    @Override
    public Goals getOriginalGoals() {
        return originalGoals;
    }

    /**
     * Indicates whether a change has been made to the
     * {@link GoalTransformationResult#getOriginalGoals() original goal} at the
     * given index.
     *
     * @param goalIndex
     * @return
     */
    @Override
    public boolean isGoalChanged(int goalIndex) {
        List<Goal> transformedGoal = transformedGoals.get(goalIndex);

        // According to the agreement for `transformedGoals`, a null value means
        // that the goal hasn't changed:
        if (transformedGoal == null) {
            return false;
        }

        // If the list is empty though, then this indicates that the goal has
        // been discharged (if it hasn't been discharged already):
        if (transformedGoal.isEmpty()) {
            return getOriginalGoals().get(goalIndex) != null;
        }

        // Is there a single element in the list? If it equals the original goal
        // then this means that the goal hasn't changed:
        if (transformedGoal.size() == 1 && transformedGoal.get(0) == getOriginalGoals().get(goalIndex)) {
            return false;
        }

        // In all other cases the goal has changed
        return true;
    }

    /**
     * Returns a collection of transformed goals that correspond to the original
     * goal at the given index (i.e.:
     * {@code getOriginalGoals().get(originalGoalIndex)}).
     *
     * <p>May return {@code null} in which case the original goal at the given
     * index has been discharged.</p>
     *
     * @param originalGoalIndex the index of the original goal.
     * @return a collection of transformed goals that correspond to the original
     * goal at the given index.
     */
    @Override
    public List<Goal> getTransformedGoalsFor(int originalGoalIndex) {
        if (originalGoalIndex < getTransformedGoalsCount() && originalGoalIndex >= 0) {
            return transformedGoals.get(originalGoalIndex);
        }
        return null;
    }

    /**
     * Returns the number of changed goals (the maximum this number can return
     * is the same as the number of original goals).
     *
     * @return the number of changed goals.
     */
    @Override
    public int getGoalChangesCount() {
        // NOTE: It's okay if this value is recalculated multiple times. It's
        // read-only.
        if (goalChangesCount.get() < 0) {
            int counter = 0;
            // First count all the goals which were transformed:
            for (int i = getOriginalGoals().size() - 1; i >= 0; i--) {
                if (isGoalChanged(i)) {
                    ++counter;
                }
            }
            goalChangesCount.set(counter);

        }
        return goalChangesCount.get();
    }

    /**
     * Returns the number of elements in
     * {@link GoalTransformationResult#transformedGoals}.
     *
     * @return
     */
    @Override
    public int getTransformedGoalsCount() {
        return transformedGoals == null ? 0 : transformedGoals.size();
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="InferenceStepResult Implementation">
    @Override
    public ProofTrace getProofTrace() {
        return proofTrace;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Static Convenience Methods">
    @NbBundle.Messages({
        "GTR_multiple_inferences_per_goal=Could not create the goal transformation result. MixR currently does not support multiple inferences on a single goal."
    })
    public static GoalTransformationResult create(GoalTransformer owner, InferenceTargets targets, InferenceTarget[] transformedTargets, List<Sentence>[] transformedSentences, ProofTrace proofTrace) {
        MovableArrayList<Goal>[] transformedGoals = new MovableArrayList[targets.getGoals().size()];
        for (int i = transformedTargets.length - 1; i >= 0; i--) {
            InferenceTarget inferenceTarget = transformedTargets[i];
            List<Sentence> resultForTarget = transformedSentences[i];

            // There should be just one transformation per goal
            int goalIndex = inferenceTarget.getGoalIndex();
            if (transformedGoals[goalIndex] != null) {
                throw new IllegalArgumentException(Bundle.GTR_multiple_inferences_per_goal());
            }
            Goal originalGoal = targets.getGoals().get(goalIndex);

            // Create the new goals from the transformation results.
            transformedGoals[i] = convertToGoals(resultForTarget, inferenceTarget, originalGoal);
        }
        return new GoalTransformationResult(owner, targets.getGoals(), transformedGoals, proofTrace);
    }

    public static GoalTransformationResult create(GoalTransformer owner, InferenceTargets targets, InferenceTarget[] transformedTargets, List<Sentence>[] transformedSentences) {
        return create(owner, targets, transformedTargets, transformedSentences, OracleProofTrace.getInstance());
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Private Helper Methods">
    private static MovableArrayList<Goal> convertToGoals(List<Sentence> resultsForTarget, InferenceTarget originalSentenceLocation, Goal originalGoal) {
        final MovableArrayList<Goal> goals = new MovableArrayList<>(resultsForTarget.size());
        for (Sentence sentence : resultsForTarget) {
            if (sentence instanceof Goal) {
                goals.add((Goal) sentence);
            } else {
                Formula formula;
                // Extract the formula
                if (sentence instanceof Formula) {
                    formula = (Formula) sentence;
                } else if (sentence instanceof FormulaRepresentation) {
                    FormulaRepresentation formulaRepresentation = (FormulaRepresentation) sentence;
                    if (originalSentenceLocation.isGoal()) {
                        formula = new Formula(formulaRepresentation, Formula.FormulaRole.Goal);
                    } else if (originalSentenceLocation.isPremiseOf(originalGoal)) {
                        formula = new Formula(formulaRepresentation, Formula.FormulaRole.Premise);
                    } else if (originalSentenceLocation.isConclusionOf(originalGoal)) {
                        formula = new Formula(formulaRepresentation, Formula.FormulaRole.Conclusion);
                    } else {
                        throw new AssertionError();
                    }
                } else {
                    throw new AssertionError();
                }
                // Put the formula into the new goal:
                switch (formula.getRole()) {
                    case Conclusion:
                        goals.add(new Goal(getCopiesOfFormulae(originalGoal), originalGoal.getPremisesFormula() == null ? null : originalGoal.getPremisesFormula().newCopy(), formula, null));
                        break;
                    case Premise:
                        ArrayList<Formula> premises = getCopiesOfFormulae(originalGoal);
                        premises.set(originalSentenceLocation.getSubformulaIndex(), formula);
                        goals.add(new Goal(premises, null, originalGoal.getConclusion() == null ? null : originalGoal.getConclusion().newCopy(), null));
                        break;
                    case Goal:
                        goals.add(new Goal(null, null, null, formula));
                        break;
                    default:
                        throw new AssertionError();
                }
            }
        }
        return goals;
    }

    private static ArrayList<Formula> getCopiesOfFormulae(Goal originalGoal) {
        if (originalGoal.getPremises() == null) {
            return null;
        } else {
            ArrayList<Formula> nf = new ArrayList<>(originalGoal.getPremises());
            for (int i = 0; i < nf.size(); i++) {
                Formula formula = nf.get(i);
                nf.set(i, formula.newCopy());
            }
            return nf;
        }
    }
    // </editor-fold>
}
