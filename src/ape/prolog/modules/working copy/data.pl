%% for each type (or sort) there has to a predicate <type>/1,
%% and it is recommended to use 'type_<name>' as convention
%% for type names

% void is a type with a single element 'empty'
type_void(empty).

% all types have to fulfill the type/1 predicate
:- abolish(type_declaration/1), dynamic(type_declaration/1).
type(Type) :-
  retractall(type_declaration(Type)),
  assertz(type_declaration(Type)).
:- type(type_void).

% type_unknown means that it can be of any registered type
type_unknown(X) :-
  findall(T, type_declaration(T), Types),!,
  type_unknown(X,Types).
type_unknown(_,[]) :- !, fail.
type_unknown(X,[T|Types]) :-
  (TX =.. [T,X], TX);
  type_unknown(X, Types).



%% the first thing to do in an operation is always to check the
%% types of the arguments using check_op_args/2,
%% actual definition of an operation works in two steps:
%% 1. Declaration of <op>: <t1>...<tn> -> <t>
%%    :- op(<op>, [<t1>,...,<tn>,<t>]).
%% 2. Definition of semantics
%%    <op>_A(Arg1,...,Argn,Arg) :- ... as usual
%%

:- abolish(op_declaration/2), dynamic(op_declaration/2).

op(Op, Types) :-
  Declaration =.. [op_declaration,Op,Types],
  assertz(Declaration),!,
  assert_op(Op,Types),!.

assert_op(Op,Types) :-
  length(Types,N),
  is_list(Types),
  abolish(Op/N),
  dynamic(Op/N),
  fresh_vars(Types,Vars),
  Op_head =.. [Op|Vars],
  Op_call =.. [operation,Op,Vars],
  retractall(Op_head),
  assertz(Op_head :- Op_call),!.

fresh_vars([],[]).
fresh_vars([_|Types],[_|Vars]) :- fresh_vars(Types,Vars).

% check if arguments of an operation are conform with the
% operation's declaration
check_op_args(Op,Args) :- op_declaration(Op,Types),check_arg_types(Args,Types).
check_arg_types([],[]).
check_arg_types([A|Args],[T|Types]) :-
  Conform =.. [T,A],
  Conform,
  check_arg_types(Args,Types).

% check types of arguments and call semantics
operation(Op,Args) :-
  atomic_concat(Op,'_A',Op_A),
  Op_A_call =.. [Op_A|Args],!,
  check_op_args(Op,Args),
  Op_A_call.


%%%% Example:
%% Type nat of numbers [0,...,5]
% type(type_nat).
% type_nat(X) :- between(0,5,X).
%
% next: type_nat -> type_nat
% :- op(next, [type_nat,type_nat]).
% next_A(X,Y) :- succ(X,Y).
