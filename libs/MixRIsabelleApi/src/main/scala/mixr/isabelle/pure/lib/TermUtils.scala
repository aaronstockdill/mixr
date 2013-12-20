package mixr.isabelle.pure.lib
import java.util.ArrayList
import isabelle.Term._
import scala.collection.mutable.Buffer
import scala.collection.JavaConversions
import scala.collection.mutable.ArrayBuffer

sealed case class FreeVar(name: String, typ: Typ)

sealed abstract class Placeholder

case class PlaceholderWithoutVars(formulaFormat: String, payloadFormula: String) extends Placeholder

case class PlaceholderWithVars(variables: java.util.List[FreeVar], formulaFormat: String, payloadFormula: String) extends Placeholder

/**
 * Provides a bunch of methods for
 */
object TermUtils {

  // Binary operators:
  val HOLConjunction = "HOL.conj"
  val HOLDisjunction = "HOL.disj"
  val HOLImplication = "HOL.implies"
  val HOLEquality = "HOL.eq"
  val MetaImplication = "==>"
  // Quantifiers:
  val HOLExistential = "HOL.Ex"
  val HOLAll = "HOL.All"
  val MetaAll = "all"
  // Unary operators:
  val HOLNot = "HOL.Not"
  val HOLTrueprop = "HOL.Trueprop"
  // Constants:
  val HOLTrue = "HOL.True"
  val HOLFalse = "HOL.False"
  // Types
  val HOLTypeBool = "HOL.bool"
  val HOL_bool = HOLTypeBool

  def main(args: Array[String]): Unit = {
    val t = TermYXML.parseYXML(TermYXML.Examples_unescaped(1))
    traverseTermTree(t, t => {
      val ph = extractPlaceholder(t)
      if (ph == null) {
        println("Not a placeholder.")
        false
      } else {
        println("Placeholder: %s".format(ph))
        true
      }
    })
  }

  /**
   * Extracts the premises and the conclusion from the given term `t`. This
   * method puts the premises into the given array and returns the conclusion.
   */
  def findPremisesAndConclusion(t: Term, premises: java.util.List[Term]): Term = {
    t match {
      case App(App(Const(MetaImplication, _), lhsTerm), rhsTerm) => {
        premises.add(lhsTerm)
        findPremisesAndConclusion(rhsTerm, premises)
      }
      case _ => t
    }
  }

  /**
   * Extracts the premises and the conclusion from the given term `t`. This
   * method puts the premises into the given array and returns the conclusion.
   */
  def findPremisesAndConclusion(t: Term, premises: Buffer[Term]): Term = findPremisesAndConclusion(t, JavaConversions.bufferAsJavaList(premises))

  /**
   * Extracts the globally meta-quantified variables from the term, puts them
   * into the given array list packed as separate `Term.Free`, and returns the
   * body.
   *
   * For example, from the formula `⋀ x y z. ⟦ A(x, y, z) ⟧ ⟹ B(x, y, z)`, we
   * get `⟦ A(x, y, z) ⟧ ⟹ B(x, y, z)` as the body, and the list `[x, y, z]`
   * in the variables list.
   *
   * The quantified variables in the body are still retained as bound.
   */
  def findQuantifiedVarsAndBody(t: Term, variables: java.util.List[Free]): Term = {
    t match {
      case App(Const(MetaAll, _), Abs(varName, varType, body)) => {
        variables.add(Free(varName, varType))
        findQuantifiedVarsAndBody(body, variables)
      }
      case _ => t
    }
  }

  /**
   * Extracts the globally meta-quantified variables from the term, puts them
   * into the given array list packed as separate `Term.Free`, and returns the
   * body.
   *
   * For example, from the formula `⋀ x y z. ⟦ A(x, y, z) ⟧ ⟹ B(x, y, z)`, we
   * get `⟦ A(x, y, z) ⟧ ⟹ B(x, y, z)` as the body, and the list `[x, y, z]`
   * in the variables list.
   *
   * The quantified variables in the body are still retained as bound.
   */
  def findQuantifiedVarsAndBody(t: Term, variables: Buffer[Free]): Term = findQuantifiedVarsAndBody(t, JavaConversions.bufferAsJavaList(variables))

