/*
 * File name: SpeedithFormatDescriptor.java
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
package speedith.mixr.logic;

import mixr.logic.FormulaFormatDescriptor;
import org.openide.util.NbBundle;
import speedith.core.lang.SpiderDiagram;

/**
 * The formula format descriptor for Speedith's formulae in the native form.
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
@NbBundle.Messages({
    "SFD_sd_format_pretty_name=Spider diagram"
})
public class SpeedithFormatDescriptor extends FormulaFormatDescriptor {
    
    //<editor-fold defaultstate="collapsed" desc="Fields">
    /**
     * The name of Speedith's internal spider diagram format. This name is used
     * in {@link FormulaFormatDescriptor#getFormatName()}.
     */
    public static final String SpeedithFormatName = "Speedith_sd";
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Constructor">
    private SpeedithFormatDescriptor() {
        super(SpeedithFormatName, Bundle.SFD_sd_format_pretty_name(), SpiderDiagram.class);
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Singleton Instance">
    /**
     * Returns the singleton instance of the Speedith internal spider diagram format descriptor.
     * @return the singleton instance of the Speedith internal spider diagram format descriptor.
     */
    public static SpeedithFormatDescriptor getInstance() {
        return SingletonContainer.Instance;
    }

    private static class SingletonContainer {
        
        private static final SpeedithFormatDescriptor Instance = new SpeedithFormatDescriptor();
        
    }
    // </editor-fold>
    
}
