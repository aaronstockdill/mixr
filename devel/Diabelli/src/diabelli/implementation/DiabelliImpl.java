/*
 * File name: DiabelliImpl.java
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
package diabelli.implementation;

import diabelli.*;
import diabelli.components.DiabelliComponent;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.modules.OnStart;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ServiceProvider;

/**
 * This is the main hub of the Diabelli framework. Reasoners can register
 * themselves here, currently opened standalone reasoners (the ones that host a
 * proof) can be found here, supporting reasoners (the ones which can apply
 * inference steps on goals or prove them outright, display goals, provide
 * formula input mechanisms etc.), currently pending goals in a proof of a
 * standalone reasoner etc.
 *
 * <p><span style="font-weight:bold">Note</span>: This class is a singleton,
 * access its only instance through the lookup API.</p>
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
@ServiceProvider(service = Diabelli.class)
@OnStart
public final class DiabelliImpl implements Diabelli, Runnable {

    // <editor-fold defaultstate="collapsed" desc="Private Fields">
    private InstanceContent instanceContent;
    private Result<DiabelliComponent> lookupResult;
    private AbstractLookup componentsLookup;
    private Set<DiabelliComponent> components;
    private final ArrayList<ManagerInternals> managers = new ArrayList<>();
    private boolean initialised = false;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Managers Fields">
    final ReasonersManagerImpl reasonersManager = new ReasonersManagerImpl();
    final GoalsManagerImpl goalManager = new GoalsManagerImpl();
    final FormulaFormatManagerImpl formulaFormatManager = new FormulaFormatManagerImpl();
    final PresentationManagerImpl presentationManager = new PresentationManagerImpl();
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public DiabelliImpl() {
        initialise();
    }

    // <editor-fold defaultstate="collapsed" desc="Diabelli Interface Implementation">
    @Override
    public GoalsManager getGoalManager() {
        return goalManager;
    }

    @Override
    public ReasonersManager getReasonersManager() {
        return reasonersManager;
    }

    @Override
    public FormulaFormatManager getFormulaFormatManager() {
        return formulaFormatManager;
    }

    @Override
    public Set<? extends DiabelliComponent> getRegisteredComponents() {
        return components;
    }

    @Override
    public PresentationManager getPresentationManager() {
        return presentationManager;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Lookup Provider Implementation">
    @Override
    public Lookup getLookup() {
        return componentsLookup;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Overrides">
    @Override
    public String toString() {
        return "Diabelli is awesome!";
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Components Registration">
    private void updateComponentsList() {
        components = new HashSet<>();
        Collection<? extends DiabelliComponent> comps = lookupResult.allInstances();
        for (DiabelliComponent comp : comps) {
            instanceContent.add(comp);
            components.add(comp);
        }
        components = Collections.unmodifiableSet(components);
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Diabelli Startup">
    @Override
    public void run() {
        if (!initialised) {
            // Set the build number:
            System.setProperty("netbeans.buildnumber", "1.0.5"); 
            
            updateComponentsList();

            // Now call the final stage in the initialisation of managers:
            for (ManagerInternals manager : managers) {
                manager.onAfterComponentsLoaded();
            }

            initialised = true;
            Logger.getLogger(DiabelliImpl.class.getName()).log(Level.INFO, "Diabelli initialised.");
        } else {
            Logger.getLogger(DiabelliImpl.class.getName()).log(Level.SEVERE, "Diabelli is already initiallised.");
        }
    }

    private void initialise() {
        // Initialise all managers:
        managers.add(reasonersManager);
        managers.add(goalManager);
        managers.add(formulaFormatManager);
        managers.add(presentationManager);

        // First create an empty list of components (then wait for them to
        // register or deregister).
        instanceContent = new InstanceContent();
        componentsLookup = new AbstractLookup(instanceContent);

        // Initialise the particular managers:
        for (ManagerInternals manager : managers) {
            manager.initialise(this);
        }

        // Find all Diabelli components and register them.
        lookupResult = Lookup.getDefault().lookupResult(DiabelliComponent.class);
//        lookupResult.addLookupListener(new LookupListener() {
//
//            @Override
//            public void resultChanged(LookupEvent ev) {
//                Logger.getLogger(DiabelliImpl.class.getName()).log(Level.INFO, "Diabelli initialised.");
//            }
//        });
    }
    //</editor-fold>
}
