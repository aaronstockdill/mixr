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
package diabelli.ui;

import diabelli.Diabelli;
import diabelli.FormulaFormatManager;
import diabelli.components.FormulaPresenter;
import diabelli.logic.Formula;
import diabelli.logic.FormulaFormat;
import diabelli.logic.FormulaRepresentation;
import diabelli.logic.Goal;
import diabelli.ui.GoalsTopComponent.ConclusionNode;
import diabelli.ui.GoalsTopComponent.GeneralGoalNode;
import diabelli.ui.GoalsTopComponent.PremiseNode;
import diabelli.ui.GoalsTopComponent.PremisesNode;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.tree.TreeSelectionModel;
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
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * This window gives the user the option to select particular representations
 * (formats) of a formula. This selection may then consequently be displayed
 * with the help of {@link FormulaPresenter formula presenters}. Additionally,
 * the user may request additional translations through this window.
 */
@ConvertAsProperties(dtd = "-//diabelli.ui//CurrentFormula//EN",
autostore = false)
@TopComponent.Description(preferredID = CurrentFormulaTopComponent.PreferredID,
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "navigator", openAtStartup = true)
@ActionID(category = "Window", id = "diabelli.ui.CurrentFormulaTopComponent")
@ActionReference(path = "Menu/Window/Diabelli", position = 200)
@TopComponent.OpenActionRegistration(displayName = "#CTL_CurrentFormulaAction",
preferredID = CurrentFormulaTopComponent.PreferredID)
@Messages({
    "CTL_CurrentFormulaAction=Current Formula",
    "CTL_CurrentFormulaTopComponent=Current Formula",
    "HINT_CurrentFormulaTopComponent=Shows details for the currently selected formula in the 'Diabelli Goals' window.",
    "CFTC_root_node_display_name=Formula formats list:"
})
public final class CurrentFormulaTopComponent extends TopComponent implements ExplorerManager.Provider {

