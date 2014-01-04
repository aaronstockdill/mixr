package speedith.mixr.isabelle

import isabelle.Term.{App, Term}
import speedith.core.lang.Zone
import mixr.isabelle.hol.util.Sets.HOL_SUBSET_CONST
import speedith.mixr.isabelle.ShadedZoneTranslator.extractShadingSpecificationTerms

// TODO: WIP
case class ShadedZoneTranslator(terms: Seq[Term], numberOfSpiders: Int, contourNames: scala.collection.Set[String]) {

  val (shadedZones: Seq[Zone], termsWithoutShading: Seq[Term]) = extractShadedZonesAndInvalidTerms()

  def extractShadedZonesAndInvalidTerms(): Pair[Seq[Zone], Seq[Term]] = {
    val shadingSpecTerms = terms.map(extractShadingSpecificationTerms)
    (Nil, Nil)
  }

}

object ShadedZoneTranslator {

  private[isabelle] def extractShadingSpecificationTerms(term: Term): Option[(Term, Term)] = {
    term match {
      case App(App(HOL_SUBSET_CONST, set), spiders) =>
        Some((set, spiders))
      case _ =>
        None
    }
  }
}