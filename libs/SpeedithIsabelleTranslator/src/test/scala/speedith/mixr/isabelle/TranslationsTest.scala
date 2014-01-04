package speedith.mixr.isabelle

import org.junit.Test
import java.io.{LineNumberReader, InputStreamReader}
import java.nio.charset.Charset
import isabelle.Term.Term
import mixr.isabelle.pure.lib.TermYXML
import org.junit.Assert.assertTrue
import speedith.core.lang.{SpiderDiagram, CompoundSpiderDiagram}
import TranslationsTest._

class TranslationsTest {

  @Test
  def termToSpiderDiagram_must_return_a_compound_spider_diagram_with_spiders(): Unit = {
    val spiderDiagram = translateSpiderDiagramFromIsaTerm(YXML_FILE_SPIDER_DIAGRAM_AB_WITH_SPIDERS)
    assertTrue(spiderDiagram.isInstanceOf[CompoundSpiderDiagram])
  }
}

object TranslationsTest {

  /**
   * (∃s1 s2. distinct[s1, s2] ∧ s1 ∈ A ∩ B ∧ s2 ∈ (A - B) ∪ (B - A)) ⟶ (∃t1 t2. distinct[t1, t2] ∧ t1 ∈ A ∧ t2 ∈ B) ∧ (A ∩ B) ≠ {}
   */
  val YXML_FILE_SPIDER_DIAGRAM_AB_WITH_SPIDERS = "/speedith/mixr/isabelle/UnescapedYXML_spider_diagram_AB_with_spiders"
  /**
   * (∃s1 s2. distinct[s1, s2] ∧ s1 ∈ A ∩ B ∧ s2 ∈ (A - B) ∪ (B - A) ∧ A ⊆ {s1, s2}) ⟶ (∃t1 t2. distinct[t1, t2] ∧ t1 ∈ A ∧ t2 ∈ B) ∧ (A ∩ B) ≠ {}
   */
  val YXML_FILE_SPIDER_DIAGRAM_AB_WITH_SPIDERS_AND_SHADED_ZONES = "/speedith/mixr/isabelle/UnescapedYXML_spider_diagram_AB_with_spiders_and_shaded_zones"

  def translateSpiderDiagramFromIsaTerm(yxmlFilePath: String): SpiderDiagram = {
    val isabelleTerm = parseYXMLFile(yxmlFilePath)
    Translations.termToSpiderDiagram(isabelleTerm)
  }

  private def readFirstLine(file: String): String = {
    val inputStream = classOf[TranslationsTest].getResourceAsStream(file)
    val reader = new InputStreamReader(inputStream, Charset.forName("US-ASCII"))
    new LineNumberReader(reader).readLine()
  }

  def parseYXMLFile(file: String): Term = {
    TermYXML.parseYXML(readFirstLine(file))
  }
}