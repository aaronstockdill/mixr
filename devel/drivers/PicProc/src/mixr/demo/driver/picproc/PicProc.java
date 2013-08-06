/*
 * File name: PicProc.java
 *    Author: Matej Urbas [matej.urbas@gmail.com]
 * 
 *  Copyright © 2013 Matej Urbas
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
package mixr.demo.driver.picproc;

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
import mixr.logic.AutomatedInferenceRule;
import mixr.logic.FormulaFormat;
import mixr.logic.FormulaRepresentation;
import mixr.logic.InferenceRule;
import mixr.logic.InferenceRuleDescriptor;
import mixr.logic.InferenceStepResult;
import mixr.logic.InferenceTargets;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 * A demonstration driver which enables integration of images into the MixR
 * framework. Images are inserted as local filesystem hyperlinks into master
 * reasoners' formulae via placeholders.
 *
 * <p>This driver implements mock reasoning about bitmap images. It extracts
 * fake information from images. The purpose of this is to indicate how proper
 * image processing could be integrated into the MixR framework.</p>
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
@ServiceProvider(service = MixRDriver.class)
public class PicProc implements MixRDriver, FormulaFormatsProvider, FormulaPresenter, GoalTransformer {

    private final List<InferenceRuleDescriptor> inferenceRules;

    public PicProc() {
        ArrayList<InferenceRuleDescriptor> tmp = new ArrayList<>();

        tmp.add(new ShapeInference(this));
        tmp.add(new AreaInference(this));

        inferenceRules = Collections.unmodifiableList(tmp);
    }

    @Override
    public String getName() {
        return "PicProc";
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
        return ImageUrlFormat.getInstance() == format;
    }

    @Override
    public ImagePresenter createVisualiserFor(FormulaRepresentation formula) throws VisualisationException {
        if (formula.getFormula() instanceof ImageUrlFormula) {
            ImageUrlFormula imageUrlFormula = (ImageUrlFormula) formula.getFormula();
            return new ImagePresenter(imageUrlFormula);
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
            InferenceRule inf = (InferenceRule) inferenceRule;
            inf.applyInferenceRule(targets);
        }
    }

    @Override
    @NbBundle.Messages({
        "PP_not_an_auto_rule=Could not apply the rule automatically. The given rule is not automated."
    })
    public InferenceStepResult applyAutomatedInferenceRule(InferenceTargets targets, InferenceRuleDescriptor inferenceRule) {
        if (inferenceRule instanceof AutomatedInferenceRule) {
            AutomatedInferenceRule inf = (AutomatedInferenceRule) inferenceRule;
            return inf.applyAutomatedInferenceRule(targets);
        } else {
            throw new IllegalArgumentException(Bundle.PP_not_an_auto_rule());
        }
    }

    @Override
    public String getInferenceSetName() {
        return "PicProc";
    }

    private static class FormulaFormatsContainer {

        private static final Set<FormulaFormat> FormulaFormats;

        static {
            HashSet<FormulaFormat> tmp = new HashSet<>();
            tmp.add(ImageUrlFormat.getInstance());
            FormulaFormats = Collections.unmodifiableSet(tmp);
        }
    }
}
