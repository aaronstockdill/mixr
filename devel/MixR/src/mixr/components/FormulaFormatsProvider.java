/*
 * File name: FormulaFormatsProvider.java
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
package mixr.components;

import mixr.MixR;
import mixr.FormulaFormatManager;
import mixr.logic.FormulaFormat;
import java.util.Collection;

/**
 * MixR components of this type can register known formula formats with the
 * {@link FormulaFormatManager formula format manager} (see {@link MixR#getFormulaFormatManager()
 * }). The registration process is automatic and happens during MixR's
 * loading stage.
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public interface FormulaFormatsProvider extends MixRDriver {

    /**
     * Returns a list of formula formats provided by this MixR component.
     *
     * @return a list of formula formats provided by this MixR component.
     */
    Collection<FormulaFormat> getFormulaFormats();
}
