/*
 * File name: Class.java
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

import mixr.MixR;
import mixr.ReasonersManager;
import mixr.components.MixRDriver;
import mixr.components.GoalProvider;
import mixr.components.GoalTransformer;
import mixr.implementation.Bundle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.NbBundle;

/**
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
@NbBundle.Messages({
    "Manager_mixr_null=A valid MixR framework manager instance must be provided."
})
class ReasonersManagerImpl implements ReasonersManager, ManagerInternals {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private GoalProvider activeReasoner;
    private MixR mixr;
    private Set<GoalTransformer> slaveReasoners;
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructors">
    ReasonersManagerImpl() {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ReasonersManager Interface Implementation">
    @Override
    public GoalProvider getActiveReasoner() {
        return activeReasoner;
    }

    @Override
    public void requestActive(GoalProvider reasoner) {
        // TODO: Some time in the future we might want to check whether the
        // currently active reasoner is busy etc.
        Logger.getLogger(ReasonersManagerImpl.class.getName()).log(Level.INFO, "Reasoner ''{0}'' requested focus.", reasoner.getName());
        setActiveReasoner(reasoner);
    }

    @Override
    public Set<GoalTransformer> getGoalTransformingReasoners() {
        return slaveReasoners;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Private Properties">
    private void setActiveReasoner(GoalProvider reasoner) {
        if (reasoner != activeReasoner) {
            GoalProvider oldReasoner = activeReasoner;
            activeReasoner = reasoner;
            fireActiveReasonerChangedEvent(oldReasoner);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Changed Event Stuff">
    private void fireActiveReasonerChangedEvent(GoalProvider oldReasoner) {
        pcs.firePropertyChange(ActiveReasonerChangedEvent, oldReasoner, activeReasoner);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener, String event) {
        pcs.addPropertyChangeListener(event, listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener, String event) {
        pcs.removePropertyChangeListener(event, listener);
    }
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Implementation Specifics">
    @Override
    public void initialise(MixRImpl host) {
        if (host == null) {
            throw new IllegalArgumentException(Bundle.Manager_mixr_null());
        }
        this.mixr = host;
    }

    @Override
    public void onAfterComponentsLoaded() {
        // Set the first goal providing reasoner as the active one:
        for (MixRDriver mixrComponent : mixr.getRegisteredComponents()) {
            if (mixrComponent instanceof GoalProvider) {
                requestActive((GoalProvider)mixrComponent);
                break;
            }
        }
        
        // Now populate the list of all goal-transforming reasoners:
        HashSet<GoalTransformer> gtrs = new HashSet<>();
        for (MixRDriver mixrComponent : mixr.getRegisteredComponents()) {
            if (mixrComponent instanceof GoalTransformer) {
                GoalTransformer gtr = (GoalTransformer) mixrComponent;
                gtrs.add(gtr);
            }
        }
        slaveReasoners = Collections.unmodifiableSet(gtrs);
    }
    // </editor-fold>
}
