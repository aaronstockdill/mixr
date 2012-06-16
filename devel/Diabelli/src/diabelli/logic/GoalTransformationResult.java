/*
 * File name: GoalTransformationResult.java
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
package diabelli.logic;

import diabelli.components.GoalTransformingReasoner;
import org.netbeans.api.annotations.common.NonNull;
import org.openide.util.NbBundle;

/**
 * This inference step result is produced by {@link GoalTransformingReasoner goal-transforming reasoners}
 * by applying an inference rule on the {@link GoalTransformationResult#getOriginalGoals() original goals}
 * and producing {@link GoalTransformationResult#getTransformedGoals() some transformed goals}.
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public class GoalTransformationResult implements InferenceStepResult {
    
    // <editor-fold defaultstate="collapsed" desc="Fields">
    private final GoalTransformingReasoner slaveReasoner;
    private final Goals transformedGoals;
    private final Goals originalGoals;
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    @NbBundle.Messages({
        "GTR_slave_reasoner_null=No slave reasoner specified. Goal transformation result must have an associated slave reasoner.",
        "GTR_original_goals_null=Original goals must not be null."
    })
    public GoalTransformationResult(@NonNull GoalTransformingReasoner slaveReasoner, @NonNull Goals originalGoals, Goals transformedGoals) {
        if (slaveReasoner == null) {
            throw new IllegalArgumentException(Bundle.GTR_slave_reasoner_null());
        }
        if (originalGoals == null) {
            throw new IllegalArgumentException(Bundle.GTR_original_goals_null());
        }
        this.slaveReasoner = slaveReasoner;
        this.transformedGoals = transformedGoals;
        this.originalGoals = originalGoals;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Properties">
    /**
     * The new goals ({@link GoalTransformationResult#getOriginalGoals()
     * original goals} transformed by {@link
     * GoalTransformationResult#getSlaveReasoner() the slave reasoner}).
     * 
     * <p><span style="font-weight:bold">Note</span>: may return 
     * {@code null} to indicate that goals have been discharged. Same goes for
     * empty goals.</p>
     * 
     * @return new goals (transformed original goals).
     */
    public Goals getTransformedGoals() {
        return transformedGoals;
    }

    /**
     * The reasoner that produced the transformed goals.
     * 
     * @return the reasoner that produced the transformed goals.
     */
    public GoalTransformingReasoner getSlaveReasoner() {
        return slaveReasoner;
    }
    
    /**
     * The goals that were transformed.
     * 
     * @return the goals that were transformed.
     */
    public Goals getOriginalGoals() {
        return originalGoals;
    }
    //</editor-fold>
}
