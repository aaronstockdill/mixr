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
import diabelli.components.GoalTransformingReasoner;
import diabelli.logic.Goals;
import diabelli.logic.InferenceRuleDescriptor;
import diabelli.logic.InferenceTarget;
import diabelli.ui.GoalsTopComponent;
import diabelli.ui.GoalsTopComponent.GeneralGoalNode;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultButtonModel;
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

    private final Lookup lookup;
    private Result<GeneralGoalNode> lookupInfo;

    public ApplyInferenceRuleAction() {
        this(Utilities.actionsGlobalContext());
    }

    public ApplyInferenceRuleAction(Lookup lookup) {
        putValue(Action.NAME, Bundle.CTL_ApplyInferenceRuleAction());
        this.lookup = lookup;
    }

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
        // TODO: Extract the inference target from the selection in the goals window:
        InferenceTarget target = null;
        // Check with all goal-transforming reasoners
        Set<GoalTransformingReasoner> goalTransformingReasoners = Lookup.getDefault().lookup(Diabelli.class).getReasonersManager().getGoalTransformingReasoners();
        for (GoalTransformingReasoner goalTransformingReasoner : goalTransformingReasoners) {
            if (goalTransformingReasoner.canTransform(target)) {
                // Now add all pplicable inference rules to the submenu:
                Collection<InferenceRuleDescriptor> applicableInferenceRules = goalTransformingReasoner.getApplicableInferenceRules(target);
                if (applicableInferenceRules != null && !applicableInferenceRules.isEmpty()) {
                    final ArrayList<InferenceRuleDescriptor> sortedInfRules = new ArrayList<>(applicableInferenceRules);
                    Collections.sort(sortedInfRules, new Comparator<InferenceRuleDescriptor>() {
                        @Override
                        public int compare(InferenceRuleDescriptor o1, InferenceRuleDescriptor o2) {
                            return o1 == o2 ? 0
                                    : o1.getName().compareToIgnoreCase(o2.getName());
                        }
                    });
                    JMenu m = new JMenu(this);
                    m.setText(goalTransformingReasoner.getInferenceSetName());
                    for (InferenceRuleDescriptor inferenceRuleDescriptor : sortedInfRules) {
                        JMenuItem infRuleMI = new JMenuItem(this);
                        infRuleMI.setText(inferenceRuleDescriptor.getName());
                        infRuleMI.setToolTipText(inferenceRuleDescriptor.getDescription());
                        // TODO: Somehow handle the click!
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
}
