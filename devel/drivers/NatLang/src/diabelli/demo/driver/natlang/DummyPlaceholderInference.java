/*
 * File name: DummyPlaceholderInference.java
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
package diabelli.demo.driver.natlang;

import diabelli.components.GoalTransformer;
import diabelli.logic.InferenceRule;
import diabelli.logic.InferenceRuleDescriptor;
import diabelli.logic.InferenceTarget;
import org.openide.util.NbBundle;

/**
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public class DummyPlaceholderInference implements InferenceRuleDescriptor, InferenceRule {
    
    private final GoalTransformer owner;

    DummyPlaceholderInference(GoalTransformer owner) {
        this.owner = owner;
    }

    @Override
    @NbBundle.Messages({
        "DPI_name=Infer new placeholder"
    })
    public String getName() {
        return Bundle.DPI_name();
    }

    @Override
    @NbBundle.Messages({
        "DPI_description=The user types in a new natural language sentence, which must be entailed by the original sentence."
    })
    public String getDescription() {
        return Bundle.DPI_description();
    }

    @Override
    public boolean isFullyAutomated() {
        return false;
    }

    @Override
    public GoalTransformer getOwner() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void applyInferenceRule(InferenceTarget targets) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
