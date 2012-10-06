/*
 * File name: SpeedithInferenceRuleDescriptor.java
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
package speedith.diabelli.logic;

import diabelli.components.GoalTransformer;
import diabelli.logic.InferenceRuleDescriptor;
import speedith.core.reasoning.InferenceRuleProvider;
import speedith.core.reasoning.args.RuleArg;
import speedith.diabelli.SpeedithDriver;

/**
 * Provides inference rule information to Diabelli.
 */
public class SpeedithInferenceRuleDescriptor implements InferenceRuleDescriptor {

    private final InferenceRuleProvider<? extends RuleArg> infRuleProvider;
    private final SpeedithDriver owner;

    public SpeedithInferenceRuleDescriptor(SpeedithDriver owner, InferenceRuleProvider<? extends RuleArg> infRuleProvider) {
        this.infRuleProvider = infRuleProvider;
        this.owner = owner;
    }

    @Override
    public String getName() {
        return infRuleProvider.getPrettyName();
    }

    @Override
    public String getDescription() {
        return infRuleProvider.getDescription();
    }

    /**
     * A reference to the actual Speedith's inference rule.
     *
     * @return the Speedith inference rule.
     */
    public InferenceRuleProvider<? extends RuleArg> getInfRuleProvider() {
        return infRuleProvider;
    }

    @Override
    public GoalTransformer getOwner() {
        return owner;
    }

    @Override
    public boolean isFullyAutomated() {
        return false;
    }
}