  /**
   * The constant name for the HOL list Cons constructor.
   */
  val HOLListCons = "List.list.Cons"

  /**
   * The constant name for the HOL list Nil constructor.
   */
  val HOLListNil = "List.list.Nil"

  /**
   * Returns the terms that represent the elements of the `listTerm` which must
   * be an HOL list.
   */
  def getListElements(listTerm: Term, outElements: java.util.List[Term]): java.util.List[Term] = {
    listTerm match {
      case App(App(Const(HOLListCons, _), element), rest) => { outElements.add(element); getListElements(rest, outElements); }
      case Const(HOLListNil, _) => {}
      case x => throw new IllegalArgumentException("The given term is not an HOL list. It contained the term '%s'.".format(x))
    }
    outElements
  }

  def traverseListElements(listTerm: Term, visitor: Term => Unit): Unit = {
    listTerm match {
      case App(App(Const(HOLListCons, _), element), rest) => { visitor(element); traverseListElements(rest, visitor); }
      case Const(HOLListNil, _) => {}
      case _ => throw new IllegalArgumentException("The given term is not an HOL list. It contained the term '%s'.".format(listTerm))
    }
  }

  /**
   * Returns the terms that represent the elements of the `listTerm` which must
   * be an HOL list.
   */
  def getListElements(listTerm: Term, outElements: Buffer[Term] = ArrayBuffer[Term]()): Buffer[Term] = {
    getListElements(listTerm, JavaConversions.bufferAsJavaList(outElements))
    outElements
  }

  /**
   * Visits every term in the given term tree.
   */
  def traverseTermTree(t: Term, visitor: Term => Boolean): Boolean = {
    if (visitor(t)) return true
    t match {
      case Abs(_, _, t1) => traverseTermTree(t1, visitor)
      case App(t1, t2) => { if (traverseTermTree(t1, visitor)) return true; traverseTermTree(t2, visitor); }
      case _ => false
    }
  }

  /**
   * If the term represents a string it will extract the string from it. Otherwise it will return `null`.
   */
  def extractString(t: Term): String = {
    t match {
      case App(App(NonEmptyStringTerm, firstChar), otherChars) => {
        val sb = new StringBuilder()
        sb.append(extractChar(firstChar))
        traverseListElements(otherChars, ch => {
          sb.append(extractChar(ch))
        })
        sb.toString()
      }
      case EmptyStringTerm => ""
      case _ => null
    }
  }

  private val Placeholder_MixR = "IsaMixR.MixRNoVars"
  private val Placeholder_MixRVars = "IsaMixR.MixR"
  private val Placeholder_Vars = "IsaMixR.mixr_var"
  private val Type_PlaceholderVars = Type(Placeholder_Vars, List())
  private val Type_PlaceholderVars_List = List(Type_PlaceholderVars)
  private val IsaMixR_About = "IsaMixR.About"

  /**
   * If the term is a placeholder then this method extracts and returns it. If it is not a placeholder, it returns `null`.
   */
  def extractPlaceholder(t: Term): Placeholder = {
    t match {
      case App(Const(HOLTrueprop, Type(Fun, _)), term) => {
        extractPlaceholder(term)
      }
      case App(Const(Placeholder_MixR, Type(Fun, List(CharListType, BoolType))), payload) => {
        val payloadString = extractString(payload)
        val delimiterIndex = payloadString.indexOf(':')
        if (delimiterIndex < 0) return null
        new PlaceholderWithoutVars(payloadString.substring(0, delimiterIndex), payloadString.substring(delimiterIndex + 1))
      }
      case App(App(Const(Placeholder_MixRVars, Type(Fun, List(Type(List_list, Type_PlaceholderVars_List), Type(Fun, List(CharListType, BoolType))))), vars), payload) => {
        val payloadString = extractString(payload)
        val delimiterIndex = payloadString.indexOf(':')
        if (delimiterIndex < 0) return null
        val varList = extractPlaceholderVars(vars)
        PlaceholderWithVars(varList, payloadString.substring(0, delimiterIndex), payloadString.substring(delimiterIndex + 1))
      }
      case _ => null
    }
  }

