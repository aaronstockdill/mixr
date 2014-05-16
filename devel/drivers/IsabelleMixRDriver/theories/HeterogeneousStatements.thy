theory HeterogeneousStatements
imports IsaMixR
begin





(* The Isabelle Driver parses the goal and converts it to a MixR HR goal. *)
lemma generic_goal: "\<lbrakk> P_1; P_2; P_3 \<rbrakk> \<Longrightarrow> C"
  oops

lemma multiple_goals: "\<lbrakk> P_1; P_2; P_3 \<rbrakk> \<Longrightarrow> C" and "\<lbrakk> P_1; P_2; P_3 \<rbrakk> \<Longrightarrow> C"
  oops





(* Individual goals can contain placeholders (foreign formulae).
   This example shows a premise with a TPTP formula in a placeholder. *)
lemma goal_with_placeholder: "\<lbrakk> A; MixRNoVars ''TPTP:fof(empty_is_sorted, axiom, sorted(nil)).''; P_3 \<rbrakk> \<Longrightarrow> C"
  oops


subsection {* PicProc *}

(* To reason about a foreign domain, we must introduce at least some of the
   constant concepts. Note that we do not have to define the 'nat' type. *)
typedecl PicProcImage
typedecl ShapeType
consts   AreaOf :: "PicProcImage \<Rightarrow> nat"
         ShapeOf :: "PicProcImage \<Rightarrow> ShapeType"
         Triangle :: "ShapeType" Square :: "ShapeType" Circle :: "ShapeType"
         ShapeA :: "PicProcImage"

lemma "MixRNoVars ''ImgUrl:/home/matej/Pictures/ShapeA.png'' \<Longrightarrow> ShapeOf ShapeA = Square"
  apply (mixrOracle "[| ShapeOf ShapeA = Square |] ==> ShapeOf ShapeA = Square")
  by (auto)

lemma "MixR [About [ShapeB]] ''ImgUrl:/home/matej/Pictures/ShapeA.png'' \<Longrightarrow> AreaOf ShapeB > 5000"
  apply (mixrOracle "[| AreaOf ShapeB = 5733 |] ==> 5000 < AreaOf ShapeB")
  by (auto)






(* Formal heterogeneous reasoning with Spider diagrams (Speedith) *)

lemma sd_proof:
  "(EX s. s : C - (A Un B) & A Int B Int C <= {s} & (A Int C) - B <= {s} & B - (A Un C) <= {s} & (B Int C) - A <= {s}) & (EX s1 s2. distinct[s1, s2] & s1 : -(C Un D) & s2 : (C - D) Un (C Int D) & C - D <= {s1, s2} & D - C <= {s1, s2}) --> (EX s s1. distinct[s, s1] & s : (-(B Un D)) Un (D - B) & s1 : (-(B Un D)) Un (B - D) & B Int D <= {s, s1})"
