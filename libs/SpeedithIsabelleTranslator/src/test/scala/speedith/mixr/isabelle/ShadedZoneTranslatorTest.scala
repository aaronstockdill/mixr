
package speedith.mixr.isabelle

import org.junit.Test
import isabelle.Term._
import org.junit.Assert.{assertEquals, assertThat}
import org.hamcrest.CoreMatchers.equalTo
import speedith.core.lang.Zone
import speedith.mixr.isabelle.TranslationsTest.parseYXMLFile
import ShadedZoneTranslatorTest._
import ShadedZoneTranslator._

// TODO: WIP: Fix ignored tests.
class ShadedZoneTranslatorTest {

  val numberOfSpiders = 2
  val allContoursSimple = Set("A", "B")
  val allContoursComplex = Set("A", "B", "C", "D")
  val shadedZoneTranslator1 = ShadedZoneTranslator(List(SHADED_ZONE_SPEC_SIMPLE), numberOfSpiders, allContoursSimple)

  @Test
  def extractShadingSpecificationTerms_should_return_a_pair_of_terms(): Unit = {
    val setSpecification = extractShadingSpecificationTerms(SHADED_ZONE_SPEC_SIMPLE)
    assertEquals(
      Some((SHADED_SET_TERM_SIMPLE, SPIDER_SET_TERM_SIMPLE)),
      setSpecification
    )
  }

  @Test
  def extractShadedZones_must_return_the_shaded_zones(): Unit = {
    val shadedZones = shadedZoneTranslator1.shadedZones
    assertThat(
      shadedZones,
      equalTo(Seq[Zone](
        Zone.fromInContours("A", "B"),
        Zone.fromInContours("A").withOutContours("B")
      ))
    )
  }

  @Test
  def extractShadedZones_must_return_the_shaded_zones_in_a_complex_set_specification(): Unit = {
    val shadedZones = ShadedZoneTranslator(List(SHADED_ZONE_SPEC_COMPLEX), numberOfSpiders, allContoursComplex).shadedZones
    assertThat(
      shadedZones,
      equalTo(Seq[Zone](
        Zone.fromInContours("A", "D").withOutContours("B", "C"),
        Zone.fromInContours("A").withOutContours("B", "C", "D"),
        Zone.fromInContours("A", "B", "C").withOutContours("D"),
        Zone.fromInContours("A", "B").withOutContours("C", "D"),
        Zone.fromInContours("A", "C").withOutContours("B", "D")
      ))
    )
  }

  @Test(expected = classOf[IllegalArgumentException])
  def extractShadedZones_must_throw_an_exception_if_some_spiders_are_missing(): Unit = {
    val largerNumberOfSpiders = 3
    ShadedZoneTranslator(List(SHADED_ZONE_SPEC_SIMPLE), largerNumberOfSpiders, Set.empty)
  }

  @Test
  def extractShadedZones_must_return_some_terms_without_shading(): Unit = {
    val termsWithoutShading = ShadedZoneTranslator(List(SHADED_SET_TERM_SIMPLE, SPIDER_SET_TERM_SIMPLE), numberOfSpiders, Set.empty).termsWithoutShading
    assertThat(
      termsWithoutShading,
      equalTo(Seq(SHADED_SET_TERM_SIMPLE, SPIDER_SET_TERM_SIMPLE))
    )
  }

  @Test
  def extractShadedZones_must_return_empty_terms_without_shading(): Unit = {
    val termsWithoutShading = shadedZoneTranslator1.termsWithoutShading
    assertThat(
      termsWithoutShading,
      equalTo(Seq[Term]())
    )
  }
}

object ShadedZoneTranslatorTest {
  /**
   * `∃s1 s2. A ⊆ {s1, s2}`
   */
  private val SIMPLE_SHADING_FULL_FORMULA = parseYXMLFile("/speedith/mixr/isabelle/UnescapedYXML_shaded_zone_simple")
  val SHADED_ZONE_SPEC_SIMPLE = extractShadingSpecification(SIMPLE_SHADING_FULL_FORMULA)
  val SHADED_SET_TERM_SIMPLE = extractSetFromShadingSpec(SHADED_ZONE_SPEC_SIMPLE)
  val SPIDER_SET_TERM_SIMPLE = extractSpiderSetFromShadingSpec(SHADED_ZONE_SPEC_SIMPLE)
  /**
   * `∃s1 s2. (A - ((B ∪ C) ∩ D)) ⊆ {s1, s2}`
   * A & !((B | C) & D)
   * A & (!(B | C) | !D)
   * A & ((!B & !C) | !D)
   * (A & !B & !C) | (A & !D)
   */
  private val COMPLEX_SHADING_FULL_FORMULA = parseYXMLFile("/speedith/mixr/isabelle/UnescapedYXML_shaded_zone_complex")
  val SHADED_ZONE_SPEC_COMPLEX = extractShadingSpecification(COMPLEX_SHADING_FULL_FORMULA)
  val SHADED_SET_TERM_COMPLEX = extractSetFromShadingSpec(SHADED_ZONE_SPEC_COMPLEX)
  val SPIDER_SET_TERM_COMPLEX = extractSpiderSetFromShadingSpec(SHADED_ZONE_SPEC_COMPLEX)


  private def extractSetFromShadingSpec(shadingSpecTerm: Term): Term = {
    shadingSpecTerm match {
      case App(App(lessEqConst, setATerm), setOfSpiders) => setATerm
      case _ => throw new IllegalStateException()
    }
  }

  private def extractSpiderSetFromShadingSpec(shadingSpecTerm: Term): Term = {
    shadingSpecTerm match {
      case App(App(lessEqConst, setATerm), setOfSpiders) => setOfSpiders
      case _ => throw new IllegalStateException()
    }
  }

  private def extractShadingSpecification(term: Term): Term = {
    term match {
      case App(_, App(exHOLConst, Abs(s1, typ1, App(exHOLConst2, Abs(s2, typ2, setSpec))))) => setSpec
      case _ => throw new IllegalStateException()
    }
  }
}