/*
 * File name: GoalsManagerImpl.java
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

import mixr.GoalsManager;
import mixr.ReasonersManager;
import mixr.components.GoalAcceptingReasoner;
import mixr.components.GoalProvider;
import mixr.logic.Goals;
import mixr.logic.InferenceStepResult;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.NbBundle;

/**
 * The main implementation of the {@link GoalsManager MixR goal manager
 * specification}.
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
class GoalsManagerImpl implements GoalsManager, ManagerInternals {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private Goals currentGoals;
    private GoalsChangedListener goalsChangedListener;
    private MixRImpl mixr;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     *
     * @param reasonersManager this goal manager will listen to this guy for
     * changes to the {@link ReasonersManager#getActiveReasoner() } property.
     */
    @NbBundle.Messages({
        "GM_reasoners_manager_null=A valid reasoners manager must be provided."
    })
    public GoalsManagerImpl() {
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GoalsManager Interface Implementation">
    @Override
    public Goals getCurrentGoals() {
        return currentGoals;
    }

    @Override
    @NbBundle.Messages({
        "GMI_inference_step_empty_or_invalid=Cannot commit a null inference step or one that is without original goals.",
        "GMI_no_goal_accepting_reasoner=Could not determine the goal-accepting reasoner to pass the goals to."
    })
    public void commitTransformedGoals(InferenceStepResult inferenceResult) {
        if (inferenceResult != null && inferenceResult.getOriginalGoals() != null) {
            // Put the result back to the master reasoner:
            GoalProvider masterReasoner = inferenceResult.getOriginalGoals().getOwner();
            if (masterReasoner == null) {
                throw new IllegalArgumentException(Bundle.GMI_no_goal_accepting_reasoner());
            } else if (masterReasoner instanceof GoalAcceptingReasoner) {
                GoalAcceptingReasoner goalAcceptingReasoner = (GoalAcceptingReasoner) masterReasoner;
                goalAcceptingReasoner.commitTransformedGoals(inferenceResult);
            }
        } else {
            throw new IllegalArgumentException(Bundle.GMI_inference_step_empty_or_invalid());
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Changed Event Stuff">
    private void fireCurrentGoalsChangedEvent(Goals oldGoals) {
        pcs.firePropertyChange(CurrentGoalsChangedEvent, oldGoals, currentGoals);
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

    // <editor-fold defaultstate="collapsed" desc="Private Properties">
    private void setCurrentGoals(Goals goals) {
        if (goals != currentGoals) {
            Goals oldGoals = currentGoals;
            currentGoals = goals;
            Logger.getLogger(GoalsManagerImpl.class.getName()).log(Level.INFO, "Current goals have changed.");
            fireCurrentGoalsChangedEvent(oldGoals);
        }
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Goals Change Monitoring Stuff">
    private class ActiveReasonerChangedListener implements PropertyChangeListener {

        public ActiveReasonerChangedListener() {
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            assert (evt.getOldValue() == null || evt.getOldValue() instanceof GoalProvider);
            unregisterGoalsListener((GoalProvider) evt.getOldValue());
            if (mixr.reasonersManager.getActiveReasoner() != null) {
                registerGoalsListener(mixr.reasonersManager.getActiveReasoner());
                setCurrentGoals(mixr.reasonersManager.getActiveReasoner().getGoals());
            } else {
                setCurrentGoals(null);
            }
        }
    }

    private class GoalsChangedListener implements PropertyChangeListener {

        public GoalsChangedListener() {
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            assert (evt.getNewValue() == null || evt.getNewValue() instanceof Goals);
            setCurrentGoals((Goals) evt.getNewValue());
        }
    }

    public void unregisterGoalsListener(GoalProvider reasoner) {
        if (reasoner != null) {
            reasoner.removePropertyChangeListener(goalsChangedListener, GoalProvider.CurrentGoalsChangedEvent);
        }
    }

    public void registerGoalsListener(GoalProvider reasoner) {
        if (reasoner != null) {
            reasoner.addPropertyChangeListener(goalsChangedListener, GoalProvider.CurrentGoalsChangedEvent);
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Package Private Implementation Specifics">
    @Override
    public void initialise(MixRImpl host) {
        if (host == null) {
            throw new IllegalArgumentException(Bundle.Manager_mixr_null());
        }
        mixr = host;
        if (host.reasonersManager == null) {
            throw new IllegalArgumentException(Bundle.GM_reasoners_manager_null());
        }
        mixr.reasonersManager.addPropertyChangeListener(new ActiveReasonerChangedListener(), ReasonersManager.ActiveReasonerChangedEvent);
        goalsChangedListener = new GoalsChangedListener();
    }

    @Override
    public void onAfterComponentsLoaded() {
        // Check whether the currently active reasoner has a goal:
        GoalProvider activeReasoner = mixr.reasonersManager.getActiveReasoner();
        if (activeReasoner != null && activeReasoner.getGoals() != null) {
            setCurrentGoals(activeReasoner.getGoals());
        }
    }
    // </editor-fold>
}