apply (mixr "(EX s. s : (C - (A Un B Un D)) Un ((C Int D) - (A Un B)) & (A Int B Int C) - D <= {s} & A Int B Int C Int D <= {s} & (A Int B Int D) - C <= {s} & (A Int C) - (B Un D) <= {s} & (A Int C Int D) - B <= {s} & (A Int D) - (B Un C) <= {s} & B - (A Un C Un D) <= {s} & (B Int C) - (A Un D) <= {s} & (B Int C Int D) - A <= {s} & (B Int D) - (A Un C) <= {s} & D - (A Un B Un C) <= {s}) & (EX s1 s2. distinct[s1, s2] & s1 : -(C Un D) & s2 : (C - D) Un (C Int D) & C - D <= {s1, s2} & D - C <= {s1, s2}) --> (EX s s1. distinct[s, s1] & s : (-(B Un D)) Un (D - B) & s1 : (-(B Un D)) Un (B - D) & B Int D <= {s, s1})")
  apply (mixr "(EX s. s : (C - (A Un B Un D)) Un ((C Int D) - (A Un B)) & (A Int B Int C) - D <= {s} & A Int B Int C Int D <= {s} & (A Int B Int D) - C <= {s} & (A Int C) - (B Un D) <= {s} & (A Int C Int D) - B <= {s} & (A Int D) - (B Un C) <= {s} & B - (A Un C Un D) <= {s} & (B Int C) - (A Un D) <= {s} & (B Int C Int D) - A <= {s} & (B Int D) - (A Un C) <= {s} & C - (A Un B Un D) <= {s} & D - (A Un B Un C) <= {s}) & (EX s1 s2. distinct[s1, s2] & s1 : -(C Un D) & s2 : (C - D) Un (C Int D) & C - D <= {s1, s2} & D - C <= {s1, s2}) --> (EX s s1. distinct[s, s1] & s : (-(B Un D)) Un (D - B) & s1 : (-(B Un D)) Un (B - D) & B Int D <= {s, s1})")
  apply (mixr "(EX s s1. distinct[s, s1] & s : (C - (A Un B Un D)) Un ((C Int D) - (A Un B)) & s1 : (-(A Un B Un C Un D)) Un (A - (B Un C Un D)) Un ((A Int B) - (C Un D)) & (A Int B Int C) - D <= {s, s1} & A Int B Int C Int D <= {s, s1} & (A Int B Int D) - C <= {s, s1} & (A Int C) - (B Un D) <= {s, s1} & (A Int C Int D) - B <= {s, s1} & (A Int D) - (B Un C) <= {s, s1} & B - (A Un C Un D) <= {s, s1} & (B Int C) - (A Un D) <= {s, s1} & (B Int C Int D) - A <= {s, s1} & (B Int D) - (A Un C) <= {s, s1} & C - (A Un B Un D) <= {s, s1} & D - (A Un B Un C) <= {s, s1}) & (EX s1 s2. distinct[s1, s2] & s1 : -(C Un D) & s2 : (C - D) Un (C Int D) & C - D <= {s1, s2} & D - C <= {s1, s2}) --> (EX s s1. distinct[s, s1] & s : (-(B Un D)) Un (D - B) & s1 : (-(B Un D)) Un (B - D) & B Int D <= {s, s1})")
  apply (mixr "(EX s s1. distinct[s, s1] & s : (C - (A Un B Un D)) Un ((C Int D) - (A Un B)) & s1 : (-(A Un B Un C Un D)) Un (A - (B Un C Un D)) Un ((A Int B) - (C Un D)) & (A Int B Int C) - D <= {s, s1} & A Int B Int C Int D <= {s, s1} & (A Int B Int D) - C <= {s, s1} & (A Int C) - (B Un D) <= {s, s1} & (A Int C Int D) - B <= {s, s1} & (A Int D) - (B Un C) <= {s, s1} & B - (A Un C Un D) <= {s, s1} & (B Int C) - (A Un D) <= {s, s1} & (B Int C Int D) - A <= {s, s1} & (B Int D) - (A Un C) <= {s, s1} & C - (A Un B Un D) <= {s, s1} & D - (A Un B Un C) <= {s, s1}) --> (EX s s1. distinct[s, s1] & s : (-(B Un D)) Un (D - B) & s1 : (-(B Un D)) Un (B - D) & B Int D <= {s, s1})")
  apply (mixr "(EX s s1. distinct[s, s1] & s : (-(A Un B Un D)) Un (D - (A Un B)) & s1 : (-(A Un B Un D)) Un (A - (B Un D)) Un ((A Int B) - D) & A Int B Int D <= {s, s1} & (A Int D) - B <= {s, s1} & B - (A Un D) <= {s, s1} & (B Int D) - A <= {s, s1}) --> (EX s s1. distinct[s, s1] & s : (-(B Un D)) Un (D - B) & s1 : (-(B Un D)) Un (B - D) & B Int D <= {s, s1})")
  apply (mixr "(EX s s1. distinct[s, s1] & s : (-(B Un D)) Un (D - B) & s1 : (-(B Un D)) Un (B - D) & B Int D <= {s, s1}) --> (EX s s1. distinct[s, s1] & s : (-(B Un D)) Un (D - B) & s1 : (-(B Un D)) Un (B - D) & B Int D <= {s, s1})")
  by(auto)

lemma hr_proof:
  "(\<exists>s1 s2. distinct[s1, s2] \<and> s1 \<in> A \<inter> B \<and> s2 \<in> (A - B) \<union> (B - A))
   \<longrightarrow> (\<exists>t1 t2. distinct[t1, t2] \<and> t1 \<in> A \<and> t2 \<in> B) \<and> A \<inter> B \<noteq> {}"
  apply(rule impI)
  apply(rule conjI)
  oops





