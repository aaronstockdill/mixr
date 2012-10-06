/*
 * File name: GoalAcceptingReasoner.java
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

import diabelli.logic.InferenceStepResult;

/**
 * Reasoners of this kind can accept new goals (which are a product of applying
 * inference rules in other Diabelli reasoners). The new goals should logically
 * entail the old goals, which means that if the new goal is proved, so will be
 * the old one.
 *
 * <p>A goal-accepting reasoner provides the following:
 *
 * <ul>
 *
 * <li>its current goals through the
 * {@link GoalProvider goal-providing interface},</li>
 *
 * <li>an interface for accepting new goals, which entail the {@link
 * GoalProvider#getGoals() original ones},</li>
 *
 * <li>an interface that provides an optional instruction to this reasoner to
 * try and reconstruct the transformation that produced the new goal, and</li>
 *
 * <li>an interface that optionally enables passing prover-specific proof traces
 * that come with the new goals (useful for proof reconstruction).</li>
 *
 * </ul>
 *
 * </p>
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public interface GoalAcceptingReasoner extends GoalProvider {

    // TODO: This method obviously needs to accept something else rather
    // than just goals. The method has to be called with an object that contains
    // the reference to the original goals (as a certification that the
    // transformed goals are actually connected to the original goals and that
    // the original goals didn't yet change and so on).
    /**
     * Puts the transformed goals back to this reasoner. The driver
     * may commit the goals to the actually reasoner asynchronously. There
     * should be no assumption on how the state of the reasoner changes when this
     * call finishes.
     * 
     * <p>This method would be typically called by {@link GoalTransformingReasoner#applyInferenceRule(diabelli.logic.InferenceTarget, diabelli.logic.InferenceRuleDescriptor)}.</p>
     *
     * @param step the results of an application of an inference rule.
     *
     * @throws UnsupportedOperationException thrown if the new goals could not be set
     * for any reason.
     */
    void commitTransformedGoals(InferenceStepResult step) throws UnsupportedOperationException;
}
