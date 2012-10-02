/*
 * File name: SingleFormulaPresentationPanel.java
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
package diabelli.ui.presenters;

import diabelli.components.FormulaPresenter;
import diabelli.logic.FormulaRepresentation;
import diabelli.ui.GoalsTopComponent.ConclusionNode;
import diabelli.ui.GoalsTopComponent.GeneralGoalNode;
import diabelli.ui.GoalsTopComponent.PremiseNode;
import diabelli.ui.GoalsTopComponent.PremisesNode;
import java.awt.BorderLayout;
import java.awt.Component;
import org.openide.util.NbBundle;

/**
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public class SingleFormulaPresentationPanel extends javax.swing.JPanel {

    //<editor-fold defaultstate="collapsed" desc="Fields">
    private static final String BoldItalicEndTag = "</i></b>";
    private static final String BoldItalicStartTag = "<b><i>";
    private static final long serialVersionUID = 0x1906271867d69be1L;
    private final GeneralGoalNode presentedNode;
    private final FormulaRepresentation<?> formula;
    private final int goalIndex;
    private final int representationIndex;
    private final Component presenterPanel;
    private final FormulaPresenter presenter;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * Creates new form SingleFormulaPresentationPanel
     */
    public SingleFormulaPresentationPanel(GeneralGoalNode presentedNode, FormulaRepresentation<?> formula, int goalIndex, int representationIndex, Component presenterPanel, FormulaPresenter presenter) {
        this.presentedNode = presentedNode;
        this.formula = formula;
        this.goalIndex = goalIndex;
        this.representationIndex = representationIndex;
        this.presenterPanel = presenterPanel;
        this.presenter = presenter;
        initComponents();
        if (presenterPanel != null) {
            this.add(presenterPanel, BorderLayout.CENTER);
        }
    }

    public SingleFormulaPresentationPanel() {
        this(null, null, 0, -1, null, null);
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        titlePanel = new javax.swing.JPanel();
        detailLabel = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        titlePanel.setBackground(new java.awt.Color(213, 237, 246));
        titlePanel.setMinimumSize(new java.awt.Dimension(100, 25));
        titlePanel.setPreferredSize(new java.awt.Dimension(100, 25));

        detailLabel.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        detailLabel.setText(this.buildDetailLabel());

        javax.swing.GroupLayout titlePanelLayout = new javax.swing.GroupLayout(titlePanel);
        titlePanel.setLayout(titlePanelLayout);
        titlePanelLayout.setHorizontalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, titlePanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(detailLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );
        titlePanelLayout.setVerticalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(detailLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        add(titlePanel, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel detailLabel;
    private javax.swing.JPanel titlePanel;
    // End of variables declaration//GEN-END:variables
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Private UI Refresh Methods">
    @NbBundle.Messages({
        "Visualisation.DetailLabel.goal=Goal {0}",
        "Visualisation.DetailLabel.premise=Premise {0}",
        "Visualisation.DetailLabel.premises=Premises",
        "Visualisation.DetailLabel.conclusion=Conclusion",
        "Visualisation.DetailLabel.format=Format: <b><i>{0}</i></b>",
        "Visualisation.DetailLabel.presenter=Visualiser: <b><i>{0}</i></b>",
        "Visualisation.DetailLabel.nothing=Nothing is being visualised..."
    })
    private String buildDetailLabel() {
        if (formula != null && presenter != null) {
            StringBuilder sb = new StringBuilder("<html>");

            // Print something like this: Goal 1 > Premise 2  Format: <format>  Visualiser: <presenter>

            // First print out the selected goal:
            sb.append(BoldItalicStartTag).append(Bundle.Visualisation_DetailLabel_goal(goalIndex + 1)).append(BoldItalicEndTag);

            if (presentedNode instanceof PremiseNode) {
                PremiseNode premiseFormulaNode = (PremiseNode) presentedNode;
                sb.append(" > ").append(BoldItalicStartTag).append(Bundle.Visualisation_DetailLabel_premise(premiseFormulaNode.getPremiseIndex() + 1)).append(BoldItalicEndTag);
            } else if (presentedNode instanceof PremisesNode) {
                sb.append(" > ").append(BoldItalicStartTag).append(Bundle.Visualisation_DetailLabel_premises()).append(BoldItalicEndTag);
            } else if (presentedNode instanceof ConclusionNode) {
                sb.append(" > ").append(BoldItalicStartTag).append(Bundle.Visualisation_DetailLabel_conclusion()).append(BoldItalicEndTag);
            }

            if (formula != null) {
                // Which format are we displaying?
                addSpaces(sb).append(Bundle.Visualisation_DetailLabel_format(formula.getFormat().getPrettyName()));
            }

            if (presenter != null) {
                // Who is visualising the stuff?
                addSpaces(sb).append(Bundle.Visualisation_DetailLabel_presenter(presenter.getName()));
            }

            sb.append("</html>");
            return sb.toString();
        } else {
            return Bundle.Visualisation_DetailLabel_nothing();
        }
    }

    StringBuilder addSpaces(StringBuilder sb) {
        return (sb.length() > 0) ? sb.append("&nbsp;&nbsp;") : sb;
    }
    // </editor-fold>
}
