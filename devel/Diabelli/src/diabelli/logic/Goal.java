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
import org.netbeans.api.annotations.common.NonNull;
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
    @NbBundle.Messages({
        "G_premises_contains_null=The list of premises contains a null formula."
    })
    @SuppressWarnings("LeakingThisInConstructor")
    public Goal(
            ArrayList<? extends Formula<?>> premises,
            Formula<?> premisesFormula,
            Formula<?> conclusion,
            Formula<?> goalFormula) {
        this.premises = premises;
        this.conclusion = conclusion == null ? new Formula<>(null, Formula.FormulaRole.Conclusion) : conclusion;
        this.goalFormula = goalFormula == null ? new Formula<>(null, Formula.FormulaRole.Goal) : goalFormula;
        this.premisesFormula = premisesFormula == null ? new Formula<>(null, Formula.FormulaRole.Premise) : premisesFormula;
        
        // Set self as the hosting goal for all the above formulae:
        if (premises != null && !premises.isEmpty()) {
            for (Formula<?> formula : premises) {
                if (formula == null) {
                    throw new IllegalArgumentException(Bundle.G_premises_contains_null());
                }
                formula.setHostingGoal(this);
            }
        }
        this.conclusion.setHostingGoal(this);
        this.goalFormula.setHostingGoal(this);
        this.premisesFormula.setHostingGoal(this);
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
     * formula then this formula may have no representation.
     * 
     * <p>This method will never return {@code null}.</p>
     *
     * @return a formula that represents the whole goal.
     */
    @NonNull
    public Formula<?> asFormula() {
        return goalFormula;
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
        @SuppressWarnings("unchecked")
        List<? extends Formula<Object>> premisesO = (List<? extends Formula<Object>>) premises;
        // Check that the premises are actually contained in this goal's
        // premises collection
        if (hostingGoalOf(premisesO) == this) {
            // Check that all premises have the same format of the main representation:
            FormulaFormat<Object> fromFormat = getCommonMainFormat(premisesO);
            // If there is a common main format, use it to get all
            // translators:
            Set<FormulaTranslator<Object, TTo>> translators = Lookup.getDefault().lookup(Diabelli.class).getFormulaFormatManager().getFormulaTranslators(fromFormat, toFormat);
            if (translators != null && !translators.isEmpty()) {
                for (FormulaTranslator<Object, TTo> translator : translators) {
                    addPremisesTranslationImpl(premisesO, translator);
                }
            }
        } else {
            Logger.getLogger(Goal.class.getName()).log(Level.INFO, Bundle.G_translator_format_mismatch());
        }
    }

    /**
     * Tries to add the translation of the given premises to the given format.
     * This method puts the translation into the {@link Goal#getPremisesFormula()
     * premises formula}.
     *
     * <p><span style="font-weight:bold">Important</span>: this method will not
     * add any representations if the given premises are not contained in this
     * goal, if their main representations are not of the same format, or if the
     * translator cannot translate from the format of the premises to the given
     * format.</p>
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
        "G_translator_format_mismatch=The format of the premises does not match the translator's input format.",
        "G_foreign_premises=The premises are not hosted by this goal."
    })
    public <TFrom, TTo> void addPremisesTranslation(List<? extends Formula<TFrom>> premises, FormulaTranslator<TFrom, TTo> translator) {
        // Check that the premises are actually contained in this goal's
        // premises collection
        if (hostingGoalOf(premises) == this) {
            // Check that all premises have the same format of the main representation:
            FormulaFormat<TFrom> fromFormat = getCommonMainFormat(premises);
            if (fromFormat == null) {
                Logger.getLogger(Goal.class.getName()).log(Level.INFO, Bundle.G_premises_not_with_same_formats());
            } else if (fromFormat != translator.getFromFormat()) {
                Logger.getLogger(Goal.class.getName()).log(Level.INFO, Bundle.G_translator_format_mismatch());
            } else {
                addPremisesTranslationImpl(premises, translator);
            }
        } else {
            Logger.getLogger(Goal.class.getName()).log(Level.INFO, Bundle.G_translator_format_mismatch());
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
            FormulaRepresentation<TTo> translate = translator.translate(premises);
            getPremisesFormula().addRepresentation(translate);
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

    /**
     * Returns the common hosting goal of the given premises.
     *
     * <p>{@code null} is returned if the premises don't share the same goal or
     * if {@code null} is actually the value of all of their {@link Formula#getHostingGoal() hosting goals}.</p>
     *
     * @param <T>
     * @param premises
     * @return
     */
    private static <T> Goal hostingGoalOf(List<? extends Formula<T>> premises) {
        if (premises != null && premises.size() > 0) {
            Goal curGoal = premises.get(0).getHostingGoal();
            for (int i = 1; i < premises.size(); i++) {
                Formula<T> formula = premises.get(i);
                if (formula.getHostingGoal() != curGoal) {
                    return null;
                }
            }
            return curGoal;
        }
        return null;
    }
    // </editor-fold>
}
