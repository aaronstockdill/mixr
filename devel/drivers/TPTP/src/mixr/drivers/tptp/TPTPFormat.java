/*
 * File name: TPTPFormat.java
 *    Author: Matej Urbas [matej.urbas@gmail.com]
 * 
 *  Copyright © 2013 Matej Urbas
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
package mixr.drivers.tptp;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import mixr.drivers.tptp.Bundle;
import mixr.logic.FormulaFormatDescriptor;
import mixr.logic.TextEncodedFormulaFormat;
import java.io.StringReader;
import org.openide.util.NbBundle;
import tptp_parser.SimpleTptpParserOutput;
import tptp_parser.SimpleTptpParserOutput.TopLevelItem;
import tptp_parser.TptpLexer;
import tptp_parser.TptpParser;

/**
 * Supplies MixR with information about the TPTP format, parses TPTP string
 * formulae and produces an abstract syntax tree, and pretty-prints TPTP
 * abstract syntax trees back into string.
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
@NbBundle.Messages({
    "TPTPFormat_format_pretty_name=TPTP format"
})
public class TPTPFormat extends FormulaFormatDescriptor implements TextEncodedFormulaFormat {

    /**
     * The name of the natural language format.
     */
    private static final String FormatFormatName = "TPTP";

    private TPTPFormat() {
        super(FormatFormatName, Bundle.TPTPFormat_format_pretty_name(), TopLevelItem.class);
    }

    @Override
    @NbBundle.Messages({
        "TPTPFormat_invalid_formula_object=The given formula object is not of the correct type. Expected an object of the TPTP abstract syntax tree type."
    })
    public String encodeAsString(Object formula) throws FormulaEncodingException {
        if (formula instanceof TopLevelItem) {
            TopLevelItem topLevelItem = (TopLevelItem) formula;
            return topLevelItem.toString();
        } else {
            throw new FormulaEncodingException(Bundle.TPTPFormat_invalid_formula_object());
        }
    }

    @Override
    @NbBundle.Messages({
        "TPTPFormat_invalid_formula=The given formula could not be parsed. Please check that the formula is of the correct TPTP format."
    })
    public Object decodeFromString(String encodedFormula) throws FormulaEncodingException {
        TptpParser tptpParser = new tptp_parser.TptpParser(new TptpLexer(new StringReader(encodedFormula)));
        try {
            final SimpleTptpParserOutput outputManager = new SimpleTptpParserOutput();
            final TopLevelItem formula = (SimpleTptpParserOutput.TopLevelItem) tptpParser.topLevelItem(outputManager);
            return formula;
        } catch (RecognitionException | TokenStreamException ex) {
            throw new FormulaEncodingException(Bundle.TPTPFormat_invalid_formula(), ex);
        }
    }

    /**
     * Returns the singleton instance of the natural language format descriptor.
     *
     * @return the singleton instance of the natural language format descriptor.
     */
    public static TPTPFormat getInstance() {
        return SingletonContainer.Instance;
    }

    private static class SingletonContainer {

        private static final TPTPFormat Instance = new TPTPFormat();
    }
}
