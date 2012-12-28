/*
 * File name: InferenceRule.java
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
package diabelli.logic;

import diabelli.GoalsManager;
import org.netbeans.api.annotations.common.NonNull;

/**
 * A convenience interface suitable for interactive inference rules in Diabelli
 * drivers.
 *
 * <p>An interactive inference rule should open a UI with which the user
 * interacts to perform the inference step. After the inference step is applied
 * the inference rule must pass the inference result back to the driver from
 * which the inference targets originate. The method
 * {@link GoalsManager#commitTransformedGoals(diabelli.logic.InferenceStepResult)}
 * may be used to commit the inference step result.</p>
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public interface InferenceRule {

    /**
     * Applies this inference rule on the given targets.
     *
     * @param targets the statements on which to apply the inference rule.
     */
    void applyInferenceRule(@NonNull InferenceTarget targets);
}
