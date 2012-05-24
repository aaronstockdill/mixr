/*
 * File name: FormulaPresenter.java
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
package diabelli.components;

import diabelli.logic.Formula;
import diabelli.logic.FormulaFormat;
import diabelli.logic.FormulaRepresentation;
import diabelli.logic.Goal;
import diabelli.ui.CurrentFormulaTopComponent;
import java.awt.Component;
import javax.swing.JPanel;
import org.netbeans.api.annotations.common.NonNull;

/**
 * Formula presenters take a {@link Formula Diabelli formula} and return a
 * visual component that displays the formula. For example, a formula with a
 * spider-diagrammatic {@link Formula#getRepresentations(diabelli.logic.FormulaFormat) representation}
 * could be visualised with a spider diagram. Diabelli components (like Speedith
 * for Diabelli) provides visual representations through this interface.
 *
 * <p>The user selects the formula to be represented through the user interface.
 * Specifically, {@link CurrentFormulaTopComponent} is responsible for selecting
 * the formula that should be displayed with the help of presenters.</p>
 *
 * @param <T> the format type of formulae that this presenter is capable of
 * visualising.
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public interface FormulaPresenter<T> extends DiabelliComponent {

    /**
     * Returns the format type of formulae this presenter is capable of
     * visualising.
     * 
     * <p><span style="font-weight:bold">Important</span>: this method must
     * not return {@code null}.</p>
     *
     * @return the format type of formulae this presenter is capable of
     * visualising.
     */
    @NonNull
    FormulaFormat<T> getPresentedFormat();

    /**
     * Returns a panel which displays the given goal. The returned panel will be
     * placed in a sub-window in the main GUI of Diabelli.
     *
     * <p>This method may return {@code null} if the visualisation was not
     * possible for expected reasons, if the given formula is {@code null}, or
     * if the given goal does not have a representation in the right format.</p>
     *
     * @param goal the formula to be visualised.
     * @return a panel which displays the given goal.
     * @throws diabelli.components.FormulaPresenter.VisualisationException see {@link VisualisationException}
     * for info on when this exception is thrown.
     */
    Component createVisualiserFor(Goal goal) throws VisualisationException;

    /**
     * Returns a panel which displays the given goal. The returned panel will be
     * placed in a sub-window in the main GUI of Diabelli.
     *
     * <p>This method may return {@code null} if the visualisation was not
     * possible for expected reasons, if the given formula is {@code null}, or
     * if the given formula does not have a representation in the right
     * format.</p>
     *
     * @param formula the formula to be visualised.
     * @return a panel which displays the given goal.
     * @throws diabelli.components.FormulaPresenter.VisualisationException see {@link VisualisationException}
     * for info on when this exception is thrown.
     */
    Component createVisualiserFor(Formula<?> formula) throws VisualisationException;

    /**
     * Returns a panel which displays the given goal. The returned panel will be
     * placed in a sub-window in the main GUI of Diabelli.
     *
     * <p>This method may return {@code null} if the visualisation was not
     * possible for expected reasons, or if the given formula is {@code null}.</p>
     *
     * @param formula the formula to be visualised.
     * @return a panel which displays the given goal.
     * @throws diabelli.components.FormulaPresenter.VisualisationException see {@link VisualisationException}
     * for info on when this exception is thrown.
     */
    Component createVisualiserFor(FormulaRepresentation<?> formula) throws VisualisationException;

    /**
     * This exception is thrown if the visualisation failed unexpectedly. If the
     * failure to visualise is due to the formula being in the right format but
     * not formed exactly right for proper visualisation. If, however, all
     * formulae of the given format should be visualisable, but the given
     * formula somehow isn't, then this exception should be thrown.
     *
     * <p>Here are some fail conditions that merit a visualisation exception:
     *
     * <ul>
     *
     * <li>If the formula is in the right format and all such formulae should be
     * visualisable, but somehow this one isn't. For example, Speedith should
     * never translate formulae into invalid spider diagrams. Invalid spider
     * diagrams cannot be visualised. If the visualiser gets an invalid spider
     * diagram, this indicates that the translation procedure is buggy. Thus, an
     * exception should be thrown.</li>
     *
     * </ul>
     *
     * </p>
     */
    public static class VisualisationException extends Exception {

        private static final long serialVersionUID = 0x7914fce3aa85859eL;

        public VisualisationException() {
        }

        public VisualisationException(Throwable cause) {
            super(cause);
        }

        public VisualisationException(String message, Throwable cause) {
            super(message, cause);
        }

        public VisualisationException(String message) {
            super(message);
        }
    }
}
