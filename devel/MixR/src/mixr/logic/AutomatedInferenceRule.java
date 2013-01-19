/*
 * File name: AutomatedInferenceRule.java
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

import org.netbeans.api.annotations.common.NonNull;

/**
 * A convenience interface suitable for fully automated inference rules in
 * MixR drivers.
 *
 * <p>An automated inference rule should apply the inference step without user
 * interaction. After the inference step is applied the inference rule should
 * simply return the inference step result, which is then automatically passed
 * to the correct goal-accepting reasoner.</p>
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public interface AutomatedInferenceRule {

    /**
     * Applies this inference rule on the given targets.
     *
     * @param targets the statements on which to apply the inference rule.
     * 
     * @return the result of the inference rule application.
     */
    InferenceStepResult applyAutomatedInferenceRule(@NonNull InferenceTarget targets);
}
