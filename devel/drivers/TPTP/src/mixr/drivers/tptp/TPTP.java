/*
 * File name: TPTP.java
 *    Author: Matej Urbas [matej.urbas@gmail.com]
 * 
 *  Copyright © 2013 Matej Urbas
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
package mixr.drivers.tptp;

import mixr.components.MixRComponent;
import mixr.components.FormulaFormatsProvider;
import mixr.components.FormulaPresenter;
import mixr.logic.FormulaFormat;
import mixr.logic.FormulaRepresentation;
import java.awt.Component;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.openide.util.lookup.ServiceProvider;
import tptp_parser.SimpleTptpParserOutput;
import tptp_parser.SimpleTptpParserOutput.TopLevelItem;

/**
 * The main class of the TPTP driver. It integrates the TPTP formula format and
 * provides visualisation of TPTP formulae.
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
@ServiceProvider(service = MixRComponent.class)
public class TPTP implements MixRComponent, FormulaFormatsProvider, FormulaPresenter {

    @Override
    public String getName() {
        return "TPTP";
    }

    @Override
    public Collection<FormulaFormat> getFormulaFormats() {
        return FormulaFormatsContainer.FormulaFormats;
    }

    @Override
    public Set<FormulaFormat> getPresentedFormats() {
        return FormulaFormatsContainer.FormulaFormats;
    }

    @Override
    public Component createVisualiserFor(FormulaRepresentation formula) throws VisualisationException {
        if (formula.getFormula() instanceof SimpleTptpParserOutput.TopLevelItem) {
            TopLevelItem topLevelItem = (TopLevelItem) formula.getFormula();
            JTextArea ta = new JTextArea(topLevelItem.toString());
            ta.setEditable(false);
            JScrollPane sp = new JScrollPane(ta);
            return sp;
        } else {
            return null;
        }
    }

    private static class FormulaFormatsContainer {

        private static final Set<FormulaFormat> FormulaFormats;

        static {
            HashSet<FormulaFormat> tmp = new HashSet<>();
            tmp.add(TPTPFormat.getInstance());
            FormulaFormats = Collections.unmodifiableSet(tmp);
        }
    }
    
}