    //<editor-fold defaultstate="collapsed" desc="Fields">
    public static final String PreferredID = "CurrentFormulaTopComponent";
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
            if ("selectedNodes".equals(evt.getPropertyName())) {
                ExplorerManager em = (ExplorerManager) evt.getSource();
                updateSelectionFrom(em);
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GUI Update Methods">
    private void showPremises(PremisesNode premisesNode) {
        Logger.getLogger(CurrentFormulaTopComponent.class.getName()).log(Level.INFO, "Showing whole premises not yet supported.");
    }

    private static class WrapperChildFactory<T extends Node> extends ChildFactory<T> {

        private final T node;

        public WrapperChildFactory(T node) {
            this.node = node;
        }

        @Override
        protected boolean createKeys(List<T> toPopulate) {
            toPopulate.add(node);
            return true;
        }

        @Override
        protected Node createNodeForKey(T key) {
            return key;
        }
    }

    private void resetRootContextTitle() {
        this.em.getRootContext().setDisplayName(Bundle.CFTC_root_node_display_name());
    }

    private void resetRootNode(final Node rootNode) {
        this.em.setRootContext(rootNode);
        resetRootContextTitle();
    }

    private <T extends Node> void wrapAndSetRootNode(final T aNode) {
        resetRootNode(new AbstractNode(Children.create(new WrapperChildFactory<>(aNode), false)));
    }

    private void showConclusion(ConclusionNode conclusionNode) {
        wrapAndSetRootNode(new CurrentConclusionNode(conclusionNode));
    }

    private void showPremise(PremiseNode premiseNode) {
        wrapAndSetRootNode(new CurrentPremiseNode(premiseNode));
    }

    private void showGoal(GeneralGoalNode generalGoalNode) {
        wrapAndSetRootNode(new CurrentGoalNode(generalGoalNode));
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
            updateSelection((GeneralGoalNode) selectedNodes[0]);
        } else {
            updateSelection(null);
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Explorer Nodes">
    /**
     * Nodes of this type provide exact information on what formula (and in what
     * type) the user wants Diabelli to focus on.
     *
     * <p>An example of a selection: the user selects a particular node in {@link GoalsTopComponent the
     * window that lists the current goals}. Immediately afterwards, {@link CurrentFormulaTopComponent the current selection window}
     * displays all the formats of the user's selection. Initially, the main
     * representation is selected, but the user can then switch between
     * representations by selecting a particular node in the <span
     * style="font-style:italic;">current selection window</span>.</p>
     *
     * @param <T> The type of the node as selected in the {@link GoalsTopComponent current Diabelli goals window}.
     */
    public abstract static class CurrentGoalSelectionNode<T extends GeneralGoalNode> extends AbstractNode {

        protected final T goal;

        CurrentGoalSelectionNode(T goal, Children children) {
            super(children);
            this.goal = goal;
        }

        /**
         * Returns the selected formula that this node represents.
         *
         * <p>The default implementation calls {@link CurrentGoalSelectionNode#getSelectedGoal()}
         * and returns its {@link Goal#asFormula() formula}.</p>
         *
         * @return the selected formula that this node represents.
         */
        public Formula<?> getSelectedFormula() {
            return getSelectedGoal().asFormula();
        }

        /**
         * Returns the selected representation of the selected formula.
         *
         * <p>The default implementation calls {@link CurrentGoalSelectionNode#getSelectedFormula()}
         * and returns its main representation.</p>
         *
         * @return the selected representation of the selected formula.
         */
        public FormulaRepresentation<?> getSelectedFormulaRepresentation() {
            return getSelectedFormula().getMainRepresentation();
        }

        /**
         * Returns the selected goal that this node represents.
         *
         * @return the selected goal that this node represents.
         */
        public final Goal getSelectedGoal() {
            return goal.goal;
        }

        /**
         * Returns the index of the selected goal that this node represents.
         *
         * @return the index of the selected goal that this node represents.
         */
        public final int getSelectedGoalIndex() {
            return goal.goalIndex;
        }
    }

    @Messages({
        "CurrentPremiseNode_display_name=Premise {0} of goal {1}"
    })
    public static class CurrentPremiseNode extends CurrentGoalSelectionNode<PremiseNode> {

        CurrentPremiseNode(PremiseNode premise) {
            super(premise, Children.LEAF);
            setDisplayName(Bundle.CurrentPremiseNode_display_name(premise.premiseIndex + 1, premise.goalIndex + 1));
            setChildren(Children.create(new FormulaFormatsChildren<>(this), false));
        }

        @Override
        public Formula<?> getSelectedFormula() {
            return goal.goal.getPremiseAt(goal.premiseIndex);
        }
    }

    @Messages({
        "CurrentConclusionNode_display_name=Conclusion of goal {0}"
    })
    public static class CurrentConclusionNode extends CurrentGoalSelectionNode<ConclusionNode> {

        CurrentConclusionNode(ConclusionNode conclusion) {
            super(conclusion, Children.LEAF);
            setDisplayName(Bundle.CurrentConclusionNode_display_name(conclusion.goalIndex + 1));
            setChildren(Children.create(new FormulaFormatsChildren<>(this), false));
        }

        @Override
        public Formula<?> getSelectedFormula() {
            return goal.goal.getConclusion();
        }
    }

    @Messages({
        "CurrentGoalNode_display_name=The entire goal {0}"
    })
    public static class CurrentGoalNode extends CurrentGoalSelectionNode<GeneralGoalNode> {

        CurrentGoalNode(GeneralGoalNode goal) {
            super(goal, Children.LEAF);
            setDisplayName(Bundle.CurrentGoalNode_display_name(goal.goalIndex + 1));
            setChildren(Children.create(new FormulaFormatsChildren<>(this), false));
        }
    }

    static class CurrentGoalSelectionDelegateNode<T extends GeneralGoalNode> extends CurrentGoalSelectionNode<T> {

        private final CurrentGoalSelectionNode<T> delegate;

        CurrentGoalSelectionDelegateNode(CurrentGoalSelectionNode<T> selection, Children children) {
            super(selection.goal, children);
            this.delegate = selection;
        }

        @Override
        public Formula<?> getSelectedFormula() {
            return delegate.getSelectedFormula();
        }

        @Override
        public FormulaRepresentation<?> getSelectedFormulaRepresentation() {
            return delegate.getSelectedFormulaRepresentation();
        }
    }

    public static class FormatNode<T extends GeneralGoalNode> extends CurrentGoalSelectionDelegateNode<T> {

        final FormulaFormat<?> toFormat;

        @Messages({
            "FormatNode_displayName=Format: {0}"
        })
        FormatNode(CurrentGoalSelectionNode<T> selection, FormulaFormat<?> toFormat) {
            super(selection, Children.LEAF);
            this.toFormat = toFormat;
            setDisplayName(Bundle.FormatNode_displayName(toFormat.getPrettyName()));
            setChildren(Children.create(new FormulaRepresentationsChildren<>(this), true));
        }

        @Override
        public FormulaRepresentation<?> getSelectedFormulaRepresentation() {
            FormulaRepresentation<?>[] representations = getSelectedFormula().fetchRepresentations(toFormat);
            return representations == null || representations.length == 0 ? null : representations[0];
        }
    }

    public static class FormulaRepresentationNode<T extends GeneralGoalNode> extends CurrentGoalSelectionDelegateNode<T> {

        final FormulaRepresentation[] representations;
        final int representationIndex;

        @Messages({
            "FormatRepresentationNode_displayName=Translation {0}",
            "FormatRepresentationNode_displayName_main_representation=Main representation"
        })
        FormulaRepresentationNode(FormatNode<T> selection, FormulaRepresentation[] representations, int representationIndex) {
            super(selection, Children.LEAF);
            this.representations = representations;
            this.representationIndex = representationIndex;
            if (selection.getSelectedFormula().getMainRepresentation() == representations[representationIndex]) {
                setDisplayName(Bundle.FormatRepresentationNode_displayName_main_representation());
            } else {
                setDisplayName(Bundle.FormatRepresentationNode_displayName(representationIndex + 1));
            }
        }

        @Override
        public FormulaRepresentation<?> getSelectedFormulaRepresentation() {
            return representations[representationIndex];
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Formula Format Node Factories">
    private static class FormulaFormatsChildren<T extends GeneralGoalNode> extends ChildFactory<FormatNode<T>> {

        private final CurrentGoalSelectionNode<T> source;

        private FormulaFormatsChildren(CurrentGoalSelectionNode<T> source) {
            this.source = source;
        }

        @Override
        protected boolean createKeys(List<FormatNode<T>> toPopulate) {
            // Go through all known formats and try to translate the selected
            // formula into all the formats.
            FormulaFormatManager formatManager = Lookup.getDefault().lookup(Diabelli.class).getFormulaFormatManager();
            Collection<FormulaFormat<?>> formats = formatManager.getFormulaFormats();
            for (FormulaFormat<?> format : formats) {
                toPopulate.add(new FormatNode<>(source, format));
            }
            return true;
        }

        @Override
        protected Node createNodeForKey(FormatNode<T> formatNode) {
            return formatNode;
        }
    }

    private static class FormulaRepresentationsChildren<T extends GeneralGoalNode> extends ChildFactory<FormulaRepresentationNode<T>> {

        private final FormatNode<T> source;

        FormulaRepresentationsChildren(FormatNode<T> source) {
            this.source = source;
        }

        @Override
        protected boolean createKeys(List<FormulaRepresentationNode<T>> toPopulate) {
            // Go through all known formats and try to translate the selected
            // formula into all the formats.
            FormulaRepresentation[] representations = source.getSelectedFormula().fetchRepresentations(source.toFormat);
            if (representations != null && representations.length != 0) {
                for (int i = 0; i < representations.length; i++) {
                    toPopulate.add(new FormulaRepresentationNode<>(source, representations, i));
                }
            }
            return true;
        }

        @Override
        protected Node createNodeForKey(FormulaRepresentationNode<T> key) {
            return key;
        }
    }
    // </editor-fold>
    // </editor-fold>
}
