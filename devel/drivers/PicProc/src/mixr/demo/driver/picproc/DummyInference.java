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
package mixr.demo.driver.picproc;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;
import mixr.MixR;
import mixr.components.GoalTransformer;
import mixr.isabelle.terms.StringFormat;
import mixr.logic.AutomatedInferenceRule;
import mixr.logic.Formula;
import mixr.logic.FormulaRepresentation;
import mixr.logic.Goal;
import mixr.logic.GoalTransformationResult;
import mixr.logic.InferenceRule;
import mixr.logic.InferenceRuleDescriptor;
import mixr.logic.InferenceStepResult;
import mixr.logic.InferenceTarget;
import mixr.logic.InferenceTargets;
import mixr.logic.OracleProofTrace;
import mixr.logic.Sentence;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import propity.util.MovableArrayList;

/**
 * A simple inference rule that extracts the shape of the object from the image.
 *
 * <p>This inference rule counts the number of non-transparent pixels (with
 * opacity lower than 50%) and returns one of the three:
 *
 * <ul>
 *
 * <il>Triangle (equilateral)</il>
 *
 * <il>Square</il>
 *
 * <il>Circle</il>
 *
 * </ul>
 *
 * </p>
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public class DummyInference implements InferenceRuleDescriptor, InferenceRule, AutomatedInferenceRule {

    private final GoalTransformer owner;

    DummyInference(GoalTransformer owner) {
        this.owner = owner;
    }

    @Override
    @NbBundle.Messages({
        "DI_name=Extract the shape of object"
    })
    public String getName() {
        return Bundle.DI_name();
    }

    @Override
    @NbBundle.Messages({
        "DI_description=Extracts the shape of the single object in the image."
    })
    public String getDescription() {
        return Bundle.DI_description();
    }

    @Override
    public boolean isFullyAutomated() {
        return true;
    }

    @Override
    public GoalTransformer getOwner() {
        return owner;
    }

    @Override
    public void applyInferenceRule(InferenceTargets targets) {
        InferenceStepResult isr = applyAutomatedInferenceRule(targets);
        // Put the result back to the master reasoner:
        Lookup.getDefault().lookup(MixR.class).getGoalManager().commitTransformedGoals(isr);
    }

    @Override
    public InferenceStepResult applyAutomatedInferenceRule(InferenceTargets targets) {
        final int targetsCount = targets.getInferenceTargetsCount();
        List<Sentence>[] transformedSentences = new List[targetsCount];
        InferenceTarget[] transformedTargets = new InferenceTarget[targetsCount];
        for (int i = 0; i < targetsCount; i++) {
            Sentence s = targets.getSentenceAt(i);
            try {
                Formula f = s.asFormula();
                if (f != null) {
                    // Apply the inference rule on this formula:
                    FormulaRepresentation imageRepresentation = f.getRepresentation(ImageUrlFormat.getInstance());
                    if (imageRepresentation != null && imageRepresentation.getFormula() instanceof ImageUrlFormula) {
                        ImageUrlFormula imageUrlFormula = (ImageUrlFormula) imageRepresentation.getFormula();
                        String shape = extractShape(imageUrlFormula);
                        Formula inferedFormula = new Formula(StringFormat.createFormula("ShapeOf " + imageUrlFormula.getName() + " " + shape), f.getRole());
                        transformedSentences[i] = Arrays.asList((Sentence) inferedFormula);
                        transformedTargets[i] = targets.getInferenceTargets().get(i);
                    }
                }
            } catch (UnsupportedOperationException uoe) {
            }
        }
        GoalTransformationResult goalTransformationResult = GoalTransformationResult.create(getOwner(), targets, transformedTargets, transformedSentences);
        return goalTransformationResult;
    }

    private String extractShape(ImageUrlFormula imageUrlFormula) {
        BufferedImage image = imageUrlFormula.getImage();
        int minPixels = 0;
        int maxPixels = 0;
        int partlyVisiblePixels = 0;
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int pixelColour = image.getRGB(i, j);
                // Get the alpha component:
                int alpha = (pixelColour & 0xff000000) >>> 24;
                if (alpha >= 0xf9) {
                    maxPixels++;
                } else if (alpha > 0x05) {
                    partlyVisiblePixels++;
                } else {
                    minPixels++;
                }
            }
        }
        // Is it a square?
        double[] distances = new double[]{
            Math.abs((double) (maxPixels + partlyVisiblePixels) / (double) partlyVisiblePixels * 4 * 4 - partlyVisiblePixels),
            Math.abs((double) (maxPixels + partlyVisiblePixels) / (double) partlyVisiblePixels * 2 * 2 * Math.PI - partlyVisiblePixels),
            Math.abs((double) (maxPixels + partlyVisiblePixels) / (double) partlyVisiblePixels * 12 * Math.sqrt(3) - partlyVisiblePixels)
        };
        // Is it a square?
        String[] shapes = new String[]{
            "Square",
            "Circle",
            "Triangle"
        };
        
        double min = Double.MAX_VALUE;
        int minIdx = 0;
        for (int i = 0; i < distances.length; i++) {
            double d = distances[i];
            if (d <= min) {
                min = d;
                minIdx = i;
            }
        }
        
        return shapes[minIdx];
    }
}