lemma simple_sentence_complex_diagram:
  "\<exists>s1 s2 s3. distinct[s1, s2, s3] \<and> s1 \<in> A \<and> s2 \<in> B \<and> s3 \<in> C"
  oops





(* Placeholder caveat *)

axiomatization where
  ErrInference1: "MixRNoVars ''x is greater than y'' \<Longrightarrow> x > y" and
  OkayInference1: "MixR [About[x, y]] ''x is greater than y'' \<Longrightarrow> x > y"

lemma err1: "MixRNoVars ''x is greater than y'' \<Longrightarrow> (0::int) > 1"
  by(fast intro: ErrInference1)

lemma "MixRNoVars ''x is greater than y'' = False"
  apply(insert err1)
  by(fastforce)

lemma "MixR [About[x, y]] ''x is greater than y'' \<Longrightarrow> (0::int) > 1"
  apply(auto simp add: OkayInference1)
  oops

lemma "MixR [About[(0::int), 1]] ''x is greater than y'' \<Longrightarrow> (0::int) > 1"
  apply(insert OkayInference1 [of "0::int" "1::int"])
  by fast





(* Spider Diagram translation test. *)
lemma test_sentential_simplification:
  "(\<exists>s1 s2. distinct[s1, s2] \<and> s1 \<in> A \<inter> B \<and> s2 \<in> (A - B) \<union> (B - A)) \<longrightarrow> (\<exists>t1 t2. distinct[t1, t2] \<and> t1 \<in> A \<and> t2 \<in> B) \<and> (A \<inter> B) \<noteq> {}"
  apply(auto)
  oops

(* Spider Diagram Shaded zones translation test. *)
lemma test_shaded_zones: "(\<exists>s1 s2. distinct[s1, s2] \<and> s1 \<in> A \<inter> B \<and> s2 \<in> (A - B) \<union> (B - A) \<and> (A - B) \<union> (B - A) \<subseteq> {s1, s2}) \<longrightarrow> (\<exists>t1 t2. distinct[t1, t2] \<and> t1 \<in> A \<and> t2 \<in> B \<and> (A \<inter> B) \<subseteq> {t1, t2})"
  apply(auto)
  oops

(* Unitary diagram with a single spider. *)
lemma test_single_spider_unitary: "\<exists>s. s \<in> B \<and> s \<in> (A - B) \<union> (B - A) \<and> (A - B) \<union> (B - A) \<subseteq> {s}"
  apply(auto)
  oops

(* Compound diagram with a single-spider unitary diagram. *)
lemma test_single_spider_compound: "(\<exists>s. s \<in> B \<and> s \<in> (A - B) \<union> (B - A) \<and> (A - B) \<union> (B - A) \<subseteq> {s}) \<longrightarrow> (\<exists>t1 t2. distinct[t1, t2] \<and> t1 \<in> A \<and> t2 \<in> B \<and> (A \<inter> B) \<subseteq> {t1, t2})"
  apply(auto)
  oops

(* Unitary spider diagram with two spiders. *)
lemma test_two_spiders_unitary: "(\<exists>s1 s2. distinct[s1, s2] \<and> s1 \<in> A \<inter> B \<and> s2 \<in> (A - B) \<union> (B - A) \<and> (A - B) \<union> (B - A) \<subseteq> {s1, s2})"
  apply(auto)
  oops

lemma shaded_zone_test_complex: "\<exists>s1 s2. s1 \<noteq> s2 \<and> s1 \<in> A - B \<and> s2 \<in> C - D \<and> (A - ((B \<union> C) \<inter> D)) \<subseteq> {s1, s2}"
  oops

lemma speedith_fig7_d1: "\<exists>s. s \<in> C - (A \<union> B) \<and> (A \<inter> C) \<union> (B \<inter> C) \<union> (B - A) \<subseteq> {s}"
  apply(auto)
  oops

lemma speedith_fig7_d2: "\<exists>s1 s2. s1 \<noteq> s2 \<and> s1 \<notin> C \<union> D \<and> s2 \<in> C \<and> (C - D) \<union> (D - C) \<subseteq> {s1, s2}"
  apply(auto)
  oops

