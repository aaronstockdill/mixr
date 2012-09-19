/*
 * File name: SpeedithDriver.java
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
package speedith.diabelli;

import diabelli.components.DiabelliComponent;
import diabelli.components.FormulaFormatsProvider;
import diabelli.components.FormulaPresenter;
import diabelli.components.FormulaPresenter.VisualisationException;
import diabelli.components.FormulaTranslationsProvider;
import diabelli.components.GoalAcceptingReasoner;
import diabelli.components.GoalProvidingReasoner;
import diabelli.components.GoalTransformingReasoner;
import diabelli.components.util.BareGoalProvidingReasoner;
import diabelli.logic.*;
import java.beans.PropertyVetoException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import propity.util.MovableArrayList;
import speedith.core.lang.SpiderDiagram;
import speedith.core.reasoning.InferenceRules;
import speedith.core.reasoning.RuleApplicationResult;
import speedith.diabelli.logic.IsabelleToSpidersTranslator;
import speedith.diabelli.logic.SpeedithFormatDescriptor;
import speedith.diabelli.logic.SpeedithInferenceRuleDescriptor;
import speedith.diabelli.logic.SpiderToIsabelleStringTranslator;
import speedith.diabelli.ui.SpiderDiagramDialog;
import speedith.ui.SpiderDiagramPanel;
import speedith.ui.rules.InteractiveRuleApplication;

/**
 * This is the main class of the Speedith driver for Diabelli. It provides
 * current Speedith's goals to Diabelli and gives changed goals back to the
 * active Speedith proof script.
 *
 * <p>This driver also provides inference rule application functionality. This
 * means that a {@link Goal goal} can be given to this driver, it will present
 * it to the user, who will apply an inference rule on it, then the transformed
 * goal will be passed back to Diabelli, and finally back to the
 * {@link GoalAcceptingReasoner goal-accepting reasoner} to whom the initial
 * goal belonged. </p>
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
@ServiceProvider(service = DiabelliComponent.class)
public class SpeedithDriver extends BareGoalProvidingReasoner implements
        GoalTransformingReasoner,
        FormulaFormatsProvider,
        FormulaTranslationsProvider,
        FormulaPresenter<SpiderDiagram> {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private List<InferenceRuleDescriptor> knownInferenceRules;
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Diabelli Component Implementation">
    @Override
    public String getName() {
        return "Speedith";
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Formula Format Provider Implementation">
    @Override
    public Collection<FormulaFormat<?>> getFormulaFormats() {
        return FormulaFormatsContainer.SpeedithFormats;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Formula Presenter Implementation">
    @Override
    public SpiderDiagramPanel createVisualiserFor(Goal goal) throws VisualisationException {
        if (goal == null) {
            return null;
        }
        return createVisualiserFor(goal.asFormula());
    }

    @Override
    public SpiderDiagramPanel createVisualiserFor(Formula<?> formula) throws VisualisationException {
        if (formula == null) {
            return null;
        }
        for (FormulaFormat<?> formulaFormat : formula.getFormats()) {
            if (canPresent(formulaFormat)) {
                return createVisualiserFor(formula.getRepresentation(formulaFormat));
            }
        }
        return null;
    }

    @Override
    public FormulaFormat<SpiderDiagram> getPresentedFormat() {
        return SpeedithFormatDescriptor.getInstance();
    }

    public boolean canPresent(FormulaFormat<?> format) {
        return SpeedithFormatDescriptor.getInstance() == format;
    }

    @Override
    public SpiderDiagramPanel createVisualiserFor(FormulaRepresentation<?> formula) throws VisualisationException {
        if (formula.getFormula() instanceof SpiderDiagram) {
            SpiderDiagram spiderDiagram = (SpiderDiagram) formula.getFormula();
            if (spiderDiagram.isValid()) {
                return new SpiderDiagramPanel(spiderDiagram);
            } else {
                throw new VisualisationException("The spider diagram is not valid and it cannot be drawn.");
            }
        } else {
            return null;
        }
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Formula Translations Provider Implementation">
    @Override
    public Collection<FormulaTranslator<?, ?>> getFormulaTranslators() {
        return FormulaTranslatorsContainer.SpeedithTranslators;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="GoalTransformingReasoner Implementation">
    @Override
    public boolean canTransform(InferenceTarget target) {
        return getSpiderDiagramFromTarget(target) != null;
    }

    private SpiderDiagram getSpiderDiagramFromTarget(InferenceTarget target) {
        if (target == null || target.getSentences().size() != 1) {
            return null;
        } else {
            Sentence sentence = target.getSentences().get(0);
            if (sentence instanceof Goal) {
                Goal goal = (Goal) sentence;
                ArrayList<? extends FormulaRepresentation<SpiderDiagram>> spiderDiagrams = goal.asFormula().fetchRepresentations(SpeedithFormatDescriptor.getInstance());
                if (spiderDiagrams == null || spiderDiagrams.isEmpty()) {
                    return null;
                }
                return spiderDiagrams.get(0).getFormula();
            } else {
                return null;
            }
        }
    }

    @Override
    public Collection<InferenceRuleDescriptor> getApplicableInferenceRules(InferenceTarget target) {
        return getInferenceRules();
    }

    @Override
    public Collection<InferenceRuleDescriptor> getInferenceRules() {
        // NOTE: I decided not to synchronise this piece of lazy initialisation
        // code. Since the usual case is to be called from the UI thread and
        // this list will never be modified at all it is okay if it gets
        // constructed multiple times.
        if (knownInferenceRules == null) {
            ArrayList<InferenceRuleDescriptor> infRules = new ArrayList<>();
            Set<String> collectedInferenceRules = InferenceRules.getKnownInferenceRules();
            for (String infRuleName : collectedInferenceRules) {
                infRules.add(new SpeedithInferenceRuleDescriptor(this, InferenceRules.getProvider(infRuleName)));
            }

            knownInferenceRules = Collections.unmodifiableList(infRules);
        }
        return knownInferenceRules;
    }

    @NbBundle.Messages({
        "SD_inference_set_name=Spider diagram rules"
    })
    @Override
    public String getInferenceSetName() {
        return Bundle.SD_inference_set_name();
    }

    @NbBundle.Messages({
        "SD_unknown_inf_rule=Speedith could not apply the given inference rule. The rule `{0}` is not known to Speedith.",
        "SD_application_error_title=Inference rule application failed",
        "SD_application_error_message=The inference rule `{0}` was not applied.\nIt failed for the following reason:\n\n`{1}`"
    })
    @Override
    public void applyInferenceRule(InferenceTarget targets, InferenceRuleDescriptor infRuleDescriptor) {
        if (infRuleDescriptor instanceof SpeedithInferenceRuleDescriptor) {
            SpeedithInferenceRuleDescriptor infRule = (SpeedithInferenceRuleDescriptor) infRuleDescriptor;
            try {
                // Apply inference rule interactively:
                speedith.ui.SpeedithMainForm a;
                RuleApplicationResult applicationResult = InteractiveRuleApplication.applyRuleInteractively(infRule.getInfRuleProvider().getInferenceRuleName(), getSpiderDiagramFromTarget(targets));
                // Show the user the results and aske them whether the results
                // should be passed back to the master reasoner.
                SpiderDiagramDialog sdd = new SpiderDiagramDialog(null, true, applicationResult.getGoals());
                sdd.pack();
                sdd.setVisible(true);
                if (sdd.isCancelled()) {
                    return;
                }
                // Put the result back to the master reasoner:
                GoalProvidingReasoner masterReasoner = targets.getGoals().getOwner();
                if (masterReasoner instanceof GoalAcceptingReasoner) {
                    GoalAcceptingReasoner goalAcceptingReasoner = (GoalAcceptingReasoner) masterReasoner;
                    targets.getGoals().toArray();
                    @SuppressWarnings({"rawtypes", "unchecked"})
                    GoalTransformationResult goalTransformationResult = new GoalTransformationResult(this, targets.getGoals(), new MovableArrayList[]{
                                new MovableArrayList<Goal>(Arrays.asList(new Goal[]{
                                    new Goal(null, null, null, new Formula<>(new FormulaRepresentation<>(applicationResult.getGoals().getGoalAt(0), SpeedithFormatDescriptor.getInstance()), Formula.FormulaRole.Goal))
                                }))
                            });
                    goalAcceptingReasoner.commitTransformedGoals(goalTransformationResult);
                }
            } catch (Exception ex) {
                Logger.getLogger(SpeedithDriver.class.getName()).log(Level.INFO, Bundle.SD_application_error_message(infRule.getName(), ex.getLocalizedMessage()), ex);
                JOptionPane.showMessageDialog(null, Bundle.SD_application_error_message(infRule.getName(), ex.getLocalizedMessage()), Bundle.SD_application_error_title(), JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            Logger.getLogger(SpeedithDriver.class.getName()).log(Level.SEVERE, Bundle.SD_unknown_inf_rule(infRuleDescriptor.getName()));
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Protected Properties">
    /**
     * Sets the goals and fires the goals changed event if the new goals differ
     * from the current ones.
     *
     * @param goals the new goals to be set.
     * @throws PropertyVetoException thrown if the new goals could not be set
     * for any reason.
     */
    @NbBundle.Messages(value = {"BGPR_goals_change_vetoed="})
    public void setGoals(Goals goals) throws PropertyVetoException {
        if (this.goals != goals) {
            preCurrentGoalsChanged(this.goals, goals);
            Goals oldGoals = this.goals;
            this.goals = goals;
            fireCurrentGoalsChangedEvent(oldGoals);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Changed Event Stuff">
    protected void fireCurrentGoalsChangedEvent(Goals oldGoals) {
        pcs.firePropertyChange(CurrentGoalsChangedEvent, oldGoals, goals);
    }

    /**
     * This method is invoked by the default implementation of
     * {@link BareGoalProvidingReasoner#setGoals(diabelli.logic.Goals)} just
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
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Lazy Initialisation Helpers">
    private static class FormulaFormatsContainer {

        private static final Set<FormulaFormat<?>> SpeedithFormats;

        static {
            HashSet<FormulaFormat<?>> tmp = new HashSet<>();
            tmp.add(SpeedithFormatDescriptor.getInstance());
            SpeedithFormats = Collections.unmodifiableSet(tmp);
        }
    }

    private static class FormulaTranslatorsContainer {

        private static final List<FormulaTranslator<?, ?>> SpeedithTranslators;

        static {
            ArrayList<FormulaTranslator<?, ?>> tmp = new ArrayList<>();
            tmp.add(IsabelleToSpidersTranslator.getInstance());
            tmp.add(SpiderToIsabelleStringTranslator.getInstance());
            SpeedithTranslators = Collections.unmodifiableList(tmp);
        }
    }
    // </editor-fold>
}
