package mixr.isabelle.pure.lib

sealed abstract class Placeholder

case class PlaceholderWithoutVars(formulaFormat: String, payloadFormula: String) extends Placeholder

case class PlaceholderWithVars(variables: java.util.List[FreeVar], formulaFormat: String, payloadFormula: String) extends Placeholder
