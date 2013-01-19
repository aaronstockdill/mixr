/*
 * File name: TermsToMixR.java
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
package mixr.isabelle.terms;

import mixr.isabelle.pure.lib.TermUtils;
import mixr.isabelle.terms.Bundle;
import mixr.logic.Formula;
import mixr.logic.FormulaRepresentation;
import mixr.logic.Goal;
import isabelle.Term;
import java.util.ArrayList;
import java.util.Collection;
import org.isabelle.iapp.proofdocument.ProofDocument;
import org.openide.util.NbBundle;

/**
 * Contains method that convert {@link isabelle.Term Isabelle terms} to {@link
 * Goal MixR goals}.
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public final class TermsToMixR {

    // <editor-fold defaultstate="collapsed" desc="Constructor">
    private TermsToMixR() {
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Public Methods">
    /**
     * Takes an Isabelle term, extracts premises and conclusions from it, and
     * puts them into a MixR goal.
     *
     * @param term well, the term to make into a goal.
     * @param proofContext the context in which the proof lives.
     * @return a MixR goal (with the extracted premises and conclusions.
     */
    @NbBundle.Messages({
        "TTDG_term_null=Cannot convert a null term into a goal."
    })
    public static TermGoal toGoal(Term.Term term, ProofDocument proofContext) {
        if (term == null) {
            throw new IllegalArgumentException(Bundle.TTDG_term_null());
        }
        ArrayList<Term.Term> premises = new ArrayList<>();
        ArrayList<Term.Free> variables = new ArrayList<>();
        Term.Term body = TermUtils.findQuantifiedVarsAndBody(term, variables);
        Term.Term conclusion = TermUtils.findPremisesAndConclusion(body, premises);
        return new TermGoal(proofContext, variables, premises, conclusion, term);
    }

    /**
     * Takes an Isabelle term and wraps it into a MixR formula.
     *
     * @param term the term to wrap.
     * @param role the role of this term in the goal.
     * @return a MixR formula (containing the term and a description of the
     * term format).
     */
    public static Formula toFormula(Term.Term term, Formula.FormulaRole role) {
        return new Formula(new FormulaRepresentation(term, TermFormatDescriptor.getInstance()), role);
    }

    /**
     * Takes a bunch of Isabelle terms and wraps them into a MixR formulae.
     *
     * @param terms the terms to wrap.
     * @param role the role of these terms in the goal.
     * @return a bunch of MixR formulae (containing the terms and
     * descriptions of the term format).
     */
    public static ArrayList<Formula> toFormulae(Collection<Term.Term> terms, Formula.FormulaRole role) {
        if (terms != null) {
            ArrayList<Formula> formulae = new ArrayList<>();
            for (Term.Term term : terms) {
                formulae.add(toFormula(term, role));
            }
            return formulae;
        } else {
            return null;
        }
    }
    // </editor-fold>
}
