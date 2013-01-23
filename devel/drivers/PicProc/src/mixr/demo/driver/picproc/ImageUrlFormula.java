/*
 * File name: ImageUrlFormula.java
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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import mixr.logic.TextEncodedFormulaFormat;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;

/**
 * This formula object contains the image as well as the URL from which the
 * image was loaded.
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public class ImageUrlFormula {

    private final File sourceFile;
    private BufferedImage image;

    @NbBundle.Messages({
        "ImgUrlFormula_source_empty=The URL of the image must not be empty.",
        "# {0} - sourceUrl",
        "ImgUrlFormula_image_not_opened=Could not open the image from '{0}'."
    })
    public ImageUrlFormula(String sourceUrl) throws TextEncodedFormulaFormat.FormulaEncodingException {
        if (sourceUrl == null || sourceUrl.isEmpty()) {
            throw new IllegalArgumentException(Bundle.ImgUrlFormula_source_empty());
        }
        try {
            this.sourceFile = Utilities.toFile(new URI(sourceUrl));
            image = javax.imageio.ImageIO.read(sourceFile);
        } catch (IOException | URISyntaxException ex) {
            throw new TextEncodedFormulaFormat.FormulaEncodingException(Bundle.ImgUrlFormula_image_not_opened(sourceUrl), ex);
        }
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public BufferedImage getImage() {
        return image;
    }

    /**
     * Returns the name of the image file. This name will be used as a variable
     * reference in the placeholder.
     *
     * @return the name of the image file.
     */
    String getName() {
        final String name = sourceFile.getName();
        return name.substring(0, name.lastIndexOf('.'));
    }

    /**
     * Returns the path to the image file. This will be inserted into the
     * placeholder.
     *
     * @return the path to the image file.
     */
    @Override
    public String toString() {
        return getSourceFile().getPath();
    }
}
