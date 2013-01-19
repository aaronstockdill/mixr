/*
 * File name: InferenceStepResult.java
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

import mixr.components.GoalAcceptingReasoner;
import mixr.components.GoalTransformer;
import java.util.List;

/**
 * When an inference rule is applied this is the result that gets passed back to
 * the master reasoner.
 *
 * <p>There can be many types of inference step results. For example, a simple
 * transformed goal (or multiple goals) or a special instruction to the master
 * theorem prover. The latter method is prover-specific. One should read the
 * documentation of the prover's
 * {@link GoalAcceptingReasoner#commitTransformedGoals(mixr.logic.InferenceStepResult) commit method}
 * in order to find out which inference step results it understands.</p>
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public interface InferenceStepResult {

    /**
     * Returns an object containing a series of steps that were performed to
     * achieve the result of this inference.
     *
     * @return a collection of inference steps that were performed on the goals
     * in order to achieve the end result. <p>May return {@code null} to
     * indicate that there is no proof trace associated with this inference step
     * result.</p>
     */
    ProofTrace getProofTrace();

    /**
     * Returns the number of changed goals (the maximum this number can return
     * is the same as the number of original goals).
     *
     * @return the number of changed goals.
     */
    int getGoalChangesCount();

    /**
     * The goals that were transformed.
     *
     * @return the goals that were transformed.
     */
    Goals getOriginalGoals();

    /**
     * The reasoner that produced the transformed goals.
     *
     * @return the reasoner that produced the transformed goals.
     */
    GoalTransformer getSlaveReasoner();

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
    List<List<Goal>> getTransformedGoals();

    /**
     * Returns the number of elements in
     * {@link GoalTransformationResult#transformedGoals}.
     *
     * @return
     */
    int getTransformedGoalsCount();

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
    List<Goal> getTransformedGoalsFor(int originalGoalIndex);

    /**
     * Indicates whether a change has been made to the
     * {@link GoalTransformationResult#getOriginalGoals() original goal} at the
     * given index.
     *
     * @param goalIndex
     * @return
     */
    boolean isGoalChanged(int goalIndex);
}
