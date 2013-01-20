/*
 * File name: InferenceTarget.java
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
package mixr.logic;

/**
 * This object uniquely describes the exact target of a rule application.
 *
 * <p>It tells which goal or which of its subformulae should be the target of
 * the inference.</p>
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public class InferenceTarget {

    //<editor-fold defaultstate="collapsed" desc="Fields">
    private int goalIndex;
    private int subformulaIndex;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public InferenceTarget(int goalIndex) {
        this(goalIndex, -1);
    }
    
    public InferenceTarget(int goalIndex, int subformulaIndex) {
        this.goalIndex = goalIndex;
        this.subformulaIndex = subformulaIndex;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Interface">
    public int getGoalIndex() {
        return goalIndex;
    }
    
    public int getSubformulaIndex() {
        return subformulaIndex;
    }
    
    public boolean isGoal() {
        return getSubformulaIndex() < 0;
    }
    
    public boolean isPremiseOf(Goal goal) {
        return getSubformulaIndex() >= 0 && getSubformulaIndex() < goal.getPremisesCount();
    }
    
    public boolean isConclusionOf(Goal goal) {
        return getSubformulaIndex() == goal.getPremisesCount();
    }
    
    /**
     * Returns the sentence on which the inference rule should be applied.
     *
     * <p>If this target object points to a goal or as subformula that is not
     * within the given {@link Goals goals collection}, then {@code null} is
     * returned.</p>
     *
     * @param goals the goals within which to search for the target sentence of
     * the inference.
     * @return the sentence on which the inference rule should be applied.
     */
    public Sentence getTargetSentenceFromGoals(Goals goals) {
        if (goals == null || getGoalIndex() >= goals.size() || getGoalIndex() < 0) {
            return null;
        }
        Goal theGoal = goals.get(getGoalIndex());
        if (getSubformulaIndex() < 0) {
            return theGoal;
        }
        if (getSubformulaIndex() < theGoal.getPremisesCount()) {
            return theGoal.getPremiseAt(getSubformulaIndex());
        } else if (getSubformulaIndex() == theGoal.getPremisesCount()) {
            return theGoal.getConclusion();
        } else {
            return null;
        }
    }
    //</editor-fold>
}