lemma speedith_fig7_goal: "\<exists>s1 s. s1 \<noteq> s \<and> s1 \<notin> D \<and> s \<notin> B \<and> (B \<inter> D) \<subseteq> {s1, s}"
  oops

lemma speedith_fig7_compound1: "(\<exists>s. s \<in> C - (A \<union> B) \<and> (A \<inter> C) \<union> (B \<inter> C) \<union> (B - A) \<subseteq> {s}) \<and> (\<exists>s1 s2. s1 \<noteq> s2 \<and> s1 \<notin> C \<union> D \<and> s2 \<in> C \<and> (C - D) \<union> (D - C) \<subseteq> {s1, s2})"
  apply(auto)
  oops

lemma speedith_fig7: "(EX s. s : C - (A Un B) & A Int B Int C <= {s} & (A Int C) - B <= {s} & B - (A Un C) <= {s} & (B Int C) - A <= {s}) & (EX s1 s2. distinct[s1, s2] & s1 : -(C Un D) & s2 : (C - D) Un (C Int D) & C - D <= {s1, s2} & D - C <= {s1, s2}) --> (EX s s1. distinct[s, s1] & s : (-(B Un D)) Un (D - B) & s1 : (-(B Un D)) Un (B - D) & B Int D <= {s, s1})"
  apply (mixr "(EX s. s : (C - (A Un B Un D)) Un ((C Int D) - (A Un B)) & (A Int B Int C) - D <= {s} & A Int B Int C Int D <= {s} & (A Int B Int D) - C <= {s} & (A Int C) - (B Un D) <= {s} & (A Int C Int D) - B <= {s} & (A Int D) - (B Un C) <= {s} & B - (A Un C Un D) <= {s} & (B Int C) - (A Un D) <= {s} & (B Int C Int D) - A <= {s} & (B Int D) - (A Un C) <= {s} & D - (A Un B Un C) <= {s}) & (EX s1 s2. distinct[s1, s2] & s1 : -(C Un D) & s2 : (C - D) Un (C Int D) & C - D <= {s1, s2} & D - C <= {s1, s2}) --> (EX s s1. distinct[s, s1] & s : (-(B Un D)) Un (D - B) & s1 : (-(B Un D)) Un (B - D) & B Int D <= {s, s1})")
  apply (mixr "(EX s s1. distinct[s, s1] & s : (C - (A Un B Un D)) Un ((C Int D) - (A Un B)) & s1 : (-(A Un B Un C Un D)) Un (A - (B Un C Un D)) Un ((A Int B) - (C Un D)) & (A Int B Int C) - D <= {s, s1} & A Int B Int C Int D <= {s, s1} & (A Int B Int D) - C <= {s, s1} & (A Int C) - (B Un D) <= {s, s1} & (A Int C Int D) - B <= {s, s1} & (A Int D) - (B Un C) <= {s, s1} & B - (A Un C Un D) <= {s, s1} & (B Int C) - (A Un D) <= {s, s1} & (B Int C Int D) - A <= {s, s1} & (B Int D) - (A Un C) <= {s, s1} & D - (A Un B Un C) <= {s, s1}) & (EX s1 s2. distinct[s1, s2] & s1 : -(C Un D) & s2 : (C - D) Un (C Int D) & C - D <= {s1, s2} & D - C <= {s1, s2}) --> (EX s s1. distinct[s, s1] & s : (-(B Un D)) Un (D - B) & s1 : (-(B Un D)) Un (B - D) & B Int D <= {s, s1})")
  apply (mixr "(EX s s1. distinct[s, s1] & s : (C - (A Un B Un D)) Un ((C Int D) - (A Un B)) & s1 : (-(A Un B Un C Un D)) Un (A - (B Un C Un D)) Un ((A Int B) - (C Un D)) & (A Int B Int C) - D <= {s, s1} & A Int B Int C Int D <= {s, s1} & (A Int B Int D) - C <= {s, s1} & (A Int C) - (B Un D) <= {s, s1} & (A Int C Int D) - B <= {s, s1} & (A Int D) - (B Un C) <= {s, s1} & B - (A Un C Un D) <= {s, s1} & (B Int C) - (A Un D) <= {s, s1} & (B Int C Int D) - A <= {s, s1} & (B Int D) - (A Un C) <= {s, s1} & C - (A Un B Un D) <= {s, s1} & D - (A Un B Un C) <= {s, s1}) & (EX s1 s2. distinct[s1, s2] & s1 : -(C Un D) & s2 : (C - D) Un (C Int D) & C - D <= {s1, s2} & D - C <= {s1, s2}) --> (EX s s1. distinct[s, s1] & s : (-(B Un D)) Un (D - B) & s1 : (-(B Un D)) Un (B - D) & B Int D <= {s, s1})")
  apply (mixr "(EX s s1. distinct[s, s1] &       s : (C - (A Un B Un D)) Un ((C Int D) - (A Un B)) & s1 : (-(A Un B Un C Un D)) Un (A - (B Un C Un D)) Un ((A Int B) - (C Un D)) & (A Int B Int C) - D <= {s, s1} & A Int B Int C Int D <= {s, s1} & (A Int B Int D) - C <= {s, s1} & (A Int C) - (B Un D) <= {s, s1} & (A Int C Int D) - B <= {s, s1} & (A Int D) - (B Un C) <= {s, s1} & B - (A Un C Un D) <= {s, s1} & (B Int C) - (A Un D) <= {s, s1} & (B Int C Int D) - A <= {s, s1} & (B Int D) - (A Un C) <= {s, s1} & C - (A Un B Un D) <= {s, s1} & D - (A Un B Un C) <= {s, s1}) --> (EX s s1. distinct[s, s1] & s : (-(B Un D)) Un (D - B) & s1 : (-(B Un D)) Un (B - D) & B Int D <= {s, s1})")
  apply (mixr "(EX s s1. distinct[s, s1] & s : (-(B Un D)) Un (D - B) & s1 : (-(B Un D)) Un (B - D) & B Int D <= {s, s1}) --> (EX s s1. distinct[s, s1] & s : (-(B Un D)) Un (D - B) & s1 : (-(B Un D)) Un (B - D) & B Int D <= {s, s1})")
  by auto


