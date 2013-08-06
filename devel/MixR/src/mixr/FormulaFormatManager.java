/*
 * File name: FormulaFormatManager.java
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
package mixr;

import mixr.components.MixRDriver;
import mixr.components.FormulaFormatsProvider;
import mixr.logic.FormulaFormat;
import mixr.logic.FormulaRepresentation;
import mixr.logic.FormulaTranslator;
import java.util.Collection;
import java.util.Set;
import org.netbeans.api.annotations.common.NonNull;

/**
 * Provides a central mechanism for registering known {@link
 * FormulaFormat formula formats}. This provides a way for identifying,
 * translating, and understanding of {@link FormulaRepresentation formulae} in
 * different formats. Since MixR's main goal is to connect different
 * reasoners, all of which may understand different representations, this class
 * provides a solution for ease of translation between the reasoners. <p>Formula
 * formats are registered when {@link MixRDriver MixR components} are
 * loaded. The components which want to register new formula formats must
 * implement the {@link FormulaFormatsProvider} interface.</p>
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public interface FormulaFormatManager {

    //<editor-fold defaultstate="collapsed" desc="Formula Formats">
    /**
     * Returns all registered formula formats.
     *
     * <p>This method never returns {@code null}.</p>
     *
     * @return all registered formula formats.
     */
    @NonNull
    Collection<FormulaFormat> getFormulaFormats();

    /**
     * Returns the formula format with the given name.
     *
     * @param formatName the name of the format to look up.
     * @return the formula format with the given name.
     */
    FormulaFormat getFormulaFormat(String formatName);

    /**
     * Returns the number of registered formula formats.
     *
     * @return the number of registered formula formats.
     */
    int getFormulaFormatsCount();
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Translators">
    /**
     * Returns all registered formula translators.
     *
     * @return all registered formula translators.
     */
    @NonNull
    Collection<FormulaTranslator> getFormulaTranslators();

    /**
     * Returns the number of registered formula translators.
     *
     * @return the number of registered formula translators.
     */
    int getFormulaTranslatorsCount();

    /**
     * Returns all registered formula translators that can convert formulae of
     * the given format into any other format.
     * 
     * <p><span style="font-weight:bold">Note</span>: this method will return
     * {@code null} to indicate that there are no desired translators.</p>
     *
     * @param fromFormat the formula format of formulae from which we want to
     * translate.
     * @return all registered formula translators that can convert formulae of
     * the given format into any other format.
     */
    Set<FormulaTranslator> getFormulaTranslatorsFrom(FormulaFormat fromFormat);

    /**
     * Returns the number of registered formula translators that can convert
     * formulae of the given format into any other format.
     *
     * @param fromFormat the formula format of formulae from which we want to
     * translate.
     * @return the number of registered formula translators that can convert
     * formulae of the given format into any other format.
     */
    int getFormulaTranslatorsFromCount(FormulaFormat fromFormat);

    /**
     * Returns all registered formula translators that can convert formulae from
     * any format into a given output format.
     * 
     * <p><span style="font-weight:bold">Note</span>: this method will return
     * {@code null} to indicate that there are no desired translators.</p>
     *
     * @param toFormat the format into which we want to translate formulae of
     * the input format.
     * 
     * @return all registered formula translators that can convert formulae from
     * any format into a given output format.
     */
    Set<FormulaTranslator> getFormulaTranslatorsTo(FormulaFormat toFormat);

    /**
     * Returns the number of registered formula translators that can convert
     * formulae from any format into a given output format.
     *
     * @param toFormat the format into which we want to translate formulae of
     * the input format.
     * 
     * @return the number of registered formula translators that can convert
     * formulae from any format into a given output format.
     */
    int getFormulaTranslatorsToCount(FormulaFormat toFormat);

    /**
     * Returns all registered formula translators that can convert formulae of
     * the given input format into the given output format.
     * 
     * <p><span style="font-weight:bold">Note</span>: this method will return
     * {@code null} to indicate that there are no desired translators.</p>
     *
     * @param fromFormat the formula format of formulae from which we want to
     * translate.
     * 
     * @param toFormat the format into which we want to translate formulae of
     * the input format.
     * 
     * @return all registered formula translators that can convert formulae of
     * the given input format into the given output format.
     */
    Set<FormulaTranslator> getFormulaTranslators(FormulaFormat fromFormat, FormulaFormat toFormat);

    /**
     * Returns the formula translator with the given name.
     *
     * @param formatName the name of the formula translator to look up.
     * @return the formula translator with the given name.
     */
    FormulaTranslator getFormulaTranslator(String formatName);
    //</editor-fold>
}
