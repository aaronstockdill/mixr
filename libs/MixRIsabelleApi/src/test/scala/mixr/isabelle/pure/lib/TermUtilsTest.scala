package mixr.isabelle.pure.lib

import org.junit.Test
import TermYXMLTest._
import TermYXML._
import isabelle.Term.Type
import scala.collection.JavaConversions._
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.equalTo

class TermUtilsTest {

  @Test
  def extractPlaceholder_when_given_a_term_containing_a_placeholder_should_find_and_return_it(): Unit = {
    val term = parseYXML(natLangWithVarsUnescapedYXML)
    val expectedPlaceholder = PlaceholderWithVars(List(FreeVar("HeterogeneousStatements.Ann",Type("HeterogeneousStatements.person",List())), FreeVar("HeterogeneousStatements.Bob",Type("HeterogeneousStatements.person",List()))),"NatLang", " Ann is a child of Bob.")
    val placeholder = TermUtils.extractPlaceholder(term)
    assertThat(
      placeholder,
      equalTo[Placeholder](expectedPlaceholder)
    )
  }

  @Test
  def extractPlaceholder_when_given_a_term_containing_a_placeholder_without_vars_should_find_and_return_it(): Unit = {
    val term = parseYXML(natLangWithoutVarsUnescapedYXML)
    val expectedPlaceholder = PlaceholderWithoutVars("NatLang", " Ann is a child of Bob.")
    val placeholder = TermUtils.extractPlaceholder(term)
    assertThat(
      placeholder,
      equalTo[Placeholder](expectedPlaceholder)
    )
  }

  @Test
  def extractPlaceholder_when_given_a_term_containing_a_placeholder_with_no_leading_space_should_find_and_return_it(): Unit = {
    val term = parseYXML(natLangWithoutVarsNoLeadingSpaceUnescapedYXML)
    val expectedPlaceholder = PlaceholderWithoutVars("NatLang", "Ann is a child of Bob.")
    val placeholder = TermUtils.extractPlaceholder(term)
    assertThat(
      placeholder,
      equalTo[Placeholder](expectedPlaceholder)
    )
  }
}
