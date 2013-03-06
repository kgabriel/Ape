%%%% Utility: Generic Net Creation

% map_to_all(+List,+Functor,+Args,-Result)
% using Functor/3, Result is obtained by applying Functor to
% all elements of List with additional arguments Args
% in the form F(ListElement,Args,Result)
map_to_all([],_,_,[]).
map_to_all([L|List],Fun,Args,[R|Result]) :- 
  F =.. [Fun,L,Args,R], 
  F, 
  map_to_all(List,Fun,Args,Result).

% pair(?A, ?B, ?AB)
% true if AB is a pair of [A,B]
pair(A, B, [A,B]).

% assert elements of a net:
% - places with types
% - transitions
% - variables with types
% - pre and post conditions (only variables as inscriptions)
% - firing conditions as single (logical) terms
assert_place([[P,Type],Net]) :- 
  assertz(net_place(P,Type,Net)).
assert_transition([T,Net]) :- 
  assertz(net_transition(T,Net)).
assert_var([[V,Type],Net]) :-
  assertz(net_var(V,Type,Net)).
assert_pre([[T,V,P],Net]) :-
  assertz(net_pre(T,V,P,Net)).
assert_post([[T,V,P],Net]) :-
  assertz(net_post(T,V,P,Net)).
assert_cond([[T,C],Net]) :-
  assertz(net_cond(T,C,Net)).
  

% create_net(+Net, +Places, +Transitions, +Vars, +Pre, +Post, +Cond)
% creates a net that can be referenced by Net, asserting all
% specified net parts
% - places are given in the form [Place,Type]
% - transitions are just identifiers
% - variables are given in the form [Var,Type]
% - pre and post conditions are given in the form [Transition,Variable,Place]
% - firing conditions are given in the form [Transition,Condition]
create_net(Net, PlacesWithTypes, Transitions,VarsWithTypes, Pre, Post, Cond) :-
  map_to_all(PlacesWithTypes,pair,Net,NetPlaces),
  retractall(net_place(_,_,Net)),
  maplist(assert_place,NetPlaces),
  map_to_all(Transitions,pair,Net,NetTransitions),
  retractall(net_transition(_,Net)) ,
  maplist(assert_transition,NetTransitions),
  map_to_all(VarsWithTypes,pair,Net,NetVars),
  retractall(net_var(_,_,Net)) ,
  maplist(assert_var,NetVars),
  map_to_all(Pre,pair,Net,NetPre),
  retractall(net_pre(_,_,_,Net)),
  maplist(assert_pre,NetPre),
  map_to_all(Post,pair,Net,NetPost),
  retractall(net_post(_,_,_,Net)) ,
  maplist(assert_post,NetPost),
  map_to_all(Cond,pair,Net,NetCond),
  retractall(net_cond(_,_,Net)) ,
  maplist(assert_cond,NetCond),
  !.


%%%% Variables, Assignments, Satisfaction

% transition_vars(+Transition, +Net, ?VarsWithType)
% true if VarsWithTypes = Var(Transition) in Net
transition_vars(T,Net,Vars) :-
  net_transition(T,Net),
  findall([V,Type],(net_pre(T,V,_,Net),net_var(V,Type,Net)),PreVars),
  findall([V,Type],(net_post(T,V,_,Net),net_var(V,Type,Net)),PostVars),
  findall([V,Type],
    (net_cond(T,C,Net), C =.. CList, member(V,CList),net_var(V,Type,Net)),
    CondVars),
  union(PreVars,PostVars,PrePostVars),
  union(PrePostVars,CondVars,VarsList),
  list_to_set(VarsList,Vars).

% transition_conditions(+Transition, +Net, ?Conditions)
% true if Conditions is the set (list) of all firing conditions
% of Transition in Net
transition_conditions(T, N, C) :-
  findall(Condition, net_cond(T,Condition,N), C).

% var_asignment(+VarsWithType, ?VarAssignment)
% true if VarAssignment is an assignment for all
% variables in VarsWithType
%
% Variables in the assignment have to be in the same
% order as in the variable set!
var_assignment([],[]).
var_assignment([[V,_]|Vars],[[V,_]|Ass]) :-
  var_assignment(Vars,Ass).

type_conform_assignment([],[]).
type_conform_assignment([[V,Type]|Vars],[[V,A]|Ass]) :-
  Type_Conform =.. [Type,A],
  Type_Conform,
  type_conform_assignment(Vars,Ass).
  
