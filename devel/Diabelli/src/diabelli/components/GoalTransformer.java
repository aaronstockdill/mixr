/*
 * File name: GoalTransformer.java
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
package diabelli.components;

import diabelli.GoalsManager;
import diabelli.logic.Formula;
import diabelli.logic.FormulaRepresentation;
import diabelli.logic.Goal;
import diabelli.logic.Goals;
import diabelli.logic.InferenceRuleDescriptor;
import diabelli.logic.InferenceStepResult;
import diabelli.logic.InferenceTarget;
import java.util.Collection;
import org.netbeans.api.annotations.common.NonNull;

/**
 * Goal-transforming reasoners have the basic ability to take {@link Goals goals},
 * {@link Goal a particular goal}, {@link Formula a particular formula} within
 * the goal, or even {@link FormulaRepresentation a particular representation of
 * a formula}, optionally let the user interactively apply some inference rules
 * on it, and then commit the changed goals back to the original reasoner
 * through {@link GoalsManager the goals manager}.
 *
 * <p>The changed goal must logically entail the original one. Also, the
 * reasoner must explicitly tell which goals were changed. All the changed goals
 * will be replaced by the new ones.</p>
 *
 * <p>The changed goal may optionally come with a proof trace, which can be
 * reconstructed in the receiving {@link GoalAcceptingReasoner goal-accepting
 * reasoner}.</p>
 *
 * <p>Here is a list of possible scenarios of how the user may apply inference
 * rules in Diabelli:
 *
 * <ol>
 *
 * <li>The user picks a single {@link InferenceTarget inference target} and
 * invokes a reasoner to work with it.</li>
 *
 * <li>The user picks multiple inference targets and invokes a reasoner to work
 * with it. Not all combinations of inference targets are allowed. For example,
 * the user may select only multiple goals or multiple premises and a conclusion
 * from a single goal.</li>
 *
 * </ol>
 *
 * </p>
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public interface GoalTransformer extends Reasoner {

    /**
     * Returns a collection of all inference rules that are applicable on the
     * given target.
     *
     * @param target the target on which we would like to apply the given
     * inference rules.
     *
     * @return a collection of all inference rules that are applicable on the
     * given target.
     */
    Collection<InferenceRuleDescriptor> getApplicableInferenceRules(InferenceTarget target);

    /**
     * Returns the collection of all inference rules provided by this reasoner.
     *
     * @return the collection of all inference rules provided by this reasoner.
     */
    Collection<InferenceRuleDescriptor> getInferenceRules();

    /**
     * Quickly and superficially checks whether this reasoner can work with the
     * given inference target.
     *
     * @param target the goals, formulae, or formulae representations on which
     * we want to use this reasoner.
     * @return {@code true} if this reasoner can work with the given inference
     * target.
     */
    boolean canTransform(InferenceTarget target);

    /**
     * This is the main method that lets the user reason about the target
     * formulae in this reasoner. This method may ask the user to work with this
     * reasoner interactively, or it may apply the transformation entirely
     * automatically. In any case, once the reasoning is done and once the user
     * is happy to commit the {@link InferenceStepResult resulting changes of
     * the goal}, this reasoner must commit the changes through the {@link
     * GoalsManager goals manager}.
     *
     * <p><span style="font-weight:bold">Note</span>: this method returns
     * nothing. It is expected that the reasoner itself decides (and the user
     * too in case of an interactive reasoning mode) when and if the goal
     * changes should be committed back to the original reasoner. This is done
     * through the {@link GoalsManager goals manager}.</p>
     *
     * <p>It may be that the original goals already change once the user is done
     * reasoning with them in this reasoner. Generally, this should not be the
     * case, because the original goals will not change unless the user tells
     * them to. So, unless the user changes the original goals, this procedure
     * should work. Note also, that the original reasoner does not have to be
     * active for the goal transformations to be successfully applied.</p>
     *
     * @param targets the formulae on which to invoke this reasoner.
     * @param inferenceRule the value of inferenceRule
     */
    void applyInferenceRule(@NonNull InferenceTarget targets, @NonNull InferenceRuleDescriptor inferenceRule);

    /**
     * Similar to
     * {@link GoalTransformer#applyInferenceRule(diabelli.logic.InferenceTarget, diabelli.logic.InferenceRuleDescriptor)},
     * however this method will block, apply the inference rule on the given
     * target and return the transformed formulae.
     *
     * <p>This method can be called only with inference rules where
     * {@link InferenceRuleDescriptor#isFullyAutomated()} equals to
     * {@code true}.</p>
     *
     * @param targets the formulae on which to invoke this reasoner.
     * @param inferenceRule the value of inferenceRule
     * @return the transformed formulae.
     */
    InferenceStepResult applyAutomatedInferenceRule(@NonNull InferenceTarget targets, @NonNull InferenceRuleDescriptor inferenceRule);

    /**
     * Returns a pretty, human-readable name for the inference system provided
     * by this goal-transforming reasoner. This will be displayed in Diabelli's
     * user interface (as a sub-menu of the popup that lists all applicable
     * inference rules). For example, Speedith returns <span
     * style="font-style:italic;">Spider diagram rules</span>.
     *
     * @return a pretty, human-readable name for the inference system provided
     * by this goal-transforming reasoner.
     */
    String getInferenceSetName();
}
