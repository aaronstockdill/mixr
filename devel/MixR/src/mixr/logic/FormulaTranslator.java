/*
 * File name: FormulaTranslator.java
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

import mixr.MixR;
import mixr.components.MixRComponent;
import mixr.logic.Bundle;
import java.util.List;
import org.netbeans.api.annotations.common.NonNull;
import org.openide.util.NbBundle;

/**
 * Formula translators provide translation capabilities to MixR. Available
 * translators may be registered by {@link MixRComponent MixR components}
 * that implement the {@link mixr.components.FormulaTranslationsProvider formula
 * translations provider interface}. {@link MixR#getFormulaFormatManager() The
 * formula format manager} will pick all these components up, fetch their
 * translators, and register them for later automatic use in MixR.
 *
 * <p>MixR will try to automatically translate all goals, premises, and
 * conclusions, that the user deliberately inspects.</p>
 *
 * @param <TFrom> the {@link FormulaFormat#getRawFormulaType() type of the raw formula}
 * which this translator can translate to another type.
 * @param <TTo> the {@link FormulaFormat#getRawFormulaType() type of the raw formula}
 * to which this translator can translate a formula.
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public abstract class FormulaTranslator {

    //<editor-fold defaultstate="collapsed" desc="Fields">
    private final FormulaFormat fromFormat;
    private final FormulaFormat toFormat;
    private final TranslationType type;
    private final String name;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * Initialises this formula format translator with the meta-information
     * about what it does.
     *
     * @param fromFormat the format from which this translator is able to
     * translate formulae.
     * @param toFormat the format into which this translator is able to
     * translate formulae.
     * @param type the type of this translation. A translation may be {@link
     * TranslationType#ToEquivalent equivalent}, {@link
     * TranslationType#ToEntailing entailing}, or {@link
     * TranslationType#ToEntailed entailed}. Which translation may be used when
     * depends on whether the original formula is a premise, a conclusion, or
     * the whole formula.
     * @param name an internal and unique name for this translator.
     */
    @NbBundle.Messages({
        "FT_fromFormat_null=The source format of the translation is not specified.",
        "FT_toFormat_null=The destination format of the translation is not specified.",
        "FT_type_null=The type of the translation is not specified.",
        "FT_name_null=The name of this translator is not specified."
    })
    protected FormulaTranslator(@NonNull FormulaFormat fromFormat, @NonNull FormulaFormat toFormat, @NonNull TranslationType type, @NonNull String name) {
        if (fromFormat == null) {
            throw new IllegalArgumentException(Bundle.FT_fromFormat_null());
        }
        if (toFormat == null) {
            throw new IllegalArgumentException(Bundle.FT_toFormat_null());
        }
        if (type == null) {
            throw new IllegalArgumentException(Bundle.FT_type_null());
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException(Bundle.FT_name_null());
        }
        this.fromFormat = fromFormat;
        this.toFormat = toFormat;
        this.type = type;
        this.name = name;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Methods">
    /**
     * Returns the format from which this translator is able to translate
     * formulae.
     *
     * @return the format from which this translator is able to translate
     * formulae.
     */
    @NonNull
    public FormulaFormat getFromFormat() {
        return fromFormat;
    }

    /**
     * Returns the format into which this translator is able to translate
     * formulae.
     *
     * @return the format into which this translator is able to translate
     * formulae.
     */
    @NonNull
    public FormulaFormat getToFormat() {
        return toFormat;
    }

    /**
     * Returns the type of this translation. A translation may be {@link
     * TranslationType#ToEquivalent equivalent}, {@link
     * TranslationType#ToEntailing entailing}, or {@link
     * TranslationType#ToEntailed entailed}. Which translation may be used when
     * depends on whether the original formula is a premise, a conclusion, or
     * the whole formula.
     *
     * @return the type of this translation.
     */
    @NonNull
    public TranslationType getTranslationType() {
        return type;
    }

    /**
     * Returns the internal and unique name for this translator.
     *
     * @return the internal and unique name for this translator.
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * Returns a human-readable description that may be displayed in the GUI
     * through tool-tips.
     *
     * @return a human-readable description that may be displayed in the GUI
     * through tool-tips.
     */
    @NonNull
    public abstract String getDescription();

    /**
     * Returns a human-readable name that may be displayed in the GUI. Possibly
     * in lists of available translations. This string should be as short as
     * possible.
     *
     * @return a human-readable name that may be displayed in the GUI.
     */
    @NonNull
    public abstract String getPrettyName();

    /**
     * Translates the given formula (in the {@link
     * FormulaTranslator#getFromFormat() source format}) into a new formula in
     * the
     * {@link FormulaTranslator#getToFormat() target format}.
     *
     * <p><span style="font-weight:bold">Important</span>: the returned {@link Formula formula}
     * should be new and the translation should be its {@link Formula#getMainRepresentation() main representation}.</p>
     *
     * @param formula the formula to translate.
     * @return the translated representation of the formula.
     * @throws mixr.logic.FormulaTranslator.TranslationException This
     * exception is thrown whenever the translation didn't succeed for any
     * reason. A detailed explanation might be given for the user.
     */
    public abstract FormulaRepresentation translate(Formula formula) throws TranslationException;

    /**
     * Translates the given premises (in the {@link
     * FormulaTranslator#getFromFormat() source format}) into a new single
     * formula in the {@link FormulaTranslator#getToFormat() target format}.
     *
     * <p><span style="font-weight:bold">Important</span>: the returned {@link Formula formula}
     * should be new and the translation should be its {@link Formula#getMainRepresentation() main representation}.</p>
     *
     * <p><span style="font-weight:bold">Important</span>: this method must
     * check that the given formulae have the {@link Formula#getRole() role} of
     * {@link Formula.FormulaRole#Premise a premise} (use the
     * {@link FormulaTranslator#arePremises(java.util.List)} method for
     * this).</p>
     *
     * @param premises the formulae to translate (this is a subset of premises
     * of the given goal).
     * @return the translated representation of the formula.
     * @throws mixr.logic.FormulaTranslator.TranslationException This
     * exception is thrown whenever the translation didn't succeed for any
     * reason. A detailed explanation might be given for the user.
     */
    public abstract FormulaRepresentation translate(List<? extends Formula> premises) throws TranslationException;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Helper Classes">
    /**
     * The translation type of particular {@link FormulaTranslator formula
     * translators}.
     */
    public static enum TranslationType {

        /**
         * Indicates that the translated formula is semantically equivalent to
         * the original one.
         */
        ToEquivalent,
        /**
         * Indicates that the translated formula is semantically entailed
         * (implied) by the original one.
         */
        ToEntailed,
        /**
         * Indicates that the translated formula is semantically entailing
         * (implies) the original one.
         */
        ToEntailing;
    }

    /**
     * This exception gives a detailed explanation as to why a translation did
     * not succeed. The message may be displayed to the user in the GUI.
     */
    public static class TranslationException extends Exception {

        public TranslationException() {
        }

        public TranslationException(Throwable cause) {
            super(cause);
        }

        public TranslationException(String message, Throwable cause) {
            super(message, cause);
        }

        public TranslationException(String message) {
            super(message);
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Static Helper Methods">
    /**
     * Checks whether the given formulae are all premises.
     *
     * <p>This method returns {@code true} only if the given list is not {@code null}
     * or empty and if all the formulae have the {@link Formula#getRole() role}
     * of {@link Formula.FormulaRole#Premise a premise}. </p>
     *
     * @param formulae the list of formulae to check.
     * @return {@code true} iff the given list contains at least one formula and
     * all of them are premises.
     */
    public static boolean arePremises(List<? extends Formula> formulae) {
        if (formulae == null || formulae.isEmpty()) {
            return false;
        } else {
            for (Formula formula : formulae) {
                if (formula.getRole() != Formula.FormulaRole.Premise) {
                    return false;
                }
            }
            return true;
        }
    }
    // </editor-fold>
}
