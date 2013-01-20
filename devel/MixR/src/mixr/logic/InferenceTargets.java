/*
 * File name: InferenceTargets.java
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
package mixr.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mixr.components.GoalTransformer;
import org.netbeans.api.annotations.common.NonNull;
import org.openide.util.NbBundle;

/**
 * Contains a collection of formulae that should be applied on by an inference
 * rule. See
 * {@link GoalTransformer#applyInferenceRule(mixr.logic.InferenceTargets, mixr.logic.InferenceRuleDescriptor)}
 * for more information.
 *
 *
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public class InferenceTargets {

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
    private List<Sentence> sentences;
    private final List<InferenceTarget> inferenceTargets;
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     *
     * @param goals this {@link Goals goals} object contains all the selected
     * formulae that should be the target of the rule application.
     *
     * @param targets a collection of locations within the
     * {@link Goals goals collection} which identifies the sentences (formulae)
     * the user has selected for rule application. Can be {@code null}, which
     * indicates that the inference rule should be applied on all goals.
     */
    @NbBundle.Messages({
        "IT_goals_null=Goals are missing from the inference target."
    })
    public InferenceTargets(@NonNull Goals goals, List<InferenceTarget> targets) {
        if (goals == null) {
            throw new IllegalArgumentException(Bundle.IT_goals_null());
        }
        this.goals = goals;
        this.inferenceTargets = Collections.unmodifiableList(targets == null ? new ArrayList<InferenceTarget>() : targets);
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
     * {@link InferenceTargets#getSentences() selected formulae} that should be
     * the target of the rule application.
     */
    public Goals getGoals() {
        return goals;
    }

    /**
     * A collection of sentences (formulae) the user has selected for rule
     * application.
     *
     * @return a collection of sentences (formulae) the user has selected for
     * rule application.
     */
    public List<Sentence> getSentences() {
        if (sentences == null && getInferenceTargets() != null && getInferenceTargets().size() > 0) {
            ArrayList<Sentence> s = new ArrayList<>();
            for (InferenceTarget inferenceTarget : getInferenceTargets()) {
                Sentence t = inferenceTarget.getTargetSentenceFromGoals(goals);
                if (t != null) {
                    s.add(t);
                }
            }
            sentences = Collections.unmodifiableList(s);
        }
        return sentences;
    }

    public Sentence getSentenceAt(int index) {
        return getInferenceTargets().get(index).getTargetSentenceFromGoals(getGoals());
    }

    public List<InferenceTarget> getInferenceTargets() {
        return inferenceTargets;
    }

    public int getInferenceTargetsCount() {
        return getInferenceTargets().size();
    }
    //</editor-fold>
}
