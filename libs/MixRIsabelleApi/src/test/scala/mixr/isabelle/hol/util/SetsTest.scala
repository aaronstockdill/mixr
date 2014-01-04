package mixr.isabelle.hol.util

import org.junit.Test
import isabelle.Term.{TFree, Free, Bound, App}
import mixr.isabelle.pure.lib.TermYXMLTest.parseYXMLFile
import Sets._
import SetsTest._
import org.junit.Assert.assertEquals
import isabelle.Term
import mixr.isabelle.pure.lib.TermUtils

class SetsTest {

  @Test(expected = classOf[IllegalArgumentException])
  def element_extraction_must_throw_an_exception_if_the_term_is_not_a_set_specification(): Unit = {
    extractInsertedSetElements(App(Bound(0), Bound(1)))
  }

  @Test
  def element_extraction_must_return_an_empty_list_for_bottom(): Unit = {
    val noElements = extractInsertedSetElements(emptySetTerm)
    assertEquals(Nil, noElements)
  }

  @Test
  def element_extraction_must_return_a_all_elements_of_nonempty_sets(): Unit = {
    assertEquals(List(freeVar("a")), extractInsertedSetElements(singletonSetTerm))
    assertEquals(List(freeVar("a"), freeVar("b")), extractInsertedSetElements(twoSetTerm))
    assertEquals(List(freeVar("a"), freeVar("b"), freeVar("c"), freeVar("d")), extractInsertedSetElements(largeSetTerm))
  }
}

object SetsTest {

  /**
   * `{} ⊆ {a}`
   */
  val SETS_TESTS_FORMULA = parseYXMLFile("/mixr/isabelle/pure/lib/UnescapedYXML_sets_element_extraction_test_emptySet_subsetOf_singletonSet")

  /**
   * `{a,b} ⊆ {a,b,c,d}`
   */
  val LARGE_SETS_TESTS_FORMULA = parseYXMLFile("/mixr/isabelle/pure/lib/UnescapedYXML_sets_element_extraction_large_sets")

  val (emptySetTerm, singletonSetTerm) = SETS_TESTS_FORMULA match {
    case App(truePropConst, App(App(lessEqConst, emptySet), setA)) => (emptySet, setA)
    case _ => throw new IllegalStateException()
  }

  val (twoSetTerm, largeSetTerm) = LARGE_SETS_TESTS_FORMULA match {
    case App(truePropConst, App(App(lessEqConst, emptySet), setA)) => (emptySet, setA)
    case _ => throw new IllegalStateException()
  }

  def freeVar(freeVarName: String): Term.Free = {
    Free(freeVarName, TFree("'a", TermUtils.HOL_LIST_TYPE))
  }
}