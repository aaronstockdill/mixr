/*
 * File name: CurrentFormulaTopComponent.java
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

import mixr.ui.GoalsTopComponent.ConclusionNode;
import mixr.ui.GoalsTopComponent.GeneralGoalNode;
import mixr.ui.GoalsTopComponent.PremiseNode;
import mixr.ui.GoalsTopComponent.PremisesNode;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.tree.TreeSelectionModel;
import mixr.logic.Formula;
import mixr.logic.FormulaRepresentation;
import mixr.logic.Goal;
import mixr.logic.Goals;
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
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * This window gives the user the option to browse particular representations
 * (formats) of a formula selected in the {@link GoalsTopComponent}.
 *
 *
 * User's selection of the contents of this component is managed by the provided
 * {@link CurrentFormulaTopComponent#getExplorerManager() explorer manager} and
 * the {@link CurrentFormulaTopComponent#getLookup() associated lookup}.
 *
 * <p>The nodes that can be found in the explorer manager of this component are
 * of type {@link GeneralFormulaNode}.</p>
 */
@ConvertAsProperties(dtd = "-//mixr.ui//CurrentFormula//EN",
autostore = false)
@TopComponent.Description(preferredID = CurrentFormulaTopComponent.PreferredID,
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "navigator", openAtStartup = true)
@ActionID(category = "Window", id = "mixr.ui.CurrentFormulaTopComponent")
@ActionReference(path = "Menu/Window/MixR", position = 200)
@TopComponent.OpenActionRegistration(displayName = "#CTL_CurrentFormulaAction",
preferredID = CurrentFormulaTopComponent.PreferredID)
@Messages({
    "CTL_CurrentFormulaAction=Current Formula",
    "CTL_CurrentFormulaTopComponent=Current Formula",
    "HINT_CurrentFormulaTopComponent=Shows details for the currently selected formula in the 'MixR Goals' window.",
    "CFTC_root_node_display_name=Formula formats list:"
})
public final class CurrentFormulaTopComponent extends TopComponent implements ExplorerManager.Provider {

