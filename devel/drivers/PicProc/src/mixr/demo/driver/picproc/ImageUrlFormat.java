/*
 * File name: NatLangFormat.java
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

import java.util.Set;
import mixr.logic.FormulaFormatDescriptor;
import mixr.logic.FormulaRepresentation;
import mixr.logic.FreeVariable;
import mixr.logic.TextEncodedFormulaFormat;
import mixr.logic.TextEncodedFormulaFormat.FormulaEncodingException;
import mixr.logic.VariableReferencingFormulaFormat;
import org.openide.util.NbBundle;

/**
 * The formula format descriptor for the <span style="font-style:italic;">bitmap
 * image hyperlinks</span> formula format.
 *
 * <p>This is a dummy formula format which enables integration of images into
 * theorem provers.</p>
 * 
 * <p>Other drivers may use this format to enable reasoning on images, of course.
 * However, the PicProc driver provides only mock inference rules on images.</p>
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
@NbBundle.Messages({
    "ImgUrlFormat_pretty_name=Image Url"
})
public class ImageUrlFormat extends FormulaFormatDescriptor implements TextEncodedFormulaFormat, VariableReferencingFormulaFormat {

    /**
     * The name of the hyperlinked image format.
     */
    private static final String FormatFormatName = "ImgUrl";

    private ImageUrlFormat() {
        super(FormatFormatName, Bundle.ImgUrlFormat_pretty_name(), ImageUrlFormula.class);
    }

    @Override
    public String encodeAsString(Object formula) throws FormulaEncodingException {
        if (formula instanceof ImageUrlFormula) {
            return formula.toString();
        }
        return null;
    }

    @Override
    public ImageUrlFormula decodeFromString(String encodedFormula) throws FormulaEncodingException {
        return new ImageUrlFormula(encodedFormula);
    }

    /**
     * Returns the singleton instance of the natural language format descriptor.
     *
     * @return the singleton instance of the natural language format descriptor.
     */
    public static ImageUrlFormat getInstance() {
        return SingletonContainer.Instance;
    }

    @Override
    public Set<FreeVariable> getFreeVariables(FormulaRepresentation formula) {
        if (formula != null && formula.getFormula() instanceof ImageUrlFormula) {
            ImageUrlFormula imageUrlFormula = (ImageUrlFormula) formula.getFormula();
            return FreeVariable.createSingletonSet(imageUrlFormula.getName(), "PicProcImage");
        } else {
            return null;
        }
    }

    private static class SingletonContainer {

        private static final ImageUrlFormat Instance = new ImageUrlFormat();
    }
}
