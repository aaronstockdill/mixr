/*
 * File name: GoalsManager.java
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

import mixr.components.GoalProvider;
import mixr.logic.Goal;
import mixr.logic.Goals;
import mixr.logic.InferenceStepResult;
import java.beans.PropertyChangeListener;

/**
 * Keeps a list of currently pending {@link Goal goals} for the currently
 * focused {@link GoalProvider goal-providing resoner}. It provides the
 * goals through the lookup provider API (which proxies the lookup of goals in
 * the currently focused goal-providing reasoner).
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public interface GoalsManager {
    /**
     * Returns the currently active goals of MixR.
     * @return the currently active goals of MixR.
     */
    Goals getCurrentGoals();
    
    /**
     * After a successful interactive application of an inference rule this
     * method should be called.
     * 
     * <p>This method passes the transformed goals back to the originating
     * driver, which in turn passes the transformed goals back to the reasoner.</p>
     * 
     * @param inferenceResult the result of the inference step.
     */
    void commitTransformedGoals(InferenceStepResult inferenceResult);

    //<editor-fold defaultstate="collapsed" desc="Property Changed Stuff">
    /**
     * Registers a property listener. This manager provides the following
     * events: <ul><li>{@link GoalsManager#CurrentGoalsChangedEvent}</li></ul>
     *
     * @param listener the object that will receive the property changed events.
     */
    void addPropertyChangeListener(PropertyChangeListener listener);
    
    /**
     * Registers a property listener.
     *
     * @param listener the object that will receive the property changed events.
     * @param event the event to which the listener wants to be registered (see
     * {@link GoalsManager#addPropertyChangeListener(java.beans.PropertyChangeListener)}
     * for a list of available events).
     */
    void addPropertyChangeListener(PropertyChangeListener listener, String event);
    
    /**
     * Unregisters the given property listener.
     *
     * @param listener the object that will not receive the property changed
     * events anymore.
     */
    void removePropertyChangeListener(PropertyChangeListener listener);
    
    /**
     * Unregisters the given property listener.
     *
     * @param listener the object that will not receive the property changed
     * events anymore.
     * @param event the event from which to deregister this listener.
     */
    void removePropertyChangeListener(PropertyChangeListener listener, String event);
    
    /**
     * The identifier that will come with the {@link GoalsManager#addPropertyChangeListener(java.beans.PropertyChangeListener) property
     * change event} that indicates that the current goals have changed.
     */
    static final String CurrentGoalsChangedEvent = "current_goals_changed";
    //</editor-fold>
}
