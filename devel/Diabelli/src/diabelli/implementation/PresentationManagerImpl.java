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
package diabelli.implementation;

import diabelli.PresentationManager;
import diabelli.components.DiabelliComponent;
import diabelli.components.FormulaPresenter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Keeps a list of all registered {@link FormulaPresenter formula presenters} in
 * the current Diabelli instance.
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
class PresentationManagerImpl implements ManagerInternals, PresentationManager {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private DiabelliImpl host;
    private final HashSet<FormulaPresenter> presenters = new HashSet<>();
    // </editor-fold>

    @Override
    public void initialise(DiabelliImpl host) {
        this.host = host;
    }

    @Override
    public void onAfterComponentsLoaded() {
        // Set the first goal providing reasoner as the active one:
        for (DiabelliComponent diabelliComponent : host.getRegisteredComponents()) {
            if (diabelliComponent instanceof FormulaPresenter) {
                presenters.add((FormulaPresenter)diabelliComponent);
            }
        }
    }

    @Override
    public Set<FormulaPresenter> getPresenters() {
        return Collections.unmodifiableSet(presenters);
    }

    @Override
    public int getPresentersCount() {
        return presenters.size();
    }
}
