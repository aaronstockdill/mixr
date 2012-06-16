/*
 * File name: InferenceTarget.java
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
import java.util.Collections;
import java.util.List;
import org.netbeans.api.annotations.common.NonNull;
import org.openide.util.NbBundle;

/**
 * Contains a collection of formulae that should be applied on by an inference
 * rule. See
 * {@link GoalTransformingReasoner#applyInferenceRule(diabelli.logic.InferenceTarget, diabelli.logic.InferenceRuleDescriptor)}
 * for more information.
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public class InferenceTarget {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    /**
     * This {@link Goals goals} object contains all the selected formulae that
     * should be the target of the rule application.
     *
     * <p>This object also provides a reference to the master reasoner who owns
     * the formulae and which should accept the transformed goals.</p>
     */
    private final Goals goals;
    /**
     * A collection of sentences (formulae) the user has selected for rule
     * application.
     */
    private final List<Sentence> sentences;
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     *
     * @param goals this {@link Goals goals} object contains all the selected
     * formulae that should be the target of the rule application.
     * @param sentences a collection of sentences (formulae) the user has
     * selected for rule application. Can be {@code null}, which indicates
     * that the inference rule should be applied on all goals.
     */
    @NbBundle.Messages({
        "IT_goals_null=Goals are missing from the inference target."
    })
    public InferenceTarget(@NonNull Goals goals, List<Sentence> sentences) {
        if (goals == null) {
            throw new IllegalArgumentException(Bundle.IT_goals_null());
        }
        this.goals = goals;
        this.sentences = Collections.unmodifiableList(sentences);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Formulae Selection">
    /**
     * This {@link Goals goals} object contains all the selected formulae that
     * should be the target of the rule application.
     *
     * <p>This object also provides a reference to the master reasoner who owns
     * the formulae and which should accept the transformed goals.</p>
     *
     * @return returns the {@link Goals goals} object that contains all the
     * {@link InferenceTarget#getSentences() selected formulae} that should be
     * the target of the rule application.
     */
    public Goals getGoals() {
        return goals;
    }

    /**
     * A collection of sentences (formulae) the user has selected for rule
     * application.
     * 
     * @return a collection of sentences (formulae) the user has selected for rule
     * application.
     */
    public List<Sentence> getSentences() {
        return sentences;
    }
    //</editor-fold>
}
