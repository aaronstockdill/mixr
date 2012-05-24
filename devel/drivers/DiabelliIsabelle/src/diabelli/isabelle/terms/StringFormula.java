/*
 * File name: StringFormula.java
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
package diabelli.isabelle.terms;

import org.isabelle.iapp.process.Message;
import org.openide.util.NbBundle;

/**
 * The raw type of the Isabelle string formula. It contains the string of the
 * formula itself as well as the object that carries the markup for the formula.
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public final class StringFormula implements CharSequence, Comparable<CharSequence> {

    //<editor-fold defaultstate="collapsed" desc="Fields">
    private final String formulaString;
    private final Message markedUpFormula;
    //</editor-fold>

    /**
     * Creates a new raw Isabelle string formula with a markup.
     * @param markedUpFormula the markup (that contains the string as well as
     * the formatting markup of the formula). Must not be {@code null}.
     * 
     * @throws IllegalArgumentException if the marked up formula is {@code null}
     * or if it does not carry the string formula.
     */
    @NbBundle.Messages({
        "SF_formula_null=The string representation of the formula must not be null.",
        "SF_mu_null=The markup of the string formula must not be null."
    })
    public StringFormula(Message markedUpFormula) {
        if (markedUpFormula == null) {
            throw new IllegalArgumentException(Bundle.SF_mu_null());
        }
        this.markedUpFormula = markedUpFormula;
        this.formulaString = markedUpFormula.getText();
        if (this.formulaString == null) {
            throw new IllegalArgumentException(Bundle.SF_formula_null());
        }
    }

    /**
     * Returns the raw string representation of the Isabelle formula.
     * @return the raw string representation of the Isabelle formula.
     */
    public final String getFormulaString() {
        return formulaString;
    }

    /**
     * Returns the object that contains the formula itself together with its
     * markup (description of particular elements).
     * @return the object that contains the formula itself together with its
     * markup (description of particular elements).
     */
    public final Message getMarkedUpFormula() {
        return markedUpFormula;
    }

    @Override
    public int length() {
        return formulaString.length();
    }

    @Override
    public char charAt(int index) {
        return formulaString.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return formulaString.subSequence(start, end);
    }

    @Override
    public String toString() {
        return formulaString;
    }

    @Override
    public int compareTo(CharSequence o) {
        if (o == null)
            throw new NullPointerException();
        else if (o instanceof String) {
            String str = (String) o;
            return this.getFormulaString().compareTo(str);
        } else if (o instanceof StringFormula) {
            StringFormula sf = (StringFormula) o;
            return this.getFormulaString().compareTo(sf.getFormulaString());
        } else {
            return this.getFormulaString().compareTo(o.toString());
        }
    }
}