% satisfies_conditions(+VarAssignment, +Conditions)
% true if VarAssignment satisfies all Conditions;
% if for a pair [Var,Value] in VarAssignment, the
% logical variable Value is unassigned, it will be
% assigned to a suitable value if possible
satisfies_conditions(Ass,Cond) :-
  condition_instances(Ass,Cond),
  maplist(call,Cond).
  
% condition_instances(+VarAssignment,+Conditions)
% instantiates all conditions according to the assignment,
% i.e. for all [Var,Value]-pairs in VarAssignment,
% all occurrences of (atom) Var in any condition are replaced
% by Value (which can be an unassigned logical variable)
condition_instances(_,[]).
condition_instances(Ass, [C|Cond]) :-
  condition_instance(Ass,C),
  condition_instances(Ass,Cond).

% condition_instance(+VarAssignment, +Condition)
% instantiates the condition according to the assignment,
% i.e. for all [Var,Value]-pairs in VarAssignment,
% all occurrences of (atom) Var in the condition are replaced
% by Value (which can be an unassigned logical variable)
condition_instance(Ass,C) :-
  find_var_positions(Ass,C,Positions),
  replace_vars(Ass,C,Positions).
  %write(Positions), nl, write(C), nl.
  
% find_var_positions(+VarAssignment, +Condition, ?Positions)
% true if the list Positions contains the positions of
% occurrences of variables in VarAssignment in the condition,
% in corresponding order as the assignment
find_var_positions([],_,[]).
find_var_positions([[Var,_]|Ass],C,[Pos|Positions]) :-
  findall(P, arg(P, C, Var), Pos),
  find_var_positions(Ass,C,Positions).  

% replace_vars(+VarAssignment, +Condition, +Positions)
% for all [Var,Value]-pair in VarAssignment and 
% all Pos-Set at the same index in Positions,
% for all P in Pos, the P-th argument of Condition
% is replaced by Value 
replace_vars([],_,[]).
replace_vars([[_,Value]|Ass],C,[P|Positions]) :-
  replace_var_at(P,C,Value),
  replace_vars(Ass,C,Positions).
  
% replae_var_at(+Positions,+Condition,+Value)
% for all P in Positions, the P-th argument of
% Condition is replaced by Value
replace_var_at([],_,_).
replace_var_at([P|Pos], C, Value) :-
  setarg(P,C,Value),
  replace_var_at(Pos,C,Value).

% consistent_assignment(+Transition,+Net,?Ass)
% true if Ass is a consistent variable assignment
% for Transition in Net;
% this is the case if Ass is an assignment
% of Var(Transition), satisfying the firing
% conditions of Transition
consistent_assignment(Trans,Net,Ass) :-
  transition_conditions(Trans, Net, Cond),
  transition_vars(Trans,Net,Vars),
  var_assignment(Vars,Ass),
  satisfies_conditions(Ass,Cond).



%%%% Variables, Assignments, Satisfaction of whole process

% disjoint_transition_var(+Var,+Transition,?DisjointVar)
% takes a (typed) variable and makes it disjoint to variables
% with same name but in context of other transitions by pairing it
% with the Transition
disjoint_transition_var([Var,Type],T,[[Var,T],Type]).

% disjoint_transition_vars(+Transition,+Net,?DisjointVars)
% returns the set of (typed) variables of Transition in Net
% where every variable is paired with Transition, i.e.
% of the form [[Var,Transition],Type]
disjoint_transition_vars(T,Net,DVars) :-
  transition_vars(T,Net,Vars),
  map_to_all(Vars,disjoint_transition_var,T,DVars).

% disjoint_pair_mapping(+[Var,Type], +Trans, ?Mapping)
% Mapping maps Var to the pair [Var,Trans]
disjoint_pair_mapping([Var,_], Trans, [Var,[Var,Trans]]).

% disjoint_pair_mappings(+List, +Element, +MapList)
disjoint_pair_mappings([],_,[]).
disjoint_pair_mappings([Var|List], Trans, [Mapping|MList]) :-
  disjoint_pair_mapping(Var,Trans,Mapping),
  disjoint_pair_mappings(List,Trans,MList).

% duplicate_terms(+List, -DList)
% DList is a list of duplicated versions of all terms in List;
% modifications using setarg/3 on terms in DList do not affect
% the terms in List
duplicate_terms([],[]).
duplicate_terms([A|AList],[B|BList]) :-
  duplicate_term(A,B),
  duplicate_terms(AList,BList).

% disjoint_terms(+Trans,+Net,+Terms,-DTerms)
% returns copies of all terms in Terms, where all occurrences of
% variables have been replaced using the disjoint_pair_mapping/3
% predicate
disjoint_terms(Trans, Net, Terms, DTerms) :-
  duplicate_terms(Terms,DTerms),
  transition_vars(Trans, Net, Vars),
  disjoint_pair_mappings(Vars, Trans, Mapping),
  condition_instances(Mapping,DTerms).

% disjoint_transition_conditions(+Trans, +Net, -DCond)
% returns disjoint copies of the conditions of Trans in Net
disjoint_transition_conditions(Trans, Net, DCond) :-
  transition_conditions(Trans, Net, Cond),
  disjoint_terms(Trans, Net, Cond, DCond).

% pre_equations(+Trans, +Net, -Pre)
% returns equations "P = V" for all P in the predomain of Trans
% with arc inscription V in Net
pre_equations(Trans, Net, Pre) :-
  findall(P = V, net_pre(Trans, V, P, Net), PreCond),
  disjoint_terms(Trans, Net, PreCond, Pre).

% post_equations(+Trans, +Net, -Post)
% returns equations "P = V" for all P in the postdomain of Trans
% with arc inscription V in Net
post_equations(Trans, Net, Post) :-
  findall(P = V, net_post(Trans, V, P, Net), PostCond),
  disjoint_terms(Trans, Net, PostCond, Post).

% transition_equations(+Trans, +Net, -Eq)
% returns all derived equations from the environment of Trans
% in Net; the derived equations are the disjoint transition conditions,
% and the pre and post equations
transition_equations(Trans, Net, Eq) :-
  disjoint_transition_conditions(Trans,Net,Cond),
  pre_equations(Trans,Net,Pre),
  post_equations(Trans,Net,Post),
  union(Pre,Post,PrePost),
  union(PrePost,Cond,Eq).

union_reduce([],[]).
union_reduce([S|Set],Union) :-
  union_reduce(Set,Rest),
  union(S,Rest,Union).

net_equations(Net, Eq) :-
  findall(T, net_transition(T,Net), Transitions),
  net_equations(Net,Transitions,Equations),
  union_reduce(Equations,Eq).

net_equations(_,[],[]).
net_equations(Net,[T|Transitions],[Eq|Equations]) :-
  transition_equations(T, Net, Eq),
  net_equations(Net,Transitions,Equations).

disjoint_net_vars(Net, Vars) :-
  findall(T,net_transition(T,Net), Transitions),
  disjoint_net_vars(Net,Transitions,TransVars),
  union_reduce(TransVars,TVars),
  findall([P,Type],net_place(P,Type,Net), Places),
  union(TVars,Places,Vars).

disjoint_net_vars(_,[],[]).
disjoint_net_vars(Net,[T|Transitions],[V|Vars]) :-
  disjoint_transition_vars(T,Net,V),
  disjoint_net_vars(Net,Transitions,Vars).

assignment_conforms_to([],_).
assignment_conforms_to([[V,A]|Ass],Partial) :-
  (member([V,A],Partial);
  not(member([V,_],Partial))),
  assignment_conforms_to(Ass,Partial),!.

net_vars(Net, Vars) :-
  findall(TV, transition_vars(_,Net,TV), TVarSets),
  findall([P,Type],net_place(P,Type,Net), Places),
  union_reduce(TVarSets,TVars),
  union(TVars,Places,Vars).

net_conditions(Net,Cond) :-
  findall(C, net_cond(_,C,Net), TCond),
  findall(P = V, net_pre(Trans, V, P, Net), PreCond),
  findall(P = V, net_post(Trans, V, P, Net), PostCond),
  union(PreCond,PostCond,PrePostCond),
  union(PrePostCond,TCond,Cond).


realization_assignment(Net,Ass) :-
  net_equations(Net,Eq),
  disjoint_net_vars(Net,Vars),
  var_assignment(Vars,Ass),
  satisfies_conditions(Ass,Eq),
  type_conform_assignment(Vars,Ass).

satisfies_each_condition(_,[]).
satisfies_each_condition(Ass,[C|Cond]) :-
  condition_instance(Ass,C),
  call(C),
  satisfies_each_condition(Ass,Cond).

realization_assignment(Net,Ass,Partial) :-
  net_conditions(Net,Eq),
  net_vars(Net,Vars),!,
  var_assignment(Vars,Ass),
  %satisfies_conditions(Ass,Eq),
  condition_instances(Ass,Eq),!,
  assignment_conforms_to(Ass,Partial),!,
  maplist(call,Eq),
  type_conform_assignment(Vars,Ass).
