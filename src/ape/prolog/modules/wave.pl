%%%% Utility

% list_of(?List, +Pred, +Limit)
% if List is given, it is checked whether all elements satisfy
% predicate Pred/1 and if List has at most length Limit;
% otherwise, List is returned as a suitable list
list_of([],_,Limit) :-
  Limit >= 0.
list_of([L|List],Pred,Limit) :-
  Limit >= 0,
  P =..[Pred,L],
  P,
  list_of(List,Pred,Limit-1).

% set_of(?List, +Pred, +Limit)
% same as list_of/3, but with restriction to sets
set_of(List,Pred,Limit) :-
  list_of(List,Pred,Limit),
  is_set(List).


%%%% Sorts / Types

% Booleans
:- type(type_bool).
type_bool(t).
type_bool(f).


% natural numbers
:- type(type_nat).
type_nat(N) :- between(0,2,N).

% range of natural numbers
:- type(type_range).
type_range([R1,R2]) :- type_nat(R1), type_nat(R2), R1 =< R2.


% text string given as a list of characters
:- type(type_text).
type_char(C) :- between(0,1,C).
type_text(T) :- list_of(T,type_char,2).

% users given as atoms
:- type(type_user).
type_user(alice).
type_user(bob).
type_user_count(2).

% wavelets have an id (nat), a set of invited users,
% and a text
:- type(type_wavelet).
type_wavelet([Id,Users,Text]) :-
  type_nat(Id),
  type_user_count(User_Limit),
  set_of(Users,type_user,User_Limit),
  type_text(Text).



%%%% Operations: Declaration

:- op(true, [type_bool]).
true_A(B) :- B = t.

:- op(false, [type_bool]).
false_A(B) :- B = f.

:- op(start, [type_range,type_nat]).
start_A([N,_],N).

:- op(toRange, [type_nat,type_nat,type_range]).
toRange_A(N1,N2,[N1,N2]).

:- op(end, [type_range,type_nat]).
end_A([_,N],N).

:- op(next, [type_nat,type_nat]).
next_A(N1,N2) :- succ(N1,N2).

:- op(new, [type_user,type_nat,type_wavelet]).
new_A(User,Id,[Id,[User],[]]).

:- op(addUser, [type_user,type_wavelet,type_wavelet]).
addUser_A(User,[Id,Users,T],[Id,Union,T]) :- union(Users,[User],Union).

:- op(invited, [type_wavelet,type_user,type_bool]).
invited_A([_,Users,_],U,t) :- member(U,Users).
invited_A([_,Users,_],U,f) :- not(member(U,Users)).

:- op(len, [type_text,type_nat]).
len_A(T,N) :- length(T,N).

:- op(sub, [type_text,type_range,type_text]).
%sub_A(_,[Start,End],[]) :- End < Start, !.
%sub_A(Text, [Start,_],[]) :- len_A(Text,L), L =< Start, !.

sub_A(_,[N,N],[]) :- !.
sub_A([T|Text],[0,End],[T|Sub]) :-
  End > 0,
  succ(E2,End),
  sub_A(Text,[0,E2],Sub).

sub_A([_|Text],[Start,End],Sub) :-
  Start =< End,
  Start > 0,
  End > 0,
  succ(S2,Start),
  succ(E2,End),
  sub_A(Text,[S2,E2],Sub).

%% small mistake in algebra:
%% it should be "pos" instead of "pos-1" in the first substring
:- op(insText, [type_wavelet,type_text,type_nat,type_wavelet]).
insText_A([Id,Users,Text],T,Pos,[Id,Users,NewText]) :-
  sub_A(Text,[0,Pos],Sub1),
  len_A(Text,L),
  sub_A(Text,[Pos,L],Sub2),
  append(Sub1,T,New1),
  append(New1,Sub2,NewText).

:- op(remText, [type_wavelet,type_range,type_wavelet]).
remText_A([Id,Users,Text],[Start,End],[Id,Users,NewText]) :-
  sub_A(Text,[0,Start],Sub1),
  len_A(Text,L),
  sub_A(Text,[End,L],Sub2),
  append(Sub1,Sub2,NewText).

