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

import mixr.components.MixRComponent;

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
public class PicProc implements MixRComponent {

    @Override
    public String getName() {
        return "PicProc";
    }
}
