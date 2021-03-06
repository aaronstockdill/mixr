/*
 * File name: GoalsTopComponent.java
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
package mixr.ui;

import mixr.MixR;
import mixr.GoalsManager;
import mixr.logic.Formula;
import mixr.logic.Goal;
import mixr.logic.Goals;
import mixr.ui.Bundle;
import mixr.ui.actions.ApplyInferenceRuleAction;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.tree.TreeSelectionModel;
import mixr.logic.InferenceTarget;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.TreeTableView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;
import org.openide.windows.TopComponent;

/**
 * This window displays currently active goals and allows the user to select a
 * particular goal or parts of it. User's selection is managed by the provided
 * {@link GoalsTopComponent#getExplorerManager() explorer manager} and the
 * {@link GoalsTopComponent#getLookup() associated lookup}.
 *
 * <p>The nodes that can be found in the explorer manager of this component are
 * of type {@link GeneralGoalNode}.</p>
 */
@ConvertAsProperties(dtd = "-//mixr.ui//Goals//EN",
autostore = false)
@TopComponent.Description(preferredID = GoalsTopComponent.PreferredID,
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "navigator", openAtStartup = true)
@ActionID(category = "Window", id = "mixr.ui.GoalsTopComponent")
@ActionReference(path = "Menu/Window/MixR", position = 100)
@TopComponent.OpenActionRegistration(displayName = "#CTL_GoalsAction",
preferredID = GoalsTopComponent.PreferredID)
@Messages({
    "CTL_GoalsAction=MixR Goals",
    "CTL_GoalsTopComponent=MixR Goals",
    "HINT_GoalsTopComponent=This window displays the list of current MixR goals.",
    "GTC_root_node_display_name=MixR list of goals"
})
public final class GoalsTopComponent extends TopComponent implements ExplorerManager.Provider {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private static final long serialVersionUID = 0x5b5c7d5e172704ebL;
    private ExplorerManager em;
    private Lookup lookup;
    private GoalsChangedListenerImpl goalsChangedListener;
    /**
     * The ID used to register this top component.
     */
    public static final String PreferredID = "GoalsTopComponent";
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public GoalsTopComponent() {
        initComponents();
        setName(Bundle.CTL_GoalsTopComponent());
        setToolTipText(Bundle.HINT_GoalsTopComponent());

        this.em = new ExplorerManager();
        ActionMap map = this.getActionMap();
        InputMap keys = this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        this.lookup = ExplorerUtils.createLookup(this.em, map);
        this.associateLookup(this.lookup);

        // Make the root node invisible in the view:
        ((TreeTableView) goalsView).setRootVisible(false);
        ((TreeTableView) goalsView).setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);

