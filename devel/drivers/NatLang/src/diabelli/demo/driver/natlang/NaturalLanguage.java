/*
 * File name: NaturalLanguage.java
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
package diabelli.demo.driver.natlang;

import diabelli.logic.EmbeddableFormulaFormat;
import diabelli.logic.FormulaFormatDescriptor;
import diabelli.logic.FormulaRepresentation;
import diabelli.logic.TextEncodedFormulaFormat.FormulaEncodingException;
import java.util.Set;
import org.openide.util.NbBundle;

/**
 * The formula format descriptor for the <span
 * style="font-style:italic;">natural language</span> formula format.
 *
 * <p>This is a dummy formula format with some hard-coded examples.</p>
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
@NbBundle.Messages({
    "NatLang_format_pretty_name=Spider diagram"
})
public class NaturalLanguage extends FormulaFormatDescriptor<String> implements EmbeddableFormulaFormat<String> {

    /**
     * The name of the natural language format.
     */
    private static final String FormatFormatName = "NatLang";

    private NaturalLanguage() {
        super(FormatFormatName, Bundle.NatLang_format_pretty_name(), String.class);
    }

    @Override
    @NbBundle.Messages({
        "NatLang_encoding_natlang_only=The given formula cannot be converted to the natural language. It is of an unknown type."
    })
    public String encodeAsString(FormulaRepresentation<String> formula) throws FormulaEncodingException {
        // Check if the given formula is of the "NatLang" format:
        if (formula.getFormat() == getInstance()) {
            return formula.getFormula();
        } else {
            throw new FormulaEncodingException(Bundle.NatLang_encoding_natlang_only());
        }
    }

    @Override
    public FormulaRepresentation<String> decodeFromString(String encodedFormula) throws FormulaEncodingException {
        return new FormulaRepresentation<>(encodedFormula, getInstance());
    }

    @Override
    public Set<String> getFreeVariables(FormulaRepresentation<String> formula) {
        return null;
    }

    /**
     * Returns the singleton instance of the natural language format descriptor.
     *
     * @return the singleton instance of the natural language format descriptor.
     */
    public static NaturalLanguage getInstance() {
        return SingletonContainer.Instance;
    }

    private static class SingletonContainer {

        private static final NaturalLanguage Instance = new NaturalLanguage();
    }
}
