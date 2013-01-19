/*
 * File name: PremisesFormula.java
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
import mixr.logic.FormulaTranslator.TranslationException;
import java.util.ArrayList;

/**
 * This is a convenience class that encapsulates {@link Goal#getPremises() the separate premises of a goal}
 * into a single formula.
 *
 * <p>When fetching other formats for the premises, this method calls {@link FormulaTranslator#translate(java.util.List)}
 * to translate the premises into the desired format.</p>
 *
 * @param <T> the {@link FormulaFormat#getRawFormulaType() type of the raw formula}
 * of {@link Formula#getMainRepresentation() the main representation} of this
 * formula.
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
class PremisesFormula extends Formula {

    //<editor-fold defaultstate="collapsed" desc="Fields">
    /**
     * The array that carries all the premises handled by this formula.
     */
    private final ArrayList<? extends Formula> originalPremises;
    private final boolean supportsTranslation;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    PremisesFormula(ArrayList<? extends Formula> originalPremises) {
        super(null, FormulaRole.Premise);
        this.originalPremises = originalPremises;
        supportsTranslation = originalPremises != null
                && originalPremises.size() > 0
                && originalPremises.get(0).getMainRepresentation() != null;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Translation">
    @Override
    public boolean hasMainTranslationSource() {
        return supportsTranslation;
    }

    @Override
    public FormulaFormat getMainTranslationSourceFormat() {
        return supportsTranslation ? originalPremises.get(0).getMainRepresentation().getFormat() : null;
    }

    @Override
    public FormulaRepresentation translateWith(FormulaTranslator translator) throws TranslationException {
        if (translator == null) {
            throw new IllegalArgumentException(Bundle.F_null_translator());
        }
        // Make sure that the translation is valid and then translate it:
        if (getRole().isTranslationApplicable(translator.getTranslationType())) {
            // We can try and translate it:
            return translator.translate(originalPremises);
        }
        return null;
    }
    // </editor-fold>
}
