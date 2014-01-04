
package speedith.mixr.isabelle

import org.junit.{Ignore, Test}
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
  val allContours1 = Set("A", "B")
  val shadedZoneTranslator1 = ShadedZoneTranslator(List(SHADED_ZONE_A_SPEC_TERM), numberOfSpiders, allContours1)

  @Test
  def extractShadingSpecificationTerms_should_return_a_pair_of_terms(): Unit = {
    val setSpecification = extractShadingSpecificationTerms(SHADED_ZONE_A_SPEC_TERM)
    assertEquals(
      Some((setA, setOfSpiders_s1_s2)),
      setSpecification
    )
  }

  @Test
  @Ignore
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

  @Test(expected = classOf[IllegalArgumentException])
  @Ignore
  def extractShadedZones_must_throw_an_exception_if_some_spiders_are_missing(): Unit = {
    val largerNumberOfSpiders = 3
    ShadedZoneTranslator(List(SHADED_ZONE_A_SPEC_TERM), largerNumberOfSpiders, Set.empty)
  }

  @Test
  @Ignore
  def extractShadedZones_must_return_some_terms_without_shading(): Unit = {
    val termsWithoutShading = ShadedZoneTranslator(List(setA, setOfSpiders_s1_s2), numberOfSpiders, Set.empty).termsWithoutShading
    assertThat(
      termsWithoutShading,
      equalTo(Seq(setA, setOfSpiders_s1_s2))
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
  val SIMPLE_SHADED_ZONE_FORMULA = parseYXMLFile("/speedith/mixr/isabelle/UnescapedYXML_shaded_zone_simple")

  val SHADED_ZONE_A_SPEC_TERM = SIMPLE_SHADED_ZONE_FORMULA match {
    case App(_, App(exHOLConst, Abs(s1, typ1, App(exHOLConst2, Abs(s2, typ2, setSpec))))) => setSpec
    case _ => throw new IllegalStateException()
  }

  val (setA, setOfSpiders_s1_s2) = SHADED_ZONE_A_SPEC_TERM match {
    case App(App(lessEqConst, setATerm), setOfSpiders) => (setATerm, setOfSpiders)
    case _ => throw new IllegalStateException()
  }
}