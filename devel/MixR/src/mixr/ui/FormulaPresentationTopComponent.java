/*
 * File name: FormulaPresentationTopComponent.java
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
import mixr.components.FormulaPresenter;
import mixr.logic.CarrierFormulaFormat;
import mixr.logic.CarrierFormulaFormat.PlaceholderEmbeddingException;
import mixr.logic.Formula;
import mixr.logic.FormulaFormat;
import mixr.logic.FormulaRepresentation;
import mixr.logic.Goal;
import mixr.logic.Goals;
import mixr.ui.Bundle;
import mixr.ui.GoalsTopComponent.GeneralGoalNode;
import mixr.ui.presenters.SingleFormulaPresentationPanel;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Listens to the currently selected formula in the {@link
 * CurrentFormulaTopComponent} window and asks presenters to display it.
 */
@ConvertAsProperties(dtd = "-//mixr.ui//FormulaPresentation//EN",
autostore = false)
@TopComponent.Description(preferredID = FormulaPresentationTopComponent.PreferredId,
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "output", openAtStartup = true)
@ActionID(category = "Window", id = "mixr.ui.FormulaPresentationTopComponent")
@ActionReference(path = "Menu/Window/MixR", position = 300)
@TopComponent.OpenActionRegistration(displayName = "#CTL_FormulaPresentationAction",
preferredID = FormulaPresentationTopComponent.PreferredId)
@Messages({
    "CTL_FormulaPresentationAction=MixR Visualisation",
    "CTL_FormulaPresentationTopComponent=MixR Visualisation",
    "HINT_FormulaPresentationTopComponent=This window displays selected formulae in all supported formats.",
    "FPTC_CurrentFormulaTopComponent_notFound=Could not find the CurrentFormulaTopComponent. This is a bug and should never happen."
})
public final class FormulaPresentationTopComponent extends TopComponent {

    //<editor-fold defaultstate="collapsed" desc="Fields">
    public static final String PreferredId = "FormulaPresentationTopComponent";
    private static final long serialVersionUID = 0x5db50c513aa41536L;
    private final FormulaSelectionListener selectedFormulaListener = new FormulaSelectionListener();
    private GoalsChangedListener goalsChangedListener = new GoalsChangedListener();
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public FormulaPresentationTopComponent() {
        initComponents();
        setName(Bundle.CTL_FormulaPresentationTopComponent());
        setToolTipText(Bundle.HINT_FormulaPresentationTopComponent());

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

        scrlVisualisationsPanel = new javax.swing.JScrollPane();
        visualisationsPanel = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        visualisationsPanel.setLayout(new javax.swing.BoxLayout(visualisationsPanel, javax.swing.BoxLayout.Y_AXIS));
        scrlVisualisationsPanel.setViewportView(visualisationsPanel);

        add(scrlVisualisationsPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scrlVisualisationsPanel;
    private javax.swing.JPanel visualisationsPanel;
    // End of variables declaration//GEN-END:variables
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TopComponent Stuff">
    @Override
    public void componentOpened() {
        // Listen for selection changes in the GoalsTopComponent (when the user
        // clicks on a particular part of the goal):
        GoalsTopComponent goalsWindow = (GoalsTopComponent) WindowManager.getDefault().findTopComponent(GoalsTopComponent.PreferredID);
        if (goalsWindow != null) {
            goalsWindow.getExplorerManager().addPropertyChangeListener(selectedFormulaListener);
            updateFromSelectedIn(goalsWindow.getExplorerManager());
        } else {
            throw new IllegalStateException(Bundle.FPTC_CurrentFormulaTopComponent_notFound());
        }
        // Listen for goal changes in MixR's goal manager:
        GoalsManager goalManager = Lookup.getDefault().lookup(MixR.class).getGoalManager();
        goalManager.addPropertyChangeListener(goalsChangedListener, GoalsManager.CurrentGoalsChangedEvent);
    }

    @Override
    public void componentClosed() {
        CurrentFormulaTopComponent currentFormulaWindow = (CurrentFormulaTopComponent) WindowManager.getDefault().findTopComponent(CurrentFormulaTopComponent.PreferredID);
        if (currentFormulaWindow != null) {
            currentFormulaWindow.getExplorerManager().removePropertyChangeListener(selectedFormulaListener);
        } else {
            throw new IllegalStateException(Bundle.FPTC_CurrentFormulaTopComponent_notFound());
        }
        // Stop listening for goal changes in MixR's goal manager:
        GoalsManager goalManager = Lookup.getDefault().lookup(MixR.class).getGoalManager();
        goalManager.removePropertyChangeListener(goalsChangedListener, GoalsManager.CurrentGoalsChangedEvent);
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

    // <editor-fold defaultstate="collapsed" desc="Visualisation Methods">
    @Messages({
        "FPTC_visualiser_failed=The formula presenter '{0}' unexpectedly failed while visualising a formula of the format '{1}'."
    })
    private void addVisualisationsOf(GeneralGoalNode goalNode, Collection<FormulaFormat> inFormats) {
        if (goalNode != null && goalNode.getFormula() != null) {
            if (inFormats == null) {
                inFormats = getAllFormats();
            }
            Formula formula = goalNode.getFormula();
            addVisualisationsOf(formula, inFormats, goalNode, goalNode.getGoalIndex());
        }
    }

    private void addVisualisationsOf(Formula formula, Collection<FormulaFormat> inFormats, GeneralGoalNode goalNode, int goalIndex) {
        if (inFormats == null) {
            inFormats = getAllFormats();
        }
        // Go through every format, every representation, and every
        // visualiser and display all these combinations
        for (FormulaFormat formulaFormat : inFormats) {
            ArrayList<? extends FormulaRepresentation> reps = formula.fetchRepresentations(formulaFormat);
            if (reps != null && reps.size() > 0) {
                Set<FormulaPresenter> presenters = getPresentersFor(formulaFormat);
                if (presenters != null && presenters.size() > 0) {
                    for (FormulaRepresentation rep : reps) {
                        for (FormulaPresenter presenter : presenters) {
                            try {
                                Component visualiser = presenter.createVisualiserFor(rep);
                                addVisualisation(goalNode, goalIndex, formulaFormat, rep, presenter, visualiser);
                            } catch (FormulaPresenter.VisualisationException visEx) {
                                Logger.getLogger(FormulaPresentationTopComponent.class.getName()).log(Level.WARNING, Bundle.FPTC_visualiser_failed(presenter.getName(), formulaFormat.getPrettyName()), visEx);
                            }
                        }
                    }
                }
            }
        }
    }

    private void addVisualisation(GeneralGoalNode goalNode, int goalIndex, FormulaFormat formulaFormat, FormulaRepresentation rep, FormulaPresenter presenter, Component visualiser) {
        // Now put the panel onto this panel:
        SingleFormulaPresentationPanel pnl = new SingleFormulaPresentationPanel(goalNode, rep, goalIndex, -1, visualiser, presenter);
        visualisationsPanel.add(pnl);
    }

    private void clearVisualisations() {
        visualisationsPanel.removeAll();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Current Selection Update">
    private ArrayList<GeneralGoalNode> extractGoalNodes(Node[] nodes) {
        if (nodes == null || nodes.length == 0) {
            return null;
        } else {
            ArrayList<GeneralGoalNode> goalNodes = new ArrayList<>(nodes.length);
            for (int i = 0; i < nodes.length; i++) {
                Node node = nodes[i];
                if (node instanceof GeneralGoalNode) {
                    GeneralGoalNode goalNode = (GeneralGoalNode) node;
                    goalNodes.add(goalNode);
                }
            }
            return goalNodes;
        }
    }

    private void updateFromSelectedIn(ExplorerManager em) {
        Node[] nodes = em.getSelectedNodes();
        if (nodes == null || nodes.length < 1) {
            updateFromAllIn(em);
        } else {
            updatePresented(extractGoalNodes(nodes));
        }
    }

    private void updateFromAllIn(ExplorerManager em) {
        Node[] nodes = em.getRootContext().getChildren().getNodes(true);
        updatePresented(extractGoalNodes(nodes));
    }

    /**
     * This method uses the explorer nodes from the GoalsTopComponent to show
     * only the user-selected formulae instead of using MixR formulae.
     *
     * @param formulae
     */
    private void updatePresented(ArrayList<GeneralGoalNode> formulae) {
        clearVisualisations();
        if (formulae != null && formulae.size() > 0) {
            Collection<FormulaFormat> allFormats = getAllFormats();
            for (GeneralGoalNode formula : formulae) {
                addVisualisationsOf(formula, allFormats);
            }
        }
        validate();
        // NOTE: We have to repaint if nothing has been added onto the panel.
        // I guess `validate` does nothing if the panel has been just emptied.
        repaint();
    }

    private void presentGoals(List<Goal> goals) {
        clearVisualisations();
        if (goals != null && goals.size() > 0) {
            Collection<FormulaFormat> allFormats = getAllFormats();
            for (int i = 0; i < goals.size(); i++) {
                Goal goal = goals.get(i);
                addVisualisationsOf(goal.asFormula(), allFormats, null, i);
            }
        }
        validate();
        // NOTE: We have to repaint if nothing has been added onto the panel.
        // I guess `validate` does nothing if the panel has been just emptied.
        repaint();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Selected Formula Changed Listener">
    private class FormulaSelectionListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            switch (evt.getPropertyName()) {
                case ExplorerManager.PROP_SELECTED_NODES: {
                    ExplorerManager em = (ExplorerManager) evt.getSource();
                    updateFromSelectedIn(em);
                    break;
                }
//                case ExplorerManager.PROP_ROOT_CONTEXT: {
//                    ExplorerManager em = (ExplorerManager) evt.getSource();
//                    updateFromAllIn(em);
//                    break;
//                }
            }
        }
    }

    private class GoalsChangedListener implements PropertyChangeListener {

        public GoalsChangedListener() {
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            // Goals have changed. Update them:
            Goals currentGoals = Lookup.getDefault().lookup(MixR.class).getGoalManager().getCurrentGoals();
            if (currentGoals != null) {
                presentGoals(currentGoals.getGoals());
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Private Helper Methods">
    private static Set<FormulaPresenter> getAllPresenters() {
        return Lookup.getDefault().lookup(MixR.class).getPresentationManager().getPresenters();
    }

    private Collection<FormulaFormat> getAllFormats() {
        return Lookup.getDefault().lookup(MixR.class).getFormulaFormatManager().getFormulaFormats();
    }

    private Set<FormulaPresenter> getPresentersFor(FormulaFormat format) {
        return Lookup.getDefault().lookup(MixR.class).getPresentationManager().getPresenters(format);
    }
    // </editor-fold>
}
