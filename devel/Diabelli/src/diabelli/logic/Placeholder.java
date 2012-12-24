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

import java.util.Set;
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
 * @param <THost> the
 * {@link FormulaFormat#getRawFormulaType() type of raw formulae} of the host
 * language (this language supports embedding of other formulae through
 * {@link Placeholder placeholders}).
 * @param <TEmbedded>
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public class Placeholder<THost, TEmbedded> {

    //<editor-fold defaultstate="collapsed" desc="Fields">
    private final FormulaRepresentation<THost> hostingFormula;
    private final CarrierFormulaFormat<THost> hostingFormat;
    private final EmbeddableFormulaFormat<TEmbedded> embeddedFormat;
    private final FormulaRepresentation<TEmbedded> embeddedFormula;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Creates a fully initialised placeholder. It contains enough information
     * to be embedded in the
     * {@link Placeholder#getHostingFormat() hosting language}.
     *
     * @param hostingFormat the format of the hosting sentence.
     * @param embeddedFormat the format of the sentence that is to be embedded.
     */
    @NbBundle.Messages({
        "PH_null_hosting_formula=A hosting formula must be provided.",
        "PH_invalid_hosting_format=The hosting formula is not of a carrier format.",
        "PH_invalid_format=The formula is not embeddable.",
        "PH_null_formula=No formula to embed."
    })
    private Placeholder(FormulaRepresentation<THost> hostingFormula, FormulaRepresentation<TEmbedded> embeddedFormula) {
        if (embeddedFormula == null) {
            throw new IllegalArgumentException(Bundle.PH_null_formula());
        }
        if (hostingFormula == null) {
            throw new IllegalArgumentException(Bundle.PH_null_hosting_formula());
        }
        if (embeddedFormula.getFormat() instanceof EmbeddableFormulaFormat) {
            this.embeddedFormat = (EmbeddableFormulaFormat<TEmbedded>) embeddedFormula.getFormat();
        } else {
            throw new IllegalArgumentException(Bundle.PH_invalid_format());
        }
        if (hostingFormula.getFormat() instanceof CarrierFormulaFormat) {
            this.hostingFormat = (CarrierFormulaFormat<THost>) hostingFormula.getFormat();
        } else {
            throw new IllegalArgumentException(Bundle.PH_invalid_hosting_format());
        }
        this.embeddedFormula = embeddedFormula;
        this.hostingFormula = hostingFormula;
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
        return embeddedFormula;
    }

    public FormulaRepresentation<THost> getHostingFormula() {
        return hostingFormula;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Placeholder Creation">
    /**
     *
     * @param rawPayloadString
     * @param freeVariables
     * @return
     */
    public static <THost> Placeholder<THost, ?> create(String payloadFormulaFormat, String payloadFormula, Set<String> freeVariables) {
        // TODO: Extract the format and the formula from the string "<format>:\s?<formula>"
        return null;
    }
    // </editor-fold>
}
