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

import diabelli.Diabelli;
import diabelli.logic.TextEncodedFormulaFormat.FormulaEncodingException;
import java.util.Collections;
import java.util.Set;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
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
public class Placeholder {

    //<editor-fold defaultstate="collapsed" desc="Fields">
    private final FormulaRepresentation hostingFormula;
    private final CarrierFormulaFormat hostingFormat;
    private final EmbeddableFormulaFormat embeddedFormat;
    private final FormulaRepresentation embeddedFormula;
    private final Set<FreeVariable> freeVariables;
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
    private Placeholder(FormulaRepresentation hostingFormula, FormulaRepresentation embeddedFormula, Set<FreeVariable> freeVariables) {
        if (embeddedFormula == null) {
            throw new IllegalArgumentException(Bundle.PH_null_formula());
        }
        if (hostingFormula == null) {
            throw new IllegalArgumentException(Bundle.PH_null_hosting_formula());
        }
        if (embeddedFormula.getFormat() instanceof EmbeddableFormulaFormat) {
            this.embeddedFormat = (EmbeddableFormulaFormat) embeddedFormula.getFormat();
        } else {
            throw new IllegalArgumentException(Bundle.PH_invalid_format());
        }
        if (hostingFormula.getFormat() instanceof CarrierFormulaFormat) {
            this.hostingFormat = (CarrierFormulaFormat) hostingFormula.getFormat();
        } else {
            throw new IllegalArgumentException(Bundle.PH_invalid_hosting_format());
        }
        this.embeddedFormula = embeddedFormula;
        this.hostingFormula = hostingFormula;
        this.freeVariables = freeVariables;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Public Properties">
    public EmbeddableFormulaFormat getEmbeddedFormat() {
        return embeddedFormat;
    }

    public CarrierFormulaFormat getHostingFormat() {
        return hostingFormat;
    }

    public FormulaRepresentation asFormula() {
        throw new UnsupportedOperationException();
    }

    public FormulaRepresentation getEmbeddedFormula() {
        return embeddedFormula;
    }

    public Set<FreeVariable> getFreeVariables() {
        return freeVariables;
    }

    public FormulaRepresentation getHostingFormula() {
        return hostingFormula;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Placeholder Creation">
    /**
     * Creates a new placeholder instance.
     *
     * @param <THost>
     * @param hostingFormula
     * @param payloadFormulaFormat
     * @param payloadFormula
     * @param freeVariables this set is not copied but it will be unmodifiable
     * through the returned placeholder.
     * @return
     * @throws UnknownFormatException
     * @throws diabelli.logic.TextEncodedFormulaFormat.FormulaEncodingException
     */
    @NbBundle.Messages({
        "Placeholder_unknown_format=The formula format '{0}' is not known.",
        "Placeholder_nonembeddable_format=The formula format '{0}' does not support textual encoding of formulae.",
        "Placeholder_formula_invalid=The formula of the payload is not parsable or understood by the '{0}' formula format.",
        "Placeholder_diabelli_not_present=Could not find the Diabelli core component."
    })
    public static Placeholder create(FormulaRepresentation hostingFormula, String payloadFormulaFormat, String payloadFormula, Set<FreeVariable> freeVariables) throws CarrierFormulaFormat.PlaceholderEmbeddingException {
        Diabelli dbli = Lookup.getDefault().lookup(Diabelli.class);
        if (dbli == null) {
            throw new IllegalStateException(Bundle.Placeholder_diabelli_not_present());
        }
        FormulaFormat formulaFormat = dbli.getFormulaFormatManager().getFormulaFormat(payloadFormulaFormat);
        if (formulaFormat == null) {
            throw new CarrierFormulaFormat.PlaceholderEmbeddingException(Bundle.Placeholder_unknown_format(payloadFormulaFormat));
        }
        if (formulaFormat instanceof EmbeddableFormulaFormat) {
            @SuppressWarnings("unchecked")
            EmbeddableFormulaFormat payloadFormat = (EmbeddableFormulaFormat) formulaFormat;
            Object decodedFormula;
            try {
                decodedFormula = payloadFormat.decodeFromString(payloadFormula);
            } catch (FormulaEncodingException ex) {
                throw new CarrierFormulaFormat.PlaceholderEmbeddingException(Bundle.Placeholder_formula_invalid(payloadFormulaFormat), ex);
            }
            FormulaRepresentation fp = new FormulaRepresentation(decodedFormula, payloadFormat, freeVariables);
            return new Placeholder(hostingFormula, fp, Collections.unmodifiableSet(freeVariables));
        } else {
            throw new CarrierFormulaFormat.PlaceholderEmbeddingException(Bundle.Placeholder_nonembeddable_format(payloadFormulaFormat));
        }
    }

    /**
     * Creates a new placeholder instance.
     *
     * @param <THost>
     * @param hostingFormula
     * @param rawPayload
     * @param freeVariables this set is not copied but it will be unmodifiable
     * through the returned placeholder.
     * @return
     * @throws UnknownFormatException
     * @throws diabelli.logic.TextEncodedFormulaFormat.FormulaEncodingException
     */
    @NbBundle.Messages({
        "Placeholder_empty_payload=The payload formula in the placeholder must not be empty.",
        "Placeholder_invalid_payload_format=The payload string is not correctly formatted. The format of payloads should be '<FormatName>: <Formula>'."
    })
    public static Placeholder create(FormulaRepresentation hostingFormula, String rawPayload, Set<FreeVariable> freeVariables) throws CarrierFormulaFormat.PlaceholderEmbeddingException {
        if (rawPayload == null || rawPayload.isEmpty()) {
            throw new CarrierFormulaFormat.PlaceholderEmbeddingException(Bundle.Placeholder_empty_payload());
        }
        String[] components = rawPayload.split(": ?", 2);
        if (components == null || components.length != 2) {
            throw new CarrierFormulaFormat.PlaceholderEmbeddingException(Bundle.Placeholder_invalid_payload_format());
        }
        return create(hostingFormula, components[0], components[1], freeVariables);
    }
    // </editor-fold>
}