lemma speedith_fig1_proof_with_sentential_help:
  "\<lbrakk> \<exists>t1 t2. distinct[t1, t2] \<and> t1 \<in> A \<inter> B \<and> t2 \<in> (A - B) \<union> (B - A) \<rbrakk>
   \<Longrightarrow> (\<exists>u1 u2. distinct[u1, u2] \<and> u1 \<in> A \<and> u2 \<in> B)"
  apply(auto)
  apply (mixr "(EX t1 t2. distinct[t1, t2] & t1 : (A Int B) Un (B - A) & t2 : A - B) --> (EX u1 u2. distinct[u1, u2] & u1 : (A - B) Un (A Int B) & u2 : (A Int B) Un (B - A))")
  apply (mixr "(EX t1 t2. distinct[t1, t2] & t1 : (A Int B) Un (B - A) & t2 : (A - B) Un (A Int B)) --> (EX u1 u2. distinct[u1, u2] & u1 : (A - B) Un (A Int B) & u2 : (A Int B) Un (B - A))")
  apply (mixr "True")
  by (auto)


lemma example_simple_sentential_complex_diagrammatic: "\<exists>s1 s2 s3. distinct[s1, s2, s3] \<and> s1 \<in> A \<and> s2 \<in> B \<and> s3 \<in> C"
  oops



(* Spider Diagram translation test. *)
lemma example_both_sentential_and_diagrammatic_complex:
  "(\<exists>s1 s2 s3. s1 \<noteq> s2 \<and> s1 \<noteq> s3 \<and> s2 \<noteq> s3
   \<and> s1 \<in> A \<and> s1 \<in> B \<union> -C \<and> s1 \<notin> D
   \<and> s3 \<in> (B \<inter> C) - (A \<union> D)
   \<and> s2 \<in> D \<and> s2 \<in> A)
   \<longrightarrow> (\<exists>t1 t2. distinct[t1, t2] \<and> t1 \<in> A \<and> t2 \<in> B)"
  by(auto)

lemma test_diagram_with_distinct: "(\<exists>s1 s2 s3. distinct[s1, s2, s3] \<and> s1 \<in> A \<and> s1 \<in> B \<union> -C \<and> s1 \<notin> D \<and> s3 \<in> (B \<inter> C) - (A \<union> D) \<and> s2 \<in> D \<and> s2 \<in> A) \<longrightarrow> (\<exists>t1 t2. distinct[t1, t2] \<and> t1 \<in> A \<and> t2 \<in> B)"
  by(auto)

