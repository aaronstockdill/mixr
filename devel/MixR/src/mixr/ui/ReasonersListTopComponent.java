/*
 * File name: ReasonersListTopComponent.java
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import mixr.MixR;
import mixr.components.MixRComponent;
import mixr.components.Reasoner;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.ListView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.Lookups;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//mixr.ui//ReasonersList//EN",
autostore = false)
@TopComponent.Description(preferredID = "ReasonersListTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "navigator", openAtStartup = true)
@ActionID(category = "Window", id = "mixr.ui.ReasonersListTopComponent")
@ActionReference(path = "Menu/Window/MixR", position = 2000)
@TopComponent.OpenActionRegistration(displayName = "#CTL_ReasonersListAction",
preferredID = "ReasonersListTopComponent")
@Messages({
    "CTL_ReasonersListAction=MixR Drivers",
    "CTL_ReasonersListTopComponent=MixR Drivers",
    "HINT_ReasonersListTopComponent=This is a list of all MixR drivers (reasoners, presenters, etc.)."
})
public final class ReasonersListTopComponent extends TopComponent implements ExplorerManager.Provider {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private ExplorerManager em;
    private Lookup lookup;
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    public ReasonersListTopComponent() {
        initComponents();
        setName(Bundle.CTL_ReasonersListTopComponent());
        setToolTipText(Bundle.HINT_ReasonersListTopComponent());


        this.em = new ExplorerManager();
        ActionMap map = this.getActionMap();
        InputMap keys = this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        this.lookup = ExplorerUtils.createLookup(this.em, map);
        this.associateLookup(this.lookup);


        Children children = new ComponentsRootNode();
        Node root = new AbstractNode(children);
        this.em.setRootContext(root);
        this.em.getRootContext().setDisplayName("MixRReasonersList");

        // The list of reasoners does not need to display icons.
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

        reasonersList = new ListView();

        setLayout(new java.awt.BorderLayout());
        add(reasonersList, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane reasonersList;
    // End of variables declaration//GEN-END:variables
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Overrides">
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Handling">
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

    // <editor-fold defaultstate="collapsed" desc="Explorer Nodes">
    private static class ReasonersNode extends ComponentNode {

        public ReasonersNode(Reasoner reasoner) {
            super(reasoner);
        }
    }

    private static class ComponentNode extends AbstractNode implements Comparable<ComponentNode> {

        private MixRComponent component;

        public MixRComponent getComponent() {
            return component;
        }

        @Messages({
            "ComponentNode_component_null=The component must not be null."
        })
        public ComponentNode(MixRComponent component) {
            super(Children.LEAF, Lookups.singleton(component));
            if (component == null) {
                throw new IllegalArgumentException(Bundle.ComponentNode_component_null());
            }
            this.component = component;
            setName(component.toString());
            setDisplayName(component.getName());
        }

        @Override
        public int compareTo(ComponentNode o) {
            return getComponent().getName().compareToIgnoreCase(o.getComponent().getName());
        }
    }

    private static class ComponentsRootNode extends Children.Array {

        private Lookup.Result<MixRComponent> reasonersLookupResult;

        public ComponentsRootNode() {
            MixR mixr = Lookup.getDefault().lookup(MixR.class);
            reasonersLookupResult = mixr.getLookup().lookupResult(MixRComponent.class);
            reasonersLookupResult.addLookupListener(new LookupListener() {
                @Override
                public void resultChanged(LookupEvent ev) {
                    updateReasonersList();
                }
            });
        }

        @Override
        protected Collection<Node> initCollection() {
            Collection<? extends MixRComponent> allReasoners = reasonersLookupResult.allInstances();

            ArrayList<Node> reasonerNodes = new ArrayList<>();
            if (allReasoners != null) {
                for (MixRComponent reasoner : allReasoners) {
                    if (reasoner instanceof Reasoner) {
                        Reasoner r = (Reasoner) reasoner;
                        reasonerNodes.add(new ReasonersNode(r));
                    } else {
                        reasonerNodes.add(new ComponentNode(reasoner));
                    }
                }
            }
            Collections.sort(reasonerNodes, new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    return ((ComponentNode) o1).compareTo((ComponentNode) o2);
                }
            });
            return reasonerNodes;
        }

        private void updateReasonersList() {
            remove(getNodes());
            add(initCollection().toArray(new ReasonersNode[0]));
        }
    }
    // </editor-fold>
}