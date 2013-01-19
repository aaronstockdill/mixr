/*
 * File name: InferenceRuleDescriptor.java
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

import mixr.components.GoalTransformer;

/**
 * A simple object that describes an inference rule. Instances of this class are
 * provided by {@link GoalTransformer goal-transforming
 * reasoners} to identify and describe all its inference rules to the user.
 *
 * <p>Instances of this class are also used to choose the inference rule to be
 * applied in
 * {@link GoalTransformer#applyInferenceRule(mixr.logic.InferenceTarget, mixr.logic.InferenceRuleDescriptor)}.</p>
 *
 * <p>This class is immutable.</p>
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public interface InferenceRuleDescriptor {

    // <editor-fold defaultstate="collapsed" desc="Public Methods">
    /**
     * Returns a human-readable name of this inference rule. This name will be
     * displayed as the text of a popup menu item.
     *
     * @return a human-readable name of this inference rule.
     */
    String getName();

    /**
     * Returns a human-readable description of this inference rule. This
     * description will become the tooltip of a popup menu item.
     *
     * @return a human-readable description of this inference rule.
     */
    String getDescription();

    /**
     * Indicates whether this rule can be used non-interactively, that is,
     * whether this rule can be called by other drivers without the need of
     * user's intervention.
     *
     * @return a value that indicates whether this rule can be used
     * non-interactively, that is, whether this rule can be called by other
     * drivers without the need of user's intervention.
     */
    boolean isFullyAutomated();

    /**
     * Returns a reference to the goal-transforming reasoner that provides this
     * inference rule.
     *
     * @return a reference to the goal-transforming reasoner that provides this
     * inference rule.
     */
    GoalTransformer getOwner();
    // TODO: Add categories at some point in the future...
//    String getCategoryName();
    // </editor-fold>
}
