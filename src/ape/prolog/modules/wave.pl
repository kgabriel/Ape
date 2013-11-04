:- use_module(library(clpfd)).

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
type_bool(B) :- B in 0..1.

% natural numbers
:- type(type_nat).
type_nat(N) :- nat_upper_bound(B), N in 0..B.
nat_upper_bound(16).

% range of natural numbers
:- type(type_range).
type_range([R1,R2]) :- type_nat(R1), type_nat(R2).


% text string given as a list of characters
:- type(type_text).
type_char(C) :- C in 0..255.
type_text(T) :- nat_upper_bound(B), list_of(T,type_char,B).

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
true_A(1).

:- op(false, [type_bool]).
false_A(0).

:- op(start, [type_range,type_nat]).
start_A([N,_],N).

:- op(toRange, [type_nat,type_nat,type_range]).
toRange_A(N1,N2,[N1,N2]).

:- op(next, [type_nat,type_nat]).
next_A(N1,N2) :- N2 #= N1 + 1.

:- op(new, [type_user,type_nat,type_wavelet]).
new_A(User,Id,[Id,[User],[]]).

:- op(addUser, [type_user,type_wavelet,type_wavelet]).
addUser_A(User,[Id,Users,T],[Id,Union,T]) :- union(Users,[User],Union).

:- op(invited, [type_wavelet,type_user,type_bool]).
invited_A([_,Users,_],U,1) :- member(U,Users).
invited_A([_,Users,_],U,0) :- not(member(U,Users)).

:- op(len, [type_text,type_nat]).
len_A(T,N) :- length(T,N).

:- op(sub, [type_text,type_range,type_text]).
sub_A(Text,[Start,RLen],Sub) :-
  append(Head,_,Text),
  append(Pre,Sub,Head),
  length(Sub,RLen),
  length(Pre,Start).

:- op(insText, [type_wavelet,type_text,type_nat,type_wavelet]).
insText_A([Id,Users,Text],T,Pos,[Id,Users,NewText]) :-
  Pos #=< L,
  RestLen #= L - Pos,
  len_A(Text,L),
  sub_A(Text,[0,Pos],Sub1),
  sub_A(Text,[Pos,RestLen],Sub2),
  N #= L1 + TL + L2,
  length(Sub1,L1),
  length(Sub2,L2),
  length(NewText,N),
  length(T, TL),
  append(Sub1,T,New1),
  append(New1,Sub2,NewText).

:- op(remText, [type_wavelet,type_range,type_wavelet]).
remText_A([Id,Users,Text],[Start,RLen],[Id,Users,NewText]) :-
  sub_A(Text,[0,Start],Sub1),
  len_A(Text,L),
  End #= Start + RLen,
  L #>= End,
  EndLen #= L - End,
  sub_A(Text,[End,EndLen],Sub2),
  append(Sub1,Sub2,NewText).

