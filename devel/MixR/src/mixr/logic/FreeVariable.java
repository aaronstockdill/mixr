/*
 * File name: FreeVariable.java
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
package mixr.logic;

import java.util.TreeSet;
import org.openide.util.NbBundle;

/**
 * Two free variables are the same if they have the same name.
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public class FreeVariable implements Comparable<FreeVariable> {

    private final String name;
    private final Object type;

    /**
     * 
     * @param name the name of the variable.
     * @param type the type of the variable.
     */
    @NbBundle.Messages({
        "FreeVariable_name_empty=The name of a free variable must not be empty."
    })
    public FreeVariable(String name, Object type) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException(Bundle.FreeVariable_name_empty());
        }
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Object getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FreeVariable) {
            FreeVariable freeVariable = (FreeVariable) obj;
            return this.getName().equals(freeVariable.getName());
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(FreeVariable o) {
        if (o == null) {
            throw new NullPointerException();
        } else {
            return this.getName().compareToIgnoreCase(o.getName());
        }
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    // <editor-fold defaultstate="collapsed" desc="Static Convenience Methods">
    /**
     * Creates a variable set containing a single variable.
     *
     * <p>This method is useful when implementing the method in a new formula
     * format
     * {@link VariableReferencingFormulaFormat#getFreeVariables(mixr.logic.FormulaRepresentation)}</p>
     *
     * @param name the name of the variable.
     * @param type the type of the variable.
     * @return a variable set containing a single variable.
     */
    public static TreeSet<FreeVariable> createSingletonSet(String name, Object type) {
        TreeSet<FreeVariable> varSet = new TreeSet<>();
        varSet.add(new FreeVariable(name, type));
        return varSet;
    }
    // </editor-fold>
}
