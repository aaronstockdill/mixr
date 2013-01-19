/*
 * File name: FormulaRepresentation.java
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

import mixr.logic.Bundle;
import java.util.Set;
import org.netbeans.api.annotations.common.NonNull;
import org.openide.util.NbBundle;

/**
 * Contains {@link FormulaRepresentation#getFormula() a formula} and {@link 
 * FormulaRepresentation#getFormat() a descriptor} of the format in which the
 * formula is encoded.
 * @param <T> the type of the {@link FormulaRepresentation#getFormula() raw formula}.
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public final class FormulaRepresentation implements Sentence {

    //<editor-fold defaultstate="collapsed" desc="Fields">
    private final Object formula;
    private final FormulaFormat format;
    private Formula parentFormula;
    private final Set<FreeVariable> freeVariables;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Creates a new representation description object for the given formula.
     * This class carries the formula itself together with some meta-information
     * about the format in which the formula is encoded. For example, the
     * formula could be a string in the syntax of a particular theorem prover,
     * also, it could be an abstract syntax tree etc.
     *
     * @param formula the raw formula.
     * @param format the description of the format in which the raw formula is
     * encoded.
     */
    @NbBundle.Messages({
        "FP_formula_null=A valid, non-null formula object must be provided.",
        "FP_format_null=A valid, non-null format description of the formula must be provided."
    })
    public FormulaRepresentation(@NonNull Object formula, @NonNull FormulaFormat format, Set<FreeVariable> freeVariables) {
        if (formula == null) {
            throw new IllegalArgumentException(Bundle.FP_formula_null());
        }
        if (format == null) {
            throw new IllegalArgumentException(Bundle.FP_format_null());
        }
        this.formula = formula;
        this.format = format;
        this.freeVariables = freeVariables;
    }
    public FormulaRepresentation(@NonNull Object formula, @NonNull FormulaFormat format) {
        this(formula, format, null);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Properties">
    /**
     * Returns the actual raw formula, which is encoded in the {@link FormulaFormatDescriptor
     * described format}.
     *
     * @return the actual raw formula.
     */
    @NonNull
    public Object getFormula() {
        return formula;
    }

    /**
     * Returns an object that describes the format in which the {@link FormulaRepresentation#getFormula()
     * formula} is encoded.
     *
     * @return an object that describes the format in which the {@link FormulaRepresentation#getFormula()
     * formula} is encoded.
     */
    @NonNull
    public FormulaFormat getFormat() {
        return format;
    }

    /**
     * 
     * @return The placeholder from which this representation originates.
     * Otherwise it returns {@code null}.
     */
    public Set<FreeVariable> getFreeVariables() {
        return freeVariables;
    }
    
    /**
     * Returns the formula of which this is a representation.
     * 
     * <p><span style="font-weight:bold">Note</span>: this method may return {@code null}
     * if this representation is not associated with any formula.</p>
     * 
     * @return the formula of which this is a representation.
     */
    public Formula getParentFormula() {
        return parentFormula;
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Parent Formula">
    /**
     * This method is called by the parent formula itself once this representation
     * is added to the formula's collection of representations.
     * @param parentFormula the formula of which this is a representation.
     */
    void setParentFormula(Formula parentFormula) {
        this.parentFormula = parentFormula;
    }
    // </editor-fold>
}
