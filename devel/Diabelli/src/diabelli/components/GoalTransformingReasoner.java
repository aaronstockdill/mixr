/*
 * File name: GoalTransformingReasoner.java
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

import diabelli.logic.GoalTransformation;
import diabelli.logic.InferenceTarget;
import diabelli.GoalsManager;
import diabelli.logic.Formula;
import diabelli.logic.FormulaFormat;
import diabelli.logic.FormulaRepresentation;
import diabelli.logic.Goal;
import java.util.Set;

/**
 * Goal-transforming reasoners have the basic ability to take {@link Goals goals},
 * {@link Goal a particular goal}, {@link Formula a particular formula} within
 * the goal, or even {@link FormulaRepresentation a particular representation of
 * a formula}, optionally let the user interactively apply some inference rules
 * on it, and then commit the changed goals back to the original reasoner
 * through {@link GoalsManager the goals manager}.
 * 
 * <p>The changed goal must logically entail the original one. Also, the
 * reasoner must explicitly tell which goals were changed. All the changed
 * goals will be replaced by the new ones.</p>
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
 * from a single goal. </li>
 *
 * </ol>
 *
 * </p>
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public interface GoalTransformingReasoner extends Reasoner {

    /**
     * Returns a set of formula formats that this reasoner is capable of working
     * with. If this set contains more than one format, then it is assumed that
     * this reasoner may be capable of applying heterogeneous inference rules
     * (i.e., if the user selects multiple formulae of different but supported
     * formats, then this reasoner will be queried whether it can do something
     * with it.
     *
     * @return
     */
    Set<? extends FormulaFormat<?>> getTransformableFormats();

    /**
     * Quickly and superficially checks whether this reasoner can work with
     * the given inference targets.
     * @param targets the goals, formulae, or formulae representations on which
     * we want to use this reasoner.
     * @return {@code true} if this reasoner can work with the given inference
     * targets.
     */
    boolean canActOn(InferenceTarget... targets);
    
    /**
     * This is the main method that lets the user reason about the target
     * formulae in this reasoner. This method may ask the user to work with this
     * reasoner interactively, or it may apply the transformation entirely
     * automatically. In any case, once the reasoning is done and once the user
     * is happy to commit the {@link GoalTransformation resulting changes of
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
     */
    void reasonAbout(InferenceTarget... targets);
}