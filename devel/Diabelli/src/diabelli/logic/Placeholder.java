/*
 * File name: Placeholder.java
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

import org.openide.util.NbBundle;

/**
 * Contains a formula of a specific format and tracks the outwardly referenced
 * variables that appear in the formula. {@link Placeholder Placeholders} are
 * used to insert arbitrary different representations into another formula.
 *
 * <p> The language into which we want to insert placeholders must support the
 * insertion of uninterpreted terms of the following form:
 *
 * <div style="padding-left: 2em;">
 * <pre><span style="font-style:italic;">P<sub>uninterpreted</sub></span>("<span
 * style="font-style:italic;">formatName</span>", [<span
 * style="font-style:italic;">listOfVariables</span>], "<span
 * style="font-style:italic;">encodedFormula</span>")</pre> </div>
 *
 * The <span style="font-style:italic;">formatName</span> may be included in the
 * <span style="font-style:italic;">encodedFormula</span>. It is up to the
 * {@link CarrierFormulaFormat carrier language driver} how to encode a
 * placeholder. </p>
 *
 * @param <THost> the {@link FormulaFormat#getRawFormulaType() type of raw formulae}
 * of the host language (this language supports embedding of other formulae
 * through {@link Placeholder placeholders}).
 * @param <TEmbedded>
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public class Placeholder<THost, TEmbedded> {

    //<editor-fold defaultstate="collapsed" desc="Fields">
    private final CarrierFormulaFormat<THost> hostingFormat;
    private final EmbeddableFormulaFormat<TEmbedded> embeddedFormat;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Creates a fully initialised placeholder. It contains enough information
     * to be embedded in the {@link Placeholder#getHostingFormat() hosting language}.
     * @param hostingFormat the format of the hosting sentence.
     * @param embeddedFormat the format of the sentence that is to be embedded.
     */
    @NbBundle.Messages({
        "PH_null_hosting_format=A valid hosting format must be provided.",
        "PH_null_embedded_format=A valid format of the embedded formula must be provided."
    })
    public Placeholder(CarrierFormulaFormat<THost> hostingFormat, EmbeddableFormulaFormat<TEmbedded> embeddedFormat) {
        if (embeddedFormat == null) {
            throw new IllegalArgumentException(Bundle.PH_null_embedded_format());
        }
        if (hostingFormat == null) {
            throw new IllegalArgumentException(Bundle.PH_null_hosting_format());
        }
        this.embeddedFormat = embeddedFormat;
        this.hostingFormat = hostingFormat;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Public Properties">
    public EmbeddableFormulaFormat<TEmbedded> getEmbeddedFormat() {
        return embeddedFormat;
    }
    
    public CarrierFormulaFormat<THost> getHostingFormat() {
        return hostingFormat;
    }
    
    public FormulaRepresentation<THost> asFormula() {
        throw new UnsupportedOperationException();
    }
    
    public FormulaRepresentation<TEmbedded> getEmbeddedFormula() {
        throw new UnsupportedOperationException();
    }
    // </editor-fold>
}
