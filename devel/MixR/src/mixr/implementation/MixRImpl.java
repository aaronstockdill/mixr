/*
 * File name: MixRImpl.java
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
package mixr.implementation;

import mixr.components.MixRDriver;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import mixr.FormulaFormatManager;
import mixr.GoalsManager;
import mixr.MixR;
import mixr.PresentationManager;
import mixr.ReasonersManager;
import org.openide.modules.OnStart;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ServiceProvider;

/**
 * This is the main hub of the MixR framework. Reasoners can register
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
@ServiceProvider(service = MixR.class)
@OnStart
public final class MixRImpl implements MixR, Runnable {

    private InstanceContent instanceContent;
    private Result<MixRDriver> lookupResult;
    private AbstractLookup componentsLookup;
    private Set<MixRDriver> components;
    private final ArrayList<ManagerInternals> managers = new ArrayList<>();
    private boolean initialised = false;
    final ReasonersManagerImpl reasonersManager = new ReasonersManagerImpl();
    final GoalsManagerImpl goalManager = new GoalsManagerImpl();
    final FormulaFormatManagerImpl formulaFormatManager = new FormulaFormatManagerImpl();
    final PresentationManagerImpl presentationManager = new PresentationManagerImpl();

    public MixRImpl() {
        initialise();
    }

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
    public Set<? extends MixRDriver> getRegisteredComponents() {
        return components;
    }

    @Override
    public PresentationManager getPresentationManager() {
        return presentationManager;
    }

    @Override
    public Lookup getLookup() {
        return componentsLookup;
    }

    @Override
    public String toString() {
        return "MixR is awesome!";
    }

    private void updateComponentsList() {
        components = new HashSet<>();
        Collection<? extends MixRDriver> comps = lookupResult.allInstances();
        for (MixRDriver comp : comps) {
            instanceContent.add(comp);
            components.add(comp);
        }
        components = Collections.unmodifiableSet(components);
    }

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
            Logger.getLogger(MixRImpl.class.getName()).log(Level.INFO, "MixR initialised.");
        } else {
            Logger.getLogger(MixRImpl.class.getName()).log(Level.SEVERE, "MixR is already initiallised.");
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

        // Find all MixR components and register them.
        lookupResult = Lookup.getDefault().lookupResult(MixRDriver.class);
//        lookupResult.addLookupListener(new LookupListener() {
//
//            @Override
//            public void resultChanged(LookupEvent ev) {
//                Logger.getLogger(MixRImpl.class.getName()).log(Level.INFO, "MixR initialised.");
//            }
//        });
    }
}
