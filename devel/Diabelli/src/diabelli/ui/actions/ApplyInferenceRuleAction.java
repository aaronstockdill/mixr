/*
 * File name: ApplyInferenceRuleAction.java
 *    Author: matej
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
package diabelli.ui.actions;

import diabelli.Diabelli;
import diabelli.components.GoalAcceptingReasoner;
import diabelli.components.GoalTransformer;
import diabelli.logic.Goals;
import diabelli.logic.InferenceRuleDescriptor;
import diabelli.logic.InferenceTarget;
import diabelli.logic.Sentence;
import diabelli.ui.GoalsTopComponent;
import diabelli.ui.GoalsTopComponent.GeneralGoalNode;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.actions.Presenter;

/**
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
@ActionID(category = "Diabelli", id = "diabelli.ui.actions.ApplyInferenceRuleAction")
@ActionRegistration(displayName = "#CTL_ApplyInferenceRuleAction", lazy = false)
//@ActionReferences({
//    @ActionReference(path = "Menu/Diabelli", position = 3333)
//})
@Messages("CTL_ApplyInferenceRuleAction=Apply inference rule")
public final class ApplyInferenceRuleAction extends AbstractAction implements Presenter.Popup, Presenter.Menu, ContextAwareAction, LookupListener {

    //<editor-fold defaultstate="collapsed" desc="Fields">
    private final Lookup lookup;
    private Result<GeneralGoalNode> lookupInfo;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public ApplyInferenceRuleAction() {
        this(Utilities.actionsGlobalContext());
    }

    public ApplyInferenceRuleAction(Lookup lookup) {
        putValue(Action.NAME, Bundle.CTL_ApplyInferenceRuleAction());
        this.lookup = lookup;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Initialisation">
    /**
     * Initialises the lookup from which we will get the selected goals/formulae
     * on which to apply the inference rule.
     *
     * <p>This method is not thread-safe and assumes that it will be run in the
     * swing thread.</p>
     */
    private void init() {
        if (lookupInfo == null) {
            lookupInfo = lookup.lookupResult(GoalsTopComponent.GeneralGoalNode.class);
            lookupInfo.addLookupListener(this);
            resultChanged(null);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Action Implementation">
    @Override
    public void actionPerformed(ActionEvent e) {
        init();
        JOptionPane.showMessageDialog(null, "Test! " + ((JMenuItem) e.getSource()).getText());
    }

    @Messages({
        "AIRA_apply_inference_step_menu=Apply inference step",
        "AIRA_apply_inference_step_menu_mnemonic=i",})
    @Override
    public JMenuItem getPopupPresenter() {
        init();

        // Start with the construction of the popup:
        JMenu myMenu = new JMenu(this);
        myMenu.setText(Bundle.AIRA_apply_inference_step_menu());
        myMenu.setMnemonic(Bundle.AIRA_apply_inference_step_menu_mnemonic().charAt(0));

        // Extract the inference target from the selection in the goals window:
        InferenceTarget target = getTarget();

        // Check with all goal-transforming reasoners
        Set<GoalTransformer> goalTransformingReasoners = Lookup.getDefault().lookup(Diabelli.class).getReasonersManager().getGoalTransformingReasoners();
        for (GoalTransformer goalTransformingReasoner : goalTransformingReasoners) {
            if (goalTransformingReasoner.canTransform(target)) {
                // Now add all applicable inference rules to the submenu:
                Collection<? extends InferenceRuleDescriptor> applicableInferenceRules = goalTransformingReasoner.getApplicableInferenceRules(target);
                if (applicableInferenceRules != null && !applicableInferenceRules.isEmpty()) {
                    // Sort the inference rules by their names:
                    final ArrayList<InferenceRuleDescriptor> sortedInfRules = new ArrayList<>(applicableInferenceRules);
                    Collections.sort(sortedInfRules, new Comparator<InferenceRuleDescriptor>() {
                        @Override
                        public int compare(InferenceRuleDescriptor o1, InferenceRuleDescriptor o2) {
                            return o1.getName().compareToIgnoreCase(o2.getName());
                        }
                    });

                    // Now add the inference rules to a sub-menu:
                    JMenu m = new JMenu(this);
                    m.setText(goalTransformingReasoner.getInferenceSetName());
                    for (final InferenceRuleDescriptor inferenceRuleDescriptor : sortedInfRules) {
                        JMenuItem infRuleMI = new JMenuItem(new AbstractActionImpl(inferenceRuleDescriptor));
                        infRuleMI.setText(inferenceRuleDescriptor.getName());
                        infRuleMI.setToolTipText(inferenceRuleDescriptor.getDescription());
                        m.add(infRuleMI);
                    }
                    myMenu.add(m);
                }
            }
        }

        return myMenu;
    }

    /**
     * Inference rules are enabled only if the goal-providing reasoner is able
     * to accept transformed goals and if the user actually selected a
     * transformation target
     *
     * @return {@code true} if this action should be enabled.
     */
    @Override
    public boolean isEnabled() {
        init();

        final Goals currentGoals = Lookup.getDefault().lookup(Diabelli.class).getGoalManager().getCurrentGoals();
        if (currentGoals == null || !(currentGoals.getOwner() instanceof GoalAcceptingReasoner)) {
            return false;
        }

        return lookupInfo != null && !lookupInfo.allInstances().isEmpty();
    }

    @Override
    public JMenuItem getMenuPresenter() {
        return getPopupPresenter();
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new ApplyInferenceRuleAction(lkp);
    }

    @Override
    public void resultChanged(LookupEvent le) {
        Collection<? extends GeneralGoalNode> allInstances = lookupInfo.allInstances();
        setEnabled(allInstances != null && !allInstances.isEmpty());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Rule Application Target Extraction">
    /**
     * Returns the rule application target (the formulae as selected in the goal
     * window).
     *
     * @return the rule application target (the formulae as selected in the goal
     * window).
     */
    private InferenceTarget getTarget() {
        Collection<? extends GeneralGoalNode> allInstances = lookupInfo.allInstances();
        if (allInstances == null || allInstances.isEmpty()) {
            return null;
        } else {
            // Traverse all selected nodes in the goals window and collect all
            // sentences:
            Iterator<? extends GeneralGoalNode> it = allInstances.iterator();
            GeneralGoalNode ggn = it.next();
            // All selected nodes should belong to a `Goals` object...
            // NOTE: maybe we could throw an exception instead of returning a 
            // null target.
            if (ggn.getGoals() == null) {
                return null;
            }

            // Collect the formulae and check that they all belong to the same
            // `Goals` object:
            Goals targetGoals = ggn.getGoals();
            ArrayList<Sentence> targetFormulae = new ArrayList<>();
            targetFormulae.add(getSentenceFromNode(ggn));
            for (; it.hasNext();) {
                ggn = it.next();
                // NOTE: maybe we could throw an exception instead of returning a 
                // null target.
                if (ggn.getGoals() != targetGoals) {
                    return null;
                }
                targetFormulae.add(getSentenceFromNode(ggn));
            }
            return new InferenceTarget(targetGoals, targetFormulae);
        }
    }

    private Sentence getSentenceFromNode(GeneralGoalNode ggn) {
        if (ggn instanceof GoalsTopComponent.GoalNode) {
            GoalsTopComponent.GoalNode goalNode = (GoalsTopComponent.GoalNode) ggn;
            return goalNode.getGoal();
        }

        return ggn.getFormula();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Helper Classes">
    private class AbstractActionImpl extends AbstractAction {

        private final InferenceRuleDescriptor inferenceRuleDescriptor;

        public AbstractActionImpl(InferenceRuleDescriptor inferenceRuleDescriptor) {
            this.inferenceRuleDescriptor = inferenceRuleDescriptor;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final InferenceTarget target = getTarget();
            if (target != null && inferenceRuleDescriptor != null) {
                inferenceRuleDescriptor.getOwner().applyInferenceRule(target, inferenceRuleDescriptor);
            } else {
                Logger.getLogger(AbstractActionImpl.class.getName()).log(Level.SEVERE, "The target formulae or the inference rules were missing for inference rule application.");
            }
        }
    }
    //</editor-fold>
}