    //<editor-fold defaultstate="collapsed" desc="Fields">
    public static final String PreferredID = "CurrentFormulaTopComponent";
    private static final long serialVersionUID = 0xf15aa95a16c4e69aL;
    private final GoalSelectionListener goalSelectionListener;
    private ExplorerManager em;
    private Lookup lookup;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    public CurrentFormulaTopComponent() {
        initComponents();
        setName(Bundle.CTL_CurrentFormulaTopComponent());
        setToolTipText(Bundle.HINT_CurrentFormulaTopComponent());

        this.em = new ExplorerManager();
        ActionMap map = this.getActionMap();
        InputMap keys = this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        this.lookup = ExplorerUtils.createLookup(this.em, map);
        this.associateLookup(this.lookup);

        // Make the root node invisible in the view:
        ((TreeTableView) goalSelectionView).setRootVisible(false);
        // Only one formula may be selected at a time:
        ((TreeTableView) goalSelectionView).setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        // Create the listener that will tell us when the user has changed the
        // focused goal.
        goalSelectionListener = new GoalSelectionListener();

        // Let the initially displayed thing be an empty node:
        resetRootNode(Node.EMPTY);
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

        goalSelectionView = new TreeTableView();

        setLayout(new java.awt.BorderLayout());
        add(goalSelectionView, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane goalSelectionView;
    // End of variables declaration//GEN-END:variables
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TopComponent Overrides">
    @Messages({
        "CFTC_goalsTopComponent_notFound=Could not find the GoalsTopComponent. This is a bug and should never happen."
    })
    @Override
    public void componentOpened() {
        GoalsTopComponent goalsTopComponent = (GoalsTopComponent) WindowManager.getDefault().findTopComponent(GoalsTopComponent.PreferredID);
        if (goalsTopComponent != null) {
            goalsTopComponent.getExplorerManager().addPropertyChangeListener(goalSelectionListener);
            updateSelectionFrom(goalsTopComponent.getExplorerManager());
        } else {
            throw new IllegalStateException(Bundle.CFTC_goalsTopComponent_notFound());
        }
    }

    @Override
    public void componentClosed() {
        GoalsTopComponent goalsTopComponent = (GoalsTopComponent) WindowManager.getDefault().findTopComponent(GoalsTopComponent.PreferredID);
        if (goalsTopComponent != null) {
            goalsTopComponent.getExplorerManager().removePropertyChangeListener(goalSelectionListener);
        } else {
            throw new IllegalStateException(Bundle.CFTC_goalsTopComponent_notFound());
        }
        resetRootNode(Node.EMPTY);
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

    //<editor-fold defaultstate="collapsed" desc="Explorer Manager Provider">
    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Goal Selection Change Listener">
    private class GoalSelectionListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (ExplorerManager.PROP_SELECTED_NODES.equals(evt.getPropertyName())) {
                ExplorerManager em = (ExplorerManager) evt.getSource();
                updateSelectionFrom(em);
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GUI Update Methods">
    private void resetRootContextTitle() {
        this.em.getRootContext().setDisplayName(Bundle.CFTC_root_node_display_name());
    }

    private void resetRootNode(final Node rootNode) {
        this.em.setRootContext(rootNode);
        resetRootContextTitle();
    }

    private <T extends Node> void wrapAndSetRootNode(final T aNode) {
        resetRootNode(aNode);
    }

    private void showConclusion(ConclusionNode conclusionNode) {
        wrapAndSetRootNode(new ConclusionFormulaNode(conclusionNode));
    }

    private void showPremise(PremiseNode premiseNode) {
        wrapAndSetRootNode(new PremiseFormulaNode(premiseNode));
    }

    private void showGoal(GeneralGoalNode generalGoalNode) {
        wrapAndSetRootNode(new GoalFormulaNode(generalGoalNode));
    }

    private void showPremises(PremisesNode premisesNode) {
        wrapAndSetRootNode(new PremisesFormulaNode(premisesNode));
    }

    private void updateSelection(GeneralGoalNode generalGoalNode) {
        if (generalGoalNode == null) {
            resetRootNode(Node.EMPTY);
        } else if (generalGoalNode instanceof GoalsTopComponent.PremisesNode) {
            showPremises((GoalsTopComponent.PremisesNode) generalGoalNode);
        } else if (generalGoalNode instanceof GoalsTopComponent.ConclusionNode) {
            showConclusion((GoalsTopComponent.ConclusionNode) generalGoalNode);
        } else if (generalGoalNode instanceof GoalsTopComponent.PremiseNode) {
            showPremise((GoalsTopComponent.PremiseNode) generalGoalNode);
        } else {
            showGoal(generalGoalNode);
        }
    }

    private void updateSelectionFrom(ExplorerManager em) {
        Node[] selectedNodes = em.getSelectedNodes();
        if (selectedNodes != null && selectedNodes.length > 0 && selectedNodes[0] instanceof GeneralGoalNode) {
            // Did the user select a bunch of premises from the same goal:
            PremiseNode[] premises = GoalsTopComponent.checkPremisesSelected(selectedNodes);
            if (premises != null) {
                // The user selected a bunch of premises. What to do?
                Logger.getLogger(CurrentFormulaTopComponent.class.getName()).log(Level.INFO, "The user selected a bunch of premises. It is not yet decided what we want to do with them.");
            } else {
                updateSelection((GeneralGoalNode) selectedNodes[0]);
            }
        } else {
            updateSelection(null);
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Explorer Nodes">
    /**
     * Nodes of this type provide exact information on what formula (and in what
     * type) the user wants MixR to focus on.
     *
     * <p>An example of a selection: the user selects a particular node in {@link GoalsTopComponent the
     * window that lists the current goals}. Immediately afterwards, {@link CurrentFormulaTopComponent the current selection window}
     * displays all the formats of the user's selection. Initially, the main
     * representation is selected, but the user can then switch between
     * representations by selecting a particular node in the <span
     * style="font-style:italic;">current selection window</span>.</p>
     *
     * @param <T> The type of the node as selected in the {@link GoalsTopComponent current MixR goals window}.
     */
    public abstract static class GeneralFormulaNode<T extends GeneralGoalNode> extends AbstractNode {

        protected final T goal;

        GeneralFormulaNode(T goalNode, Children children) {
            super(children);
            this.goal = goalNode;
        }

        /**
         * Returns the selected formula that this node represents.
         *
         * <p>The default implementation calls {@link GeneralFormulaNode#getSelectedGoal()}
         * and returns its {@link Goal#asFormula() formula}.</p>
         *
         * @return the selected formula that this node represents.
         */
        public Formula getSelectedFormula() {
            return getSelectedGoal().asFormula();
        }

        /**
         * Returns the selected representation of the selected formula.
         *
         * <p>The default implementation calls {@link GeneralFormulaNode#getSelectedFormula()}
         * and returns its main representation.</p>
         *
         * @return the selected representation of the selected formula.
         */
        public FormulaRepresentation getSelectedFormulaRepresentation() {
            return getSelectedFormula().getMainRepresentation();
        }

        /**
         * Returns the selected goal that this node represents.
         *
         * @return the selected goal that this node represents.
         */
        public final Goal getSelectedGoal() {
            return goal.getGoal();
        }

        /**
         * Returns the index of the selected goal that this node represents.
         *
         * @return the index of the selected goal that this node represents.
         */
        public final int getSelectedGoalIndex() {
            return goal.getGoalIndex();
        }

        /**
         * Returns the {@link Goals goals object} that hosts the formula
         * selection represented by this node.
         *
         * @return the {@link Goals goals object} that hosts the formula
         * selection represented by this node.
         */
        public final Goals getHostingGoals() {
            return goal.getGoals();
        }
    }

    @Messages({
        "CurrentPremiseNode_display_name=Premise {0} of goal {1}"
    })
    public static class PremiseFormulaNode extends GeneralFormulaNode<PremiseNode> {

        PremiseFormulaNode(PremiseNode premise) {
            super(premise, Children.LEAF);
            setDisplayName(Bundle.CurrentPremiseNode_display_name(premise.getPremiseIndex() + 1, premise.getGoalIndex() + 1));
            setChildren(Children.create(new FormulaRepresentationsChildren<> (this), false));
        }

        @Override
        public Formula getSelectedFormula() {
            return goal.getGoal().getPremiseAt(goal.getPremiseIndex());
        }

        public final int getPremiseIndex() {
            return goal.getPremiseIndex();
        }
    }

    @Messages({
        "PremisesFormulaNode_display_name=All premises of goal {0}"
    })
    public static class PremisesFormulaNode extends GeneralFormulaNode<PremisesNode> {

        PremisesFormulaNode(PremisesNode premises) {
            super(premises, Children.LEAF);
            setDisplayName(Bundle.PremisesFormulaNode_display_name(premises.getGoalIndex() + 1));
            setChildren(Children.create(new FormulaRepresentationsChildren<>(this), false));
        }

        @Override
        public Formula getSelectedFormula() {
            return goal.getGoal().getPremisesFormula();
        }
    }

    @Messages({
        "CurrentConclusionNode_display_name=Conclusion of goal {0}"
    })
    public static class ConclusionFormulaNode extends GeneralFormulaNode<ConclusionNode> {

        ConclusionFormulaNode(ConclusionNode conclusion) {
            super(conclusion, Children.LEAF);
            setDisplayName(Bundle.CurrentConclusionNode_display_name(conclusion.getGoalIndex() + 1));
            setChildren(Children.create(new FormulaRepresentationsChildren<>(this), false));
        }

        @Override
        public Formula getSelectedFormula() {
            return goal.getGoal().getConclusion();
        }
    }

    @Messages({
        "CurrentGoalNode_display_name=The entire goal {0}"
    })
    public static class GoalFormulaNode extends GeneralFormulaNode<GeneralGoalNode> {

        GoalFormulaNode(GeneralGoalNode goal) {
            super(goal, Children.LEAF);
            setDisplayName(Bundle.CurrentGoalNode_display_name(goal.getGoalIndex() + 1));
            setChildren(Children.create(new FormulaRepresentationsChildren<>(this), false));
        }
    }

    public static abstract class FormulaDelegateNode<T extends GeneralGoalNode> extends GeneralFormulaNode<T> {

        private final GeneralFormulaNode<T> delegate;

        FormulaDelegateNode(GeneralFormulaNode<T> selection, Children children) {
            super(selection.goal, children);
            this.delegate = selection;
        }

        @Override
        public Formula getSelectedFormula() {
            return delegate.getSelectedFormula();
        }

        @Override
        public FormulaRepresentation getSelectedFormulaRepresentation() {
            return delegate.getSelectedFormulaRepresentation();
        }

        /**
         * The node to which this one delegates.
         *
         * @return the node to which this one delegates.
         */
        public final GeneralFormulaNode<T> getUnderlyingNode() {
            return delegate;
        }

        /**
         * Returns the formula node on which this delegate node is based. In
         * other words, this method returns a {@link GeneralFormulaNode formula
         * node} that is of one of the following types:
         *
         * <ul>
         *
         * <li>{@link GoalFormulaNode},</li>
         *
         * <li>{@link PremisesFormulaNode},</li>
         *
         * <li>{@link PremiseFormulaNode}, or</li>
         *
         * <li>{@link ConclusionFormulaNode}.</li>
         *
         * </ul>
         *
         * @return the formula node on which this delegate node is based.
         */
        public final GeneralFormulaNode<T> getBaseFormulaNode() {
            if (delegate instanceof FormulaDelegateNode<?>) {
                FormulaDelegateNode<T> formulaDelegateNode = (FormulaDelegateNode<T>) delegate;
                return formulaDelegateNode.getBaseFormulaNode();
            } else {
                return delegate;
            }
        }
    }

    public static class RepresentationFormulaNode<T extends GeneralGoalNode> extends FormulaDelegateNode<T> {

        final List<? extends FormulaRepresentation> representations;
        final int representationIndex;

        @Messages({
            "FormatRepresentationNode_displayName=Representation {0}: {1}",
            "FormatRepresentationNode_displayName_main_representation=Main representation: {0}"
        })
        RepresentationFormulaNode(GeneralFormulaNode<T> selection, List<? extends FormulaRepresentation> representations, int representationIndex) {
            super(selection, Children.LEAF);
            this.representations = representations;
            this.representationIndex = representationIndex;
            if (selection.getSelectedFormula().getMainRepresentation() == representations.get(representationIndex)) {
                setDisplayName(Bundle.FormatRepresentationNode_displayName_main_representation(representations.get(representationIndex).getFormat().getPrettyName()));
            } else {
                setDisplayName(Bundle.FormatRepresentationNode_displayName(representationIndex + 1, representations.get(representationIndex).getFormat().getPrettyName()));
            }
        }

        @Override
        public FormulaRepresentation getSelectedFormulaRepresentation() {
            return representations.get(representationIndex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Formula Format Node Factories">
    /**
     * This node children factory produces all the representations of the
     * given formula.
     * @param <T> 
     */
    private static class FormulaRepresentationsChildren<T extends GeneralGoalNode> extends ChildFactory<RepresentationFormulaNode<T>> {

        private final GeneralFormulaNode<T> source;

        FormulaRepresentationsChildren(GeneralFormulaNode<T> source) {
            this.source = source;
        }

        @Override
        protected boolean createKeys(List<RepresentationFormulaNode<T>> toPopulate) {
            // Enumerate all the representations of the currently selected
            // formula:
            Formula selectedFormula = source.getSelectedFormula();
            if (selectedFormula == null || selectedFormula.getRepresentationsCount() < 1)
                return true;
            List<FormulaRepresentation> representations = Arrays.asList(selectedFormula.getRepresentations());
            for (int i = 0; i < representations.size(); i++) {
                toPopulate.add(new RepresentationFormulaNode<>(source, representations, i));
            }
            return true;
        }

        @Override
        protected Node createNodeForKey(RepresentationFormulaNode<T> key) {
            return key;
        }
    }
    // </editor-fold>
    // </editor-fold>
}
