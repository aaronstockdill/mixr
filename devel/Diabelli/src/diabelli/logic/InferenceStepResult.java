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
package diabelli.logic;

import diabelli.components.GoalAcceptingReasoner;

/**
 * When an inference rule is applied this is the result that gets passed back to
 * the master reasoner.
 *
 * <p>There can be many types of inference step results. For example, a simple
 * transformed goal (or multiple goals) or a special instruction to the master
 * theorem prover. The latter method is prover-specific. One should read the
 * documentation of the prover's
 * {@link GoalAcceptingReasoner#commitTransformedGoals(diabelli.logic.InferenceStepResult) commit method}
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
}
