/*
 * File name: OracleProofTrace.java
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

/**
 * If the inference step was performed with an oracle, then
 * {@link OracleProofTrace#getInstance()} may be used to indicate so.
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public class OracleProofTrace implements ProofTrace {

    private OracleProofTrace() {
    }

    /**
     * Returns the singleton instance of the Isabelle string format descriptor.
     *
     * @return the singleton instance of the Isabelle string format descriptor.
     */
    public static OracleProofTrace getInstance() {
        return SingletonContainer.Instance;
    }

    private static class SingletonContainer {

        private static final OracleProofTrace Instance = new OracleProofTrace();
    }
}