        updateGoalsList();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Generated Code">
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        goalsView = new TreeTableView();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(goalsView, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(goalsView, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane goalsView;
    // End of variables declaration//GEN-END:variables
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TopComponent-Specific Stuff">
    @Override
    public void componentOpened() {
        // Register to 'goal change' events in MixR's goal manager:
        if (goalsChangedListener == null) {
            goalsChangedListener = new GoalsChangedListenerImpl();
        }
        GoalsManager goalManager = Lookup.getDefault().lookup(MixR.class).getGoalManager();
        goalManager.addPropertyChangeListener(goalsChangedListener, GoalsManager.CurrentGoalsChangedEvent);
        updateGoalsList();
    }

    @Override
    public void componentClosed() {
        // Unregister to 'goal change' events in MixR's goal manager:
        if (goalsChangedListener != null) {
            GoalsManager goalManager = Lookup.getDefault().lookup(MixR.class).getGoalManager();
            goalManager.addPropertyChangeListener(goalsChangedListener, GoalsManager.CurrentGoalsChangedEvent);
        }
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Goal Explorer Nodes">
    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }

    //<editor-fold defaultstate="collapsed" desc="Children Factories">
    private static class GoalChildrenFactory extends ChildFactory<GoalNode> {

        private final Goals goals;

        public GoalChildrenFactory(Goals goals) {
            this.goals = goals;
        }

        @Override
        protected boolean createKeys(List<GoalNode> toPopulate) {
            if (goals != null && !goals.isEmpty()) {
                for (int i = 0; i < goals.size(); i++) {
                    toPopulate.add(new GoalNode(goals, i));
                }
            }
            return true;
        }

        @Override
        protected GoalNode createNodeForKey(GoalNode key) {
            return key;
        }
    }

    private static class GoalPremisesConclusionFactory extends ChildFactory<AbstractNode> {

        private final GeneralGoalNode goalNode;

        public GoalPremisesConclusionFactory(GeneralGoalNode goalNode) {
            this.goalNode = goalNode;
        }

        @Override
        protected boolean createKeys(List<AbstractNode> toPopulate) {
            if (goalNode.getGoal().getPremisesCount() > 0 && goalNode.getGoal().getPremisesFormula().isEmpty()) {
                toPopulate.add(new PremisesNode(goalNode));
            }
            if (goalNode.getGoal().getConclusion() != null) {
                toPopulate.add(new ConclusionNode(goalNode));
            }
            return true;
        }

        @Override
        protected Node createNodeForKey(AbstractNode key) {
            return key;
        }
    }

    private static class PremisesFactory extends ChildFactory<AbstractNode> {

        private final PremisesNode premisesNode;

        public PremisesFactory(PremisesNode premiseNode) {
            this.premisesNode = premiseNode;
        }

        @Override
        protected boolean createKeys(List<AbstractNode> toPopulate) {
            int premisesCount = premisesNode.getGoal().getPremisesCount();
            for (int premiseIndex = 0; premiseIndex < premisesCount; premiseIndex++) {
                toPopulate.add(new PremiseNode(premisesNode, premiseIndex));
            }
            return true;
        }

        @Override
        protected Node createNodeForKey(AbstractNode key) {
            return key;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Explorer Nodes">
    /**
     * The base type of all explorer nodes in the {@link GoalsTopComponent}.
     */
    public abstract static class GeneralGoalNode extends AbstractNode {

        private final Goals goals;
        private final int goalIndex;
        protected InferenceTarget inferenceTarget;

        GeneralGoalNode(Goals goals, int goalIndex, Children children, Lookup lookup) {
            super(children, lookup);
            this.goals = goals;
            this.goalIndex = goalIndex;
        }

        GeneralGoalNode(Goals goals, int goalIndex, Children children) {
            super(children);
            this.goals = goals;
            this.goalIndex = goalIndex;
        }

        /**
         * Returns the goals that contain the
         * {@link GeneralGoalNode#getGoal() goal} connected to this node.
         *
         * @return the goals that contain the
         * {@link GeneralGoalNode#getGoal() goal} connected to this node.
         */
        public final Goals getGoals() {
            return goals;
        }

        /**
         * Returns the goal connected to this node.
         *
         * @return the goal connected to this node.
         */
        public final Goal getGoal() {
            return goals.get(goalIndex);
        }

        public final int getGoalIndex() {
            return goalIndex;
        }

        /**
         * Returns the formula that corresponds to this node.
         *
         * @return the formula that corresponds to this node.
         */
        public abstract Formula getFormula();

        @Override
        public Action[] getActions(boolean context) {
            return new ApplyInferenceRuleAction[]{new ApplyInferenceRuleAction(Utilities.actionsGlobalContext())};
        }

        public InferenceTarget getInferenceTarget() {
            if (inferenceTarget == null) {
                inferenceTarget = new InferenceTarget(getGoalIndex());
            }
            return inferenceTarget;
        }
    }

    /**
     * This node corresponds directly to a particular {@link Goal goal} in the
     * displayed {@link GoalsManager#getCurrentGoals() currently active goals}.
     */
    @Messages({
        "# {0} - goalNumber",
        "FN_goal_display_name=Goal {0}"
    })
    public static final class GoalNode extends GeneralGoalNode {

        GoalNode(Goals goals, int goalIndex) {
            super(goals, goalIndex, Children.LEAF, Lookups.singleton(goals));
            setDisplayName(Bundle.FN_goal_display_name(goalIndex + 1));
            setChildren(Children.create(new GoalPremisesConclusionFactory(this), false));
        }

        @Override
        public Formula getFormula() {
            return this.getGoal().asFormula();
        }
    }

    /**
     * This node corresponds directly to the
     * {@link Goal#getConclusion() conclusion} of a particular {@link Goal goal}
     * in the displayed
     * {@link GoalsManager#getCurrentGoals() currently active goals}.
     */
    @Messages({
        "FN_conclusion_display_name=Conclusion"
    })
    public static final class ConclusionNode extends GeneralGoalNode {

        private ConclusionNode(GeneralGoalNode parentGoal) {
            super(parentGoal.getGoals(), parentGoal.getGoalIndex(), Children.LEAF);
            setDisplayName(Bundle.FN_conclusion_display_name());
        }

        @Override
        public Formula getFormula() {
            return this.getGoal().getConclusion();
        }

        @Override
        public InferenceTarget getInferenceTarget() {
            if (inferenceTarget == null) {
                inferenceTarget = new InferenceTarget(getGoalIndex(), getGoal().getPremisesCount());
            }
            return inferenceTarget;
        }
    }

    /**
     * This node corresponds directly to the {@link Goal#getPremises() premises}
     * of a particular {@link Goal goal} in the displayed
     * {@link GoalsManager#getCurrentGoals() currently active goals}.
     */
    @Messages({
        "PN_premises_display_name=Premises"
    })
    public static final class PremisesNode extends GeneralGoalNode {

        private PremisesNode(GeneralGoalNode parentGoal) {
            super(parentGoal.getGoals(), parentGoal.getGoalIndex(), Children.LEAF);
            setDisplayName(Bundle.PN_premises_display_name());
            setChildren(Children.create(new PremisesFactory(this), false));
        }

        @Override
        public Formula getFormula() {
            return this.getGoal().getPremisesFormula();
        }
    }

    /**
     * This node corresponds directly to a
     * {@link Goal#getPremiseAt(int) particular premise} of a particular
     * {@link Goal goal} in the displayed
     * {@link GoalsManager#getCurrentGoals() currently active goals}.
     */
    @Messages({
        "# {0} - premiseNumber",
        "PN_premise_display_name=Premise {0}"
    })
    public static final class PremiseNode extends GeneralGoalNode {

        private final int premiseIndex;

        private PremiseNode(GeneralGoalNode parentGoal, int premiseIndex) {
            super(parentGoal.getGoals(), parentGoal.getGoalIndex(), Children.LEAF);
            this.premiseIndex = premiseIndex;
            this.setDisplayName(Bundle.PN_premise_display_name(premiseIndex + 1));
        }

        /**
         * Returns the index of the premise that this node <span
         * style="font-style:italic;">carries</span>.
         *
         * @return the index of the premise that this node <span
         * style="font-style:italic;">carries</span>.
         */
        public final int getPremiseIndex() {
            return premiseIndex;
        }

        @Override
        public Formula getFormula() {
            return this.getGoal().getPremiseAt(this.getPremiseIndex());
        }

        @Override
        public InferenceTarget getInferenceTarget() {
            if (inferenceTarget == null) {
                inferenceTarget = new InferenceTarget(getGoalIndex(), getPremiseIndex());
            }
            return inferenceTarget;
        }
    }
    //</editor-fold>
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Event Handlers">
    private class GoalsChangedListenerImpl implements PropertyChangeListener {

        private GoalsChangedListenerImpl() {
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            updateGoalsList((Goals) evt.getNewValue());
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="UI Refresh Methods">
    private void updateGoalsList(Goals goals) {
        Children children = Children.create(new GoalChildrenFactory(goals), false);
        Node root = new AbstractNode(children);
        this.em.setRootContext(root);
        this.em.getRootContext().setDisplayName(Bundle.GTC_root_node_display_name());
    }

    private void updateGoalsList() {
        GoalsManager goalManager = Lookup.getDefault().lookup(MixR.class).getGoalManager();
        if (goalManager.getCurrentGoals() != null) {
            updateGoalsList(goalManager.getCurrentGoals());
        } else {
            updateGoalsList(null);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Private Helper Methods">
    /**
     * Checks if premises were selected and returns a non-{@code null} array of
     * selected premise nodes if and only if:
     *
     * <ul>
     *
     * <li>all selected nodes are premises, and</li>
     *
     * <li>all of them are from the same goal, and</li>
     *
     * <li>at least two premises are selected.</li>
     *
     * </ul>
     *
     * In every other case this method returns {@code null}.
     *
     * @param selectedNodes the nodes representing the selected
     * @return an array of selected premises or {@code null}.
     */
    public static PremiseNode[] checkPremisesSelected(Node[] selectedNodes) {
        if (selectedNodes == null || selectedNodes.length < 2) {
            return null;
        }
        Goal commonGoal = null;
        for (int i = 0; i < selectedNodes.length; i++) {
            Node node = selectedNodes[i];
            if (!(node instanceof PremiseNode)) {
                return null;
            }
            PremiseNode premiseNode = (PremiseNode) node;
            // Is the premise from the same goal?
            if (commonGoal == null) {
                commonGoal = premiseNode.getGoal();
            } else if (premiseNode.getGoal() != commonGoal) {
                return null;
            }
        }
        // Good, the selected premises passed the test.
        return Arrays.copyOf(selectedNodes, selectedNodes.length, PremiseNode[].class);
    }
    // </editor-fold>
}
