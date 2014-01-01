package mixr.logic.normalization

import org.junit.Test
import mixr.logic.normalization.NormalForms._
import mixr.logic.normalization.Formula._
import org.junit.Assert._
import org.hamcrest.CoreMatchers._

class NormalFormsTest {

  @Test
  def conversion_to_NNF_should_not_change_an_atom(): Unit = {
    applyNNFAndAssert('A, 'A)
  }

  @Test
  def conversion_to_NNF_should_not_change_a_singly_negated_atom(): Unit = {
    applyNNFAndAssert(!'A, !'A)
  }

  @Test
  def conversion_to_NNF_should_not_convert_a_double_negated_atom_to_the_atom(): Unit = {
    applyNNFAndAssert(!(!'A), 'A)
  }

  @Test
  def conversion_to_NNF_should_not_move_negation_into_a_disjunction(): Unit = {
    applyNNFAndAssert(!('A | 'B), !'A & !'B)
  }

  @Test
  def conversion_to_NNF_should_not_move_negation_into_a_conjunction(): Unit = {
    applyNNFAndAssert(!('A & 'B), !'A | !'B)
  }

  @Test
  def conversion_to_NNF_should_recurse_into_a_conjunction(): Unit = {
    applyNNFAndAssert('A & !('B | !'C), 'A & (!'B & 'C))
  }

  @Test
  def conversion_to_NNF_should_recurse_into_a_disjunction(): Unit = {
    applyNNFAndAssert(!('A & !'B) | !'C, (!'A | 'B) | !'C)
  }

  @Test
  def conversion_to_CNF_should_remove_double_negations_inside_a_conjunction(): Unit = {
    applyCNFAndAssert(
      !(!'A) & 'B,
      'A & 'B
    )
  }

  @Test
  def conversion_to_CNF_should_not_change_a_formula_that_is_already_in_CNF(): Unit = {
    applyCNFAndAssert('A, 'A)
    applyCNFAndAssert(!'A, !'A)
    applyCNFAndAssert(
      !'A & 'B,
      !'A & 'B
    )
    applyCNFAndAssert(
      'A & 'B,
      'A & 'B
    )
    applyCNFAndAssert(
      'A | 'B,
      'A | 'B
    )
    applyCNFAndAssert(
      !'A & ('B | 'C),
      !'A & ('B | 'C)
    )
    applyCNFAndAssert(
      ('A | 'B) & (!'B | 'C | !'D) & ('D | !'E),
      ('A | 'B) & (!'B | 'C | !'D) & ('D | !'E)
    )
  }

  @Test
  def conversion_to_CNF_should_change_a_disjunction_with_nested_conjunction(): Unit = {
    applyCNFAndAssert(
      'A | ('B & 'C),
      ('A | 'B) & ('A | 'C)
    )
    applyCNFAndAssert(
      ('B & 'C) | 'A,
      ('B | 'A) & ('C | 'A)
    )
  }

  @Test
  def conversion_to_CNF_should_change_a_negated_disjunction(): Unit = {
    applyCNFAndAssert(
      !('A | 'B),
      !'A & !'B
    )
  }

  @Test
  def conversion_to_CNF_should_change_a_disjunction_with_nested_conjunctions(): Unit = {
    applyCNFAndAssert(
      ('A & 'B) | ('C & 'D),
      (('A | 'C) & ('A | 'D)) & (('B | 'C) & ('B | 'D))
    )
  }

  @Test
  def conversion_to_DNF_should_remove_double_negations(): Unit = {
    applyDNFAndAssert(
      !(!'A) & 'B,
      'A & 'B
    )
    applyDNFAndAssert(
      'A | !(!'B),
      'A | 'B
    )
  }

  @Test
  def conversion_to_DNF_should_not_change_a_formula_that_is_already_in_DNF(): Unit = {
    applyDNFAndAssert('A, 'A)
    applyDNFAndAssert(!'A, !'A)
    applyDNFAndAssert(
      'A & 'B,
      'A & 'B
    )
    applyDNFAndAssert(
      !'A & 'B,
      !'A & 'B
    )
    applyDNFAndAssert(
      'A | 'B,
      'A | 'B
    )
    applyDNFAndAssert(
      !'A | ('B & 'C),
      !'A | ('B & 'C)
    )
    applyDNFAndAssert(
      ('A & 'B) | (!'B & 'C & !'D) | ('D & !'E),
      ('A & 'B) | (!'B & 'C & !'D) | ('D & !'E)
    )
  }

  @Test
  def conversion_to_DNF_should_change_a_conjunction_with_nested_disjunction(): Unit = {
    applyDNFAndAssert(
      'A & ('B | 'C),
      ('A & 'B) | ('A & 'C)
    )
    applyDNFAndAssert(
      ('B | 'C) & 'A,
      ('B & 'A) | ('C & 'A)
    )
  }

  @Test
  def conversion_to_DNF_should_change_a_negated_conjunction(): Unit = {
    applyDNFAndAssert(
      !('A & 'B),
      !'A | !'B
    )
  }

  @Test
  def conversion_to_DNF_should_change_a_conjunction_with_nested_disjunctions(): Unit = {
    applyDNFAndAssert(
      ('A | 'B) & ('C | 'D),
      (('A & 'C) | ('A & 'D)) | (('B & 'C) | ('B & 'D))
    )
  }

  private def applyNNFAndAssert(initialFormula: Formula[Symbol], expectedFormula: Formula[Symbol]) {
    assertThat(
      toNNF(initialFormula),
      equalTo(expectedFormula)
    )
  }

  private def applyCNFAndAssert(initialFormula: Formula[Symbol], expectedFormula: Formula[Symbol]) {
    assertThat(
      toCNF(initialFormula),
      equalTo(expectedFormula)
    )
  }

  private def applyDNFAndAssert(initialFormula: Formula[Symbol], expectedFormula: Formula[Symbol]) {
    assertThat(
      toDNF(initialFormula),
      equalTo(expectedFormula)
    )
  }
}
