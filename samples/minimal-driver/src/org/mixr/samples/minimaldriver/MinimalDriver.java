package org.mixr.samples.minimaldriver;

import mixr.components.MixRDriver;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = MixRDriver.class)
public class MinimalDriver implements MixRDriver {

    @Override
    public String getName() {
        return "Minimal MixR Driver";
    }

}