  def extractPlaceholderVars(t: Term): java.util.List[FreeVar] = {
    t match {
      case App(App(Const(List_list_Cons, Type(Fun, List(Type_PlaceholderVars, Type(Fun, List(Type(List_list, Type_PlaceholderVars_List), Type(List_list, Type_PlaceholderVars_List)))))), first), other) => {
        val outVars = Buffer[FreeVar]()
        extractPlaceholderVarGroup(first, outVars)
        traverseListElements(other, t => {
          extractPlaceholderVarGroup(t, outVars)
        })
        JavaConversions.bufferAsJavaList(outVars)
      }
      case _ => throw new IllegalArgumentException("Not a valid list of placeholder variables.")
    }
  }

  def extractPlaceholderVarGroup(t: Term, outVars: Buffer[FreeVar]) = {
    t match {
      case App(Const(IsaMixR_About, Type(Fun, List(Type(List_list, List(_)), Type_PlaceholderVars))), vars) => {
        traverseListElements(vars, t => {t match {
          case Free(varName, varType) =>  {outVars += FreeVar(varName, varType);}
          case Const(varName, varType) =>  {outVars += FreeVar(varName, varType);}
          case tAny =>  throw new IllegalArgumentException("Not a valid group of placeholder variables. The offending term: " + tAny)
        }})
      }
      case _ => throw new IllegalArgumentException("Not a valid group of placeholder variables.")
    }
  }

  private val Fun = "fun"
  private val String_nibble = "String.nibble"
  private val String_char = "String.char"
  private val String_char_Char = "String.char.Char"
  private val List_list_Cons = "List.list.Cons"
  private val List_list_Nil = "List.list.Nil"
  private val List_list = "List.list"
  private val NibbleType = Type(String_nibble, List())
  private val CharType = Type(String_char, List())
  private val CharListType = Type(List_list, List(CharType))
  private val EmptyStringTerm = Const(List_list_Nil, CharListType)
  private val BoolType = Type(HOL_bool, List())
  private val NonEmptyStringTerm = Const(List_list_Cons, Type(Fun, List(CharType, Type(Fun, List(CharListType, CharListType)))))
  private val HOL_type = "HOL.type"
  private val HOL_type_List = List(HOL_type)
  private val TFree_a = TFree("'a", HOL_type_List)

  private def extractChar(t: Term): Char = {
    t match {
      case App(App(Const(String_char_Char, Type(Fun, List(NibbleType, Type(Fun, List(NibbleType, CharType))))), Const(n1, NibbleType)), Const(n2, NibbleType)) => extractCharFromNibbles(n1, n2)
      case _ => throw new IllegalArgumentException("Expected a character term. Got '%s'.".format(t))
    }
  }

  private def extractCharFromNibbles(n1: String, n2: String): Char = {
    val hexChar1 = n1.charAt(n1.length() - 1)
    val hexChar2 = n2.charAt(n2.length() - 1)
    ((hexCharToInt(hexChar1) << 4) + hexCharToInt(hexChar2)).toChar
  }

  private def hexCharToInt(hex: Char): Int = {
    if (hex >= '0' && hex <= '9')
      hex - '0'
    else if (hex >= 'A' && hex <= 'F')
      hex - 'A' + 10
    else
      throw new IllegalArgumentException("Not a hexadecimal character.")
  }
}
