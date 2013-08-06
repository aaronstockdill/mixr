/*
 * File name: PresentationManagerImpl.java
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
package mixr.implementation;

import mixr.PresentationManager;
import mixr.components.MixRDriver;
import mixr.components.FormulaPresenter;
import mixr.implementation.Bundle;
import mixr.logic.FormulaFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.NbBundle;

/**
 * Keeps a list of all registered {@link FormulaPresenter formula presenters} in
 * the current MixR instance.
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
class PresentationManagerImpl implements ManagerInternals, PresentationManager {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private MixRImpl host;
    private Set<FormulaPresenter> presenters;
    private Map<FormulaFormat, Set<FormulaPresenter>> format2presenters;
    // </editor-fold>

    @Override
    public void initialise(MixRImpl host) {
        this.host = host;
    }

    @NbBundle.Messages({
        "PMI_presenters_format_null=The presenter '{0}' does not advertise which format of formulae it can visualise."
    })
    @Override
    public void onAfterComponentsLoaded() {
        HashSet<FormulaPresenter> ps = new HashSet<>();
        HashMap<FormulaFormat, Set<FormulaPresenter>> f2ps = new HashMap<>();
        for (MixRDriver mixrComponent : host.getRegisteredComponents()) {
            if (mixrComponent instanceof FormulaPresenter) {
                final FormulaPresenter fp = (FormulaPresenter) mixrComponent;
                Set<FormulaFormat> presentedFormats = fp.getPresentedFormats();
                if (presentedFormats == null) {
                    Logger.getLogger(PresentationManagerImpl.class.getName()).log(Level.SEVERE, Bundle.PMI_presenters_format_null(fp.getName()));
                } else {
                    // Here we fill the associative map `FormulaFormat -> Set<FormulaPresenter>`
                    for (FormulaFormat presentedFormat : presentedFormats) {
                        Set<FormulaPresenter> psForFormat = f2ps.get(presentedFormat);
                        if (psForFormat == null) {
                            f2ps.put(presentedFormat, psForFormat = new HashSet<>());
                        }
                        psForFormat.add(fp);
                    }
                    ps.add(fp);
                }
            }
        }
        presenters = Collections.unmodifiableSet(ps);
        for (Entry<FormulaFormat, Set<FormulaPresenter>> entry : f2ps.entrySet()) {
            entry.setValue(Collections.unmodifiableSet(entry.getValue()));
        }
        format2presenters = Collections.unmodifiableMap(f2ps);
    }

    @Override
    public Set<FormulaPresenter> getPresenters() {
        return presenters;
    }

    @Override
    public int getPresentersCount() {
        return presenters.size();
    }

    @Override
    public Set<FormulaPresenter> getPresenters(FormulaFormat format) {
        return format2presenters.get(format);
    }
}