(* Spider Diagram translation test. *)
lemma speedith_fig1_proof_with_unknown_sentential_fragment:
  "(\<exists>s1 s2. distinct[s1, s2] \<and> s1 \<in> A \<inter> B \<and> s2 \<in> (A - B) \<union> (B - A))
   \<longrightarrow> (\<exists>t1 t2. distinct[t1, t2] \<and> t1 \<in> A \<and> t2 \<in> B) \<and> A \<inter> B \<noteq> {}"
  apply(rule impI)
  apply(rule conjI)
  apply (mixr "(EX s1 s2. distinct[s1, s2] & s1 : A Int B & s2 : (A - B) Un (B - A)) --> (EX t1 t2. distinct[t1, t2] & t1 : (A - B) Un (A Int B) & t2 : (A Int B) Un (B - A))")
  apply (mixr "(EX s1 s2. distinct[s1, s2] & s1 : A Int B & s2 : A - B) | (EX s1 s2. distinct[s1, s2] & s1 : A Int B & s2 : B - A) --> (EX t1 t2. distinct[t1, t2] & t1 : (A - B) Un (A Int B) & t2 : (A Int B) Un (B - A))")
  apply (mixr "(EX s1 s2. distinct[s1, s2] & s1 : A Int B & s2 : (A - B) Un (A Int B)) | (EX s1 s2. distinct[s1, s2] & s1 : A Int B & s2 : B - A) --> (EX t1 t2. distinct[t1, t2] & t1 : (A - B) Un (A Int B) & t2 : (A Int B) Un (B - A))")
  apply (mixr "(EX s1 s2. distinct[s1, s2] & s1 : (A Int B) Un (B - A) & s2 : (A - B) Un (A Int B)) | (EX s1 s2. distinct[s1, s2] & s1 : A Int B & s2 : B - A) --> (EX t1 t2. distinct[t1, t2] & t1 : (A - B) Un (A Int B) & t2 : (A Int B) Un (B - A))")
  apply (mixr "(EX s1 s2. distinct[s1, s2] & s1 : (A Int B) Un (B - A) & s2 : (A - B) Un (A Int B)) | (EX s1 s2. distinct[s1, s2] & s1 : (A - B) Un (A Int B) & s2 : B - A) --> (EX t1 t2. distinct[t1, t2] & t1 : (A - B) Un (A Int B) & t2 : (A Int B) Un (B - A))")
  apply (mixr "(EX s1 s2. distinct[s1, s2] & s1 : (A Int B) Un (B - A) & s2 : (A - B) Un (A Int B)) | (EX s1 s2. distinct[s1, s2] & s1 : (A - B) Un (A Int B) & s2 : (A Int B) Un (B - A)) --> (EX t1 t2. distinct[t1, t2] & t1 : (A - B) Un (A Int B) & t2 : (A Int B) Un (B - A))")
  apply (mixr "(EX s1 s2. distinct[s1, s2] & s1 : (A - B) Un (A Int B) & s2 : (A Int B) Un (B - A)) --> (EX t1 t2. distinct[t1, t2] & t1 : (A - B) Un (A Int B) & t2 : (A Int B) Un (B - A))")
  by auto




