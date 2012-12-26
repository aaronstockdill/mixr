/*
 * File name: VariableReferencingFormulaFormat.java
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

import java.util.Set;

/**
 * Formulae of this format may contain free variables (or variables that are
 * bound outside of the scope of the formula).
 *
 * @param <T> the
 * {@link FormulaFormat#getRawFormulaType() type of the raw formula}.
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public interface VariableReferencingFormulaFormat<T> extends FormulaFormat<T> {

    /**
     * Returns the names of variables that are free in the formula (or that are
     * bound outside of the scope of the formula).
     *
     * <p>This function may return {@code null}, which denotes that there are no
     * externally referenced variables in this formula.</p>
     *
     * @param formula the formula from which we want to extract free variables.
     * @return the names of variables that are free in the formula (or that are
     * bound outside of the scope of the formula). May be {@code null}, which
     * denotes that there are no externally referenced variables in this
     * formula.
     */
    Set<FreeVariable<?>> getFreeVariables(FormulaRepresentation<T> formula);
}
