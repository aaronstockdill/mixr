package mixr.isabelle.pure.lib

import isabelle.Term.Typ

sealed case class FreeVar(name: String, typ: Typ)