section {* Verification of MixR's proof concepts *}
(* ================== MixR Heterogeneous Proof Verification ================== *)

(* Lemma 1: If we have formula A' that is entailed by a premise A, and B'
  entails the conclusion B, then by proving A' \<Longrightarrow> B', we also prove A \<Longrightarrow> B. *)
lemma assumes en1: "A \<Longrightarrow> A'" and en2: "B' \<Longrightarrow> B" and new: "A' \<Longrightarrow> B'"
      shows old: "A \<Longrightarrow> B"
proof -
  assume a: "A"
  show ?thesis using assms a
    by (fast)
qed

(* Lemma 2: If we have a goal G to prove, and we know that G' \<Longrightarrow> G, can we
    then prove G by just proving G'? *)
lemma assumes en1: "G' \<Longrightarrow> G" and new: "G'"
      shows "G"
  by (fast intro: en1 new)

(* Lemma 3: A backwards proof can be applied on a conclusion in HOL if it is a
    conjunction. *)
lemma assumes p1: "B' \<Longrightarrow> B" and p2: "A \<Longrightarrow> B' \<and> C"
  shows "A \<Longrightarrow> B \<and> C"
  by (metis p1 p2)

(* Lemma 4: A backwards proof can be applied on a conclusion in HOL if it is a
    disjunction. *)
lemma assumes p1: "B' \<Longrightarrow> B" and p2: "A \<Longrightarrow> B' \<or> C"
  shows "A \<Longrightarrow> B \<or> C"
  by (metis p1 p2)


typedecl person
consts
  Ann :: person
  Bob :: person
  ParentOf :: "person \<Rightarrow> person \<Rightarrow> bool"

axiomatization where
  Relation1: "ParentOf Ann Bob"


lemma "MixRNoVars ''NatLang: Ann is a child of Bob.''"
  apply (mixrOracle "MixRNoVars ''NatLang: Bob is a parent of Ann.''")
  apply (mixrOracle "ParentOf Ann Bob")
  by (simp add: Relation1)


lemma withoutLeadingSpace: "MixRNoVars ''NatLang:Ann is a child of Bob.''"
  apply (mixrOracle "MixRNoVars ''NatLang: Bob is a parent of Ann.''")
  apply (mixrOracle "ParentOf Ann Bob")
  by (simp add: Relation1)


lemma "MixR [About[Ann, Bob]] ''NatLang: Ann is a child of Bob.''"
  apply (mixrOracle "ParentOf Ann Bob")
  oops







lemma "MixRNoVars ''TPTP:fof(empty_is_sorted, axiom, sorted(nil)).''"
  oops

subsection {* Example 1 *}

text {* Typically, placeholders will need some surrounding theory (like
constants, functions, relations etc.) which the external reasoner of the placeholder
talks about. Without properly connecting the content of the placeholder with the
logic and theory of the hosting reasoner, confusions and invalid inferences might
occur. We demonstrate some of these problems problems and also provide solutions: *}

text {* One problem arises when the placeholder is talking about constants which are in fact
treated as free variables and are thus universally quantified. The example below
demonstrates the problem. It shows an inference step, which misleads us to believe
that the natural language payload is talking about ``Child of'' and ``Parent of''
relations between two persons. Particularly, it says that if Ann is a child of Bob,
then they are in the Isabelle/HOL relation @{text "Child Ann Bob"}. However, the predicate
symbol @{text "Child"} is not a constant, but a free variable. It is thus universally
quantified, which means that the predicate symbol @{text "Child"} is merely a name that
talks about all relations (and not a particular relation, which we might intuitively
expect). *}
axiomatization where
  Inference1: "MixR [About[Ann, Bob]] ''Ann is a child of Bob.'' \<Longrightarrow> Child Ann Bob"

text {* Given the above inference step, let us try to prove a lemma that exposes the problem.
The lemma merely changes the name of the predicate @{text "Child"} into @{text "Parent"}. The proof
succeeds, as the substitution of @{text "Child"} into @{text "Parent"} yields a unification with the
@{text "Inference1"} axiom and thus produces a ``valid'' proof. *}
lemma "MixR[About[Ann, Bob]] ''Ann is a child of Bob.'' \<Longrightarrow> Parent Ann Bob"
  by(simp add: Inference1)

(** THE SOLUTION **)
text {* Let us define a constant, which will prevent Isabelle to treat references to @{text "Child"} as
a free variables: *}
consts Child :: "'a \<Rightarrow> 'a \<Rightarrow> bool"
text {* Again, we simulate the inference step that would be otherwise provided by an external reasoner: *}
axiomatization where
  Inference2: "MixR[About[Ann, Bob]] ''Ann is a child of Bob.'' \<Longrightarrow> Child Ann Bob"

text {* Now the following become unprovable (as expected): *}
lemma "MixR[About[Ann, Bob]] ''Ann is a child of Bob.'' \<Longrightarrow> Parent Ann Bob"
  oops

text {* Additionally, we may provide another constant @{text "Parent"} and define it in terms of @{text "Child"}: *}
consts Parent :: "'a \<Rightarrow> 'a \<Rightarrow> bool"
defs Parent_def: "Parent x y \<equiv> Child y x"

text {* After this, we can perform the desired reasoning: *}
lemma "MixR[About[Ann, Bob]] ''Ann is a child of Bob.'' \<Longrightarrow> Parent Bob Ann"
  by(simp add: Inference2 Parent_def)

text {* Furthermore, the following is still not provable: *}
lemma "MixR[About[Ann, Bob]] ''Ann is a child of Bob.'' \<Longrightarrow> Parent Ann Bob"
  oops

subsection {* Example 2: Theory without referenced variables *}
text {* A natural language example. Over the sets of humans. *}
locale HumanParents =
  fixes Humans :: "'a set" and
  Owner :: "'a \<Rightarrow> 'b \<Rightarrow> bool" and
  Dogs :: "'a set"
  assumes Inference3: "MixRNoVars ''NatLang: Every human has a parent.'' \<Longrightarrow> \<forall>h \<in> Humans. (\<exists>p \<in> Humans. Parent p h)"
begin
lemma "MixRNoVars ''NatLang: Every human has a parent.''
       \<and> h \<in> Humans
       \<longrightarrow> (\<exists>p \<in> Humans. Parent p h)"
  by (auto simp add: Inference3)
end

subsection {* Example 3: Referenced variables without a theory *}

text {* Similar example without a surrounding theory, only referenced variables: *}
axiomatization where
  Inference4: "MixR [About[Humans, Mortal]] ''NatLang: All humans are mortal'' \<Longrightarrow> \<forall>h \<in> Humans. h \<in> Mortal" and
  Inference5: "MixR [About[Greeks, Humans]] ''NatLang: All Greeks are human.'' \<Longrightarrow> \<forall>g \<in> Greeks. g \<in> Humans"

text {* As expected, we can prove lemmata of the following form:  *}
lemma "MixR [About[Humans, Mortal]] ''NatLang: All humans are mortal''
       \<and> MixR [About[Greeks, Humans]] ''NatLang: All Greeks are human.''
       \<and> g \<in> Greeks
       \<longrightarrow> g \<in> Mortal"
  apply(rule impI)
  apply(erule conjE)+
  apply(drule Inference4 Inference5)+
  by(auto)

text {* Note, however, that the predicates @{text "Humans"}, @{text "Mortal"}, and @{text "Greeks"} are
again free variables. Therefore, thay can be exchanged with any other predicate symbols: *}
lemma "MixR [About[Mortal, Greeks]] ''NatLang: All humans are mortal''
       \<and> MixR [About[Humans, Mortal]] ''NatLang: All Greeks are human.''
       \<and> h \<in> Humans
       \<longrightarrow> h \<in> Greeks"
  apply(rule impI)
  apply(erule conjE)+
  apply(drule Inference4 Inference5)+
  by(auto)

text {* The above statement is true because we @{text "Inference4"}
and @{text "Inference5"} are merely schematic axioms which establishes
a relation between three variables---regardless of what their names are. *}




section {* Placeholders---caveats  *}

(*axiomatization where
  ErrInference1: "MixRNoVars ''x is greater than y'' \<Longrightarrow> x > y" and
  OkayInference1: "MixR [About[x, y]] ''x is greater than y'' \<Longrightarrow> x > y"

lemma err1: "MixRNoVars ''x is greater than y'' \<Longrightarrow> (0::int) > 1"
  by(fast intro: ErrInference1)

lemma "MixRNoVars ''x is greater than y'' = False"
  apply(insert err1)
  by(fastforce)

lemma "MixR [About[x, y]] ''x is greater than y'' \<Longrightarrow> (0::int) > 1"
  apply(auto simp add: OkayInference1)
  oops

lemma "MixR [About[(0::int), 1]] ''x is greater than y'' \<Longrightarrow> (0::int) > 1"
  apply(insert OkayInference1 [of "0::int" "1::int"])
  by fast*)




section {* Backward and forward reasoning in MixR *}

lemma backward_step:
      assumes premise: "\<lbrakk>A; B'\<rbrakk> \<Longrightarrow> B" and rule: "B' \<Longrightarrow> B" and rest: "A \<Longrightarrow> B'"
      shows concl:     "A \<Longrightarrow> B"
proof -
  assume a: "A"
  show ?thesis using a premise rule rest
    by auto
qed



end