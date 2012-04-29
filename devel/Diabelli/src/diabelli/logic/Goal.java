/*
 * File name: Goal.java
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
import diabelli.components.GoalProvidingReasoner;
import diabelli.logic.FormulaTranslator.TranslationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 * Represents a proof goal (with premises and conclusions) that are being
 * tackled in a {@link GoalProvidingReasoner goal-providing reasoner}. <p>A goal
 * consists of a list of premise formulae and a single conclusion formula. In
 * short, a goal is a Horn clause: <div style="padding-left: 2em;"><pre>(&#x22C0;<sub>[1 &#x2264;
 * <span style="font-style:italic;">i</span> &#x2264; n]</sub>
 * <span style="font-style:italic;">P<sub>i</sub></span>) &#x27F9;
 * <span style="font-style:italic;">C</span></pre></div> </p>
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public class Goal {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private final ArrayList<? extends Formula<?>> premises;
    private final Formula<?> premisesFormula;
    private final Formula<?> conclusion;
    private final Formula<?> goalFormula;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * Initialises the goal with the given premises, conclusion, and a formula
     * that represents the whole goal. <p>Any of the parameters may be {@code null}.</p>
     *
     * @param premises the premises of the goal.
     * @param premisesFormula the premises as a single formula.
     * @param conclusion the conclusion of the goal.
     * @param goalFormula the goal represented with a formula.
     */
    public Goal(
            ArrayList<? extends Formula<?>> premises,
            Formula<?> premisesFormula,
            Formula<?> conclusion,
            Formula<?> goalFormula) {
        this.premises = premises;
        this.conclusion = conclusion == null ? new Formula<>(null, Formula.FormulaRole.Conclusion) : conclusion;
        this.goalFormula = goalFormula == null ? new Formula<>(null, Formula.FormulaRole.Goal) : goalFormula;
        this.premisesFormula = premisesFormula == null ? new Formula<>(null, Formula.FormulaRole.Premise) : premisesFormula;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Public Methods">
    /**
     * Returns the list of premises in this goal. This method will return {@code
     * null} if there are no premises.
     *
     * @return the list of premises in this goal.
     */
    public List<? extends Formula<?>> getPremises() {
        return premises == null || premises.isEmpty() ? null : Collections.unmodifiableList(premises);
    }

    /**
     * Returns the premises represented as a single formula. This function will
     * never return {@code null} even though there are no premises present. It
     * will simply be without a main representation.
     *
     * @return the premises represented as a single formula.
     */
    public Formula<?> getPremisesFormula() {
        return premisesFormula;
    }

    /**
     * Returns the number of premises present in this goal.
     *
     * @return the number of premises present in this goal.
     */
    public int getPremisesCount() {
        return premises == null ? 0 : premises.size();
    }

    /**
     * Returns the premise at the given index.
     *
     * @param index the index of the premise to return.
     * @return the premise at the given index.
     */
    @NbBundle.Messages({
        "G_premise_index_out_of_bounds=Could not fetch the premise at index '{0}'. There are '{1}' premises in this goal."
    })
    public Formula<?> getPremiseAt(int index) {
        int count = getPremisesCount();
        if (index >= count || index < 0) {
            throw new IndexOutOfBoundsException(Bundle.G_premise_index_out_of_bounds(index, count));
        } else {
            return premises.get(index);
        }
    }

    /**
     * Returns the conclusion of this goal. This method will never return {@code
     * null} if this goal has no conclusion.
     *
     * @return the conclusion of this goal.
     */
    public Formula<?> getConclusion() {
        return conclusion;
    }

    /**
     * Returns a formula that represents the whole goal. If the reasoner that
     * owns this goal does not support representation of a whole goal as a
     * formula then this method may return {@code null}.
     *
     * @return a formula that represents the whole goal.
     */
    public Formula<?> asFormula() {
        return goalFormula;
    }

    /**
     * First looks up if there already is a representation of this formula in
     * the given format or if it has already been attempted to convert this
     * formula to the given format. If so, then the existing list of
     * representations are returned (which might be {@code null} or empty).
     *
     * <p>However, if there was no attempt to translate this formula into the
     * given format, then an attempt will be made. If the translation was
     * successful the resulting representation will be returned, otherwise
     * {@code null} is returned.</p>
     *
     * <p>This method is thread-safe.</p>
     *
     * <p>This method is quite expensive if called for the first time,
     * successive calls will be as expensive as calls to {@link Formula#getRepresentations(diabelli.logic.FormulaFormat)}.</p>
     *
     * <p><span style="font-weight:bold">Important</span>: this method tries to
     * translate only the main representation into others. Therefore, if there
     * is no main representation, this method does the same as {@link Formula#getRepresentations(diabelli.logic.FormulaFormat)}.</p>
     *
     * @param <TFrom> the {@link FormulaFormat#getRawFormulaType() type of the raw formula}
     * carried by the main representation of the given formula.
     * @param <TTo> the {@link FormulaFormat#getRawFormulaType() type of the raw formula}
     * carried by the returned representations.
     * @param formula a formula in this goal.
     * @param format the desired format in which to get this formula.
     * @return the translation of the {@link Formula#getMainRepresentation()
     * formula}.
     */
    @NbBundle.Messages({
        "G_toFormat_null=A target format has to be specified.",
        "G_formula_null=A formula for which to fetch representations in the given format must be specified."
    })
    public <TFrom, TTo> ArrayList<? extends FormulaRepresentation<TTo>> fetchRepresentations(Formula<TFrom> formula, FormulaFormat<TTo> format) {
        if (format == null) {
            throw new IllegalArgumentException(Bundle.G_toFormat_null());
        }
        if (formula == null) {
            throw new IllegalArgumentException(Bundle.G_formula_null());
        }
        // If the representations in this format have already been calculated
        // once, return what is already available (it does not matter if no
        // translations are available).
        if (formula.hasAttemptedTranslations(format)) {
            return formula.getRepresentations(format);
        }
        // If there is no main representation, then we will not attempt a
        // translation at all:
        if (formula.getMainRepresentation() == null) {
            return null;
        }
        // Try to translate this formula:
        FormulaRepresentation<TTo> representation = null;
        // There is no representation yet for this format. Try to find one.
        final Set<FormulaTranslator<TFrom, TTo>> formulaTranslatorsFrom = Lookup.getDefault().lookup(Diabelli.class).getFormulaFormatManager().getFormulaTranslators(formula.getMainRepresentation().getFormat(), format);
        if (formulaTranslatorsFrom != null && !formulaTranslatorsFrom.isEmpty()) {
            for (FormulaTranslator<TFrom, TTo> translator : formulaTranslatorsFrom) {
                // Make sure that the translation is valid:
                if (formula.getRole().isTranslationApplicable(translator.getTranslationType())) {
                    try {
                        // We can try and translate it:
                        Formula<TTo> otherRep = translator.translate(this, formula);
                        if (otherRep != null) {
                            // We got a translation, add it to the collection of
                            // all representations of this formula and return it
                            representation = otherRep.getMainRepresentation();
                            break;
                        }
                    } catch (FormulaTranslator.TranslationException ex) {
                        Logger.getLogger(Formula.class.getName()).log(Level.FINEST, String.format("Translation with '%s' failed. Translation error message: %s", translator.getPrettyName(), ex.getMessage()), ex);
                    }
                }
            }
        }
        // Put the found representation into the collection of all representatios.
        // In case the translation didn't succeed, null will indicate that in the
        // future no automatic translation attempts need to be made.
        formula.addRepresentation(format, representation);
        if (representation == null) {
            return null;
        } else {
            ArrayList<FormulaRepresentation<TTo>> rep = new ArrayList<>();
            rep.add(representation);
            return rep;
        }
    }

    /**
     * Tries to add all translation of the given premises to the given format.
     * This method puts the translations into the {@link Goal#getPremisesFormula()
     * premises formula}.
     *
     * @param <TTo> the the type of raw formulae to which to translate the
     * premises.
     * @param premises a subset of premises of this goal.
     * @param toFormat the format to which to translate the subset of premises.
     */
    public <TTo> void addPremisesTranslations(List<? extends Formula<? extends Object>> premises, FormulaFormat<TTo> toFormat) {
        // Are there any premises at all?
        if (premises != null && premises.size() > 0) {
            // TODO: Check that the premises are actually contained in this
            // goal's premises collection

            // Check that all premises have the same format of the main
            // representation:
            @SuppressWarnings("unchecked")
            List<? extends Formula<Object>> premisesO = (List<? extends Formula<Object>>) premises;
            FormulaFormat<Object> fromFormat = getCommonMainFormat(premisesO);
            // If there is a common main format, use it to get all
            // translators:
            Set<FormulaTranslator<Object, TTo>> translators = Lookup.getDefault().lookup(Diabelli.class).getFormulaFormatManager().getFormulaTranslators(fromFormat, toFormat);
            if (translators != null && !translators.isEmpty()) {
                for (FormulaTranslator<Object, TTo> translator : translators) {
                    addPremisesTranslationImpl(premisesO, translator);
                }
            }
        }
    }

    /**
     * Tries to add the translation of the given premises to the given format.
     * This method puts the translation into the {@link Goal#getPremisesFormula()
     * premises formula}.
     *
     * @param <TFrom> the type of raw formulae contained by the main
     * representations of all the premises.
     * @param <TTo> the the type of raw formulae to which to translate the
     * premises.
     * @param premises a subset of premises of this goal.
     * @param translator the translator with which to translate the given
     * premises into the target format.
     */
    @NbBundle.Messages({
        "G_premises_not_with_same_formats=Not all premises have the same format or there are no premises to translate.",
        "G_translator_format_mismatch=The format of the premises does not match the translator's input format."
    })
    public <TFrom, TTo> void addPremisesTranslation(List<? extends Formula<TFrom>> premises, FormulaTranslator<TFrom, TTo> translator) {
        // TODO: Check that the premise is actually contained in this
        // goal's premises collection

        // Check that all premises have the same format of the main representation:
//        @SuppressWarnings("unchecked")
//        List<? extends Formula<TFrom>> premisesO = (List<? extends Formula<TFrom>>) premises;
        FormulaFormat<TFrom> fromFormat = getCommonMainFormat(premises);
        if (fromFormat == null) {
            Logger.getLogger(Goal.class.getName()).log(Level.INFO, Bundle.G_premises_not_with_same_formats());
        } else if (fromFormat != translator.getFromFormat()) {
            Logger.getLogger(Goal.class.getName()).log(Level.INFO, Bundle.G_translator_format_mismatch());
        } else {
            addPremisesTranslationImpl(premises, translator);
        }
    }

    /**
     * Tries to add the translation of the given premises to the given format.
     * This method puts the translation into the {@link Goal#getPremisesFormula()
     * premises formula}.
     *
     * @param <TFrom> the type of raw formulae contained by the main
     * representations of all the premises.
     * @param <TTo> the the type of raw formulae to which to translate the
     * premises.
     * @param premises a subset of premises of this goal.
     * @param translator the translator with which to translate the given
     * premises into the target format.
     */
    private <TFrom, TTo> void addPremisesTranslationImpl(List<? extends Formula<TFrom>> premises, FormulaTranslator<TFrom, TTo> translator) {
        try {
            Formula<TTo> translate = translator.translate(this, premises);
            getPremisesFormula().addRepresentation(translate.getMainRepresentation());
        } catch (TranslationException ex) {
            Logger.getLogger(Formula.class.getName()).log(Level.FINEST, String.format("Translation with '%s' failed. Translation error message: %s", translator.getPrettyName(), ex.getMessage()), ex);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Private Helper Methods">
    /**
     * Returns a non-{@code null} formula format if all the premises have a main
     * representation and all their main representations are of the same format.
     *
     * @param formulae the formulae to check.
     * @return a non-{@code null} formula format if all the premises have a main
     * representation and all their main representations are of the same format.
     */
    private static <T> FormulaFormat<T> getCommonMainFormat(List<? extends Formula<T>> formulae) {
        if (formulae != null && formulae.size() > 0) {
            Formula<T> curFormula = formulae.get(0);
            if (curFormula != null && curFormula.getMainRepresentation() != null) {
                @SuppressWarnings("unchecked")
                FormulaFormat<T> format = curFormula.getMainRepresentation().getFormat();
                for (int i = 1; i < formulae.size(); i++) {
                    curFormula = formulae.get(i);
                    if (curFormula == null || curFormula.getMainRepresentation() == null || curFormula.getMainRepresentation().getFormat() != format) {
                        return null;
                    }
                }
                return format;
            }
        }
        return null;
    }
    // </editor-fold>
}
