/*
 * File name: NatLang.java
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
package mixr.demo.driver.natlang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mixr.components.FormulaFormatsProvider;
import mixr.components.FormulaPresenter;
import mixr.components.GoalTransformer;
import mixr.components.MixRDriver;
import mixr.logic.FormulaFormat;
import mixr.logic.FormulaRepresentation;
import mixr.logic.InferenceRule;
import mixr.logic.InferenceRuleDescriptor;
import mixr.logic.InferenceStepResult;
import mixr.logic.InferenceTargets;
import org.openide.util.lookup.ServiceProvider;

/**
 * A demonstration driver which provides support for the <span
 * style="font-style:italic;">NatLang</span> language.
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
@ServiceProvider(service = MixRDriver.class)
public class NatLang implements MixRDriver, FormulaFormatsProvider, FormulaPresenter, GoalTransformer {

    private final List<InferenceRuleDescriptor> inferenceRules;

    public NatLang() {
        ArrayList<InferenceRuleDescriptor> tmp = new ArrayList<>();

        tmp.add(new DummyPlaceholderInference(NatLang.this));
        tmp.add(new DummyIsabelleInference(NatLang.this));

        inferenceRules = Collections.unmodifiableList(tmp);
    }

    @Override
    public String getName() {
        return "NatLang";
    }

    @Override
    public Collection<FormulaFormat> getFormulaFormats() {
        return FormulaFormatsContainer.FormulaFormats;
    }

    @Override
    public Set<FormulaFormat> getPresentedFormats() {
        return FormulaFormatsContainer.FormulaFormats;
    }

    public boolean canPresent(FormulaFormat format) {
        return NatLangFormat.getInstance() == format;
    }

    @Override
    public NatLangPresenter createVisualiserFor(FormulaRepresentation formula) throws VisualisationException {
        if (formula.getFormula() instanceof String) {
            return new NatLangPresenter((String) formula.getFormula());
        } else {
            return null;
        }
    }

    @Override
    public Collection<InferenceRuleDescriptor> getApplicableInferenceRules(InferenceTargets target) {
        return getInferenceRules();
    }

    @Override
    public Collection<InferenceRuleDescriptor> getInferenceRules() {
        return inferenceRules;
    }

    @Override
    public boolean canTransform(InferenceTargets target) {
        return true;
    }

    @Override
    public void applyInferenceRule(InferenceTargets targets, InferenceRuleDescriptor inferenceRule) {
        if (inferenceRule instanceof InferenceRule) {
            InferenceRule dummyPlaceholderInference = (InferenceRule) inferenceRule;
            dummyPlaceholderInference.applyInferenceRule(targets);
        }
    }

    @Override
    public InferenceStepResult applyAutomatedInferenceRule(InferenceTargets targets, InferenceRuleDescriptor inferenceRule) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getInferenceSetName() {
        return "NatLang";
    }

    private static class FormulaFormatsContainer {

        private static final Set<FormulaFormat> FormulaFormats;

        static {
            HashSet<FormulaFormat> tmp = new HashSet<>();
            tmp.add(NatLangFormat.getInstance());
            FormulaFormats = Collections.unmodifiableSet(tmp);
        }
    }
}
