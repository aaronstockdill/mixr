theory "dummy" 

imports 
  Main
  IsaMixR
begin 

(* Don't forget to activate Z3 by adding
Z3_NON_COMMERCIAL=yes

to
$HOME/.isabelle/Isabelle2013-2/etc/settings

*)

(* SMT works after getting rid of sets, somehow I would have hoped it could handle sets better.
   SMT works well and is fast, but only available for non-commerical use *)
lemma problematic: "(EX s s1. distinct[s, s1] & s : (C - (A Un B Un D)) Un ((C Int D) - (A Un B)) & s1 : (-(A Un B Un C Un D)) Un (A - (B Un C Un D)) Un ((A Int B) - (C Un D)) & (A Int B Int C) - D <= {s, s1} & A Int B Int C Int D <= {s, s1} & (A Int B Int D) - C <= {s, s1} & (A Int C) - (B Un D) <= {s, s1} & (A Int C Int D) - B <= {s, s1} & (A Int D) - (B Un C) <= {s, s1} & B - (A Un C Un D) <= {s, s1} & (B Int C) - (A Un D) <= {s, s1} & (B Int C Int D) - A <= {s, s1} & (B Int D) - (A Un C) <= {s, s1} & C - (A Un B Un D) <= {s, s1} & D - (A Un B Un C) <= {s, s1}) & (EX s1 s2. distinct[s1, s2] & s1 : -(C Un D) & s2 : (C - D) Un (C Int D) & C - D <= {s1, s2} & D - C <= {s1, s2}) --> (EX s s1. distinct[s, s1] & s : (-(B Un D)) Un (D - B) & s1 : (-(B Un D)) Un (B - D) & B Int D <= {s, s1})
                    \<Longrightarrow>
                    (EX s s1. distinct[s, s1] & s : (C - (A Un B Un D)) Un ((C Int D) - (A Un B)) & s1 : (-(A Un B Un C Un D)) Un (A - (B Un C Un D)) Un ((A Int B) - (C Un D)) & (A Int B Int C) - D <= {s, s1} & A Int B Int C Int D <= {s, s1} & (A Int B Int D) - C <= {s, s1} & (A Int C) - (B Un D) <= {s, s1} & (A Int C Int D) - B <= {s, s1} & (A Int D) - (B Un C) <= {s, s1} & B - (A Un C Un D) <= {s, s1} & (B Int C) - (A Un D) <= {s, s1} & (B Int C Int D) - A <= {s, s1} & (B Int D) - (A Un C) <= {s, s1} & D - (A Un B Un C) <= {s, s1}) & (EX s1 s2. distinct[s1, s2] & s1 : -(C Un D) & s2 : (C - D) Un (C Int D) & C - D <= {s1, s2} & D - C <= {s1, s2}) --> (EX s s1. distinct[s, s1] & s : (-(B Un D)) Un (D - B) & s1 : (-(B Un D)) Un (B - D) & B Int D <= {s, s1})"
  apply (simp add: subset_iff)
  apply smt
done

(* Metis works as well, but is much slower and I expect much less reliable. It needs some
   cleaning up before it can tackle even this simple goal. *)
lemma problematic2: "(EX s s1. distinct[s, s1] & s : (C - (A Un B Un D)) Un ((C Int D) - (A Un B)) & s1 : (-(A Un B Un C Un D)) Un (A - (B Un C Un D)) Un ((A Int B) - (C Un D)) & (A Int B Int C) - D <= {s, s1} & A Int B Int C Int D <= {s, s1} & (A Int B Int D) - C <= {s, s1} & (A Int C) - (B Un D) <= {s, s1} & (A Int C Int D) - B <= {s, s1} & (A Int D) - (B Un C) <= {s, s1} & B - (A Un C Un D) <= {s, s1} & (B Int C) - (A Un D) <= {s, s1} & (B Int C Int D) - A <= {s, s1} & (B Int D) - (A Un C) <= {s, s1} & C - (A Un B Un D) <= {s, s1} & D - (A Un B Un C) <= {s, s1}) & (EX s1 s2. distinct[s1, s2] & s1 : -(C Un D) & s2 : (C - D) Un (C Int D) & C - D <= {s1, s2} & D - C <= {s1, s2}) --> (EX s s1. distinct[s, s1] & s : (-(B Un D)) Un (D - B) & s1 : (-(B Un D)) Un (B - D) & B Int D <= {s, s1})
                    \<Longrightarrow>
                    (EX s s1. distinct[s, s1] & s : (C - (A Un B Un D)) Un ((C Int D) - (A Un B)) & s1 : (-(A Un B Un C Un D)) Un (A - (B Un C Un D)) Un ((A Int B) - (C Un D)) & (A Int B Int C) - D <= {s, s1} & A Int B Int C Int D <= {s, s1} & (A Int B Int D) - C <= {s, s1} & (A Int C) - (B Un D) <= {s, s1} & (A Int C Int D) - B <= {s, s1} & (A Int D) - (B Un C) <= {s, s1} & B - (A Un C Un D) <= {s, s1} & (B Int C) - (A Un D) <= {s, s1} & (B Int C Int D) - A <= {s, s1} & (B Int D) - (A Un C) <= {s, s1} & D - (A Un B Un C) <= {s, s1}) & (EX s1 s2. distinct[s1, s2] & s1 : -(C Un D) & s2 : (C - D) Un (C Int D) & C - D <= {s1, s2} & D - C <= {s1, s2}) --> (EX s s1. distinct[s, s1] & s : (-(B Un D)) Un (D - B) & s1 : (-(B Un D)) Un (B - D) & B Int D <= {s, s1})"
apply (simp add: subset_iff)
apply (clarify)
apply metis
done

end
