/*
 * File name: BareGoalProvidingReasoner.java
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
package mixr.components.util;

import mixr.components.GoalProvider;
import mixr.logic.Goals;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.util.RandomAccess;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 * Provides a <span style="font-style:italic;">bare</span> (partial and
 * convenience) implementation of the {@link GoalProvider} interface.
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public abstract class BareGoalProvidingReasoner implements GoalProvider, RandomAccess {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    protected Goals goals;
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Properties">
    @Override
    public Goals getGoals() {
        return goals;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Protected Helper Methods">
    /**
     * Sets the goals and fires the goals changed event if the new goals differ
     * from the current ones.
     *
     * <p>The default implementation calls
     * {@link BareGoalAcceptingReasoner#preCurrentGoalsChanged(mixr.logic.Goals, mixr.logic.Goals)}
     * just before applying the change and then
     * {@link BareGoalAcceptingReasoner#fireCurrentGoalsChangedEvent(mixr.logic.Goals)}
     * if the change actually happens.</p>
     *
     * @param goals the new goals to be set.
     * @throws PropertyVetoException thrown if the new goals could not be set
     * for any reason.
     */
    protected void setGoals(Goals goals) throws PropertyVetoException {
        if (this.goals != goals) {
            preCurrentGoalsChanged(this.goals, goals);
            Goals oldGoals = this.goals;
            this.goals = goals;
            fireCurrentGoalsChangedEvent(oldGoals);
        }
    }

    /**
     * Asks MixR to make this reasoner the active one.
     */
    protected void requestActive() {
        Lookup.getDefault().lookup(mixr.MixR.class).getReasonersManager().requestActive(this);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Changed Event Stuff">
    protected void fireCurrentGoalsChangedEvent(Goals oldGoals) {
        pcs.firePropertyChange(CurrentGoalsChangedEvent, oldGoals, goals);
    }

    /**
     * This method is invoked by the default implementation of
     * {@link BareGoalProvidingReasoner#setGoals(mixr.logic.Goals)} just
     * before it actually changes the goals. Subclasses may override this method
     * to veto the change (by throwing a {@link PropertyVetoException}).
     *
     * @param oldGoals goals before the change.
     * @param newGoals goals after the change.
     * @throws PropertyVetoException thrown if the new goals could not be set
     * for any reason.
     */
    protected void preCurrentGoalsChanged(Goals oldGoals, Goals newGoals) throws PropertyVetoException {
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
    protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    // </editor-fold>
}
