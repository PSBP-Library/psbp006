package psbp.implementation.program

import psbp.specification.computation.{Computation}

import psbp.implementation.function.{Function}

import psbp.implementation.algorithm.{SequentialComposition}

import psbp.implementation.dataStructure.{DataStructure}

private[psbp] given program[
    C[+_]: Computation,
    &&[+_, +_]: psbp.specification.Product,
    ||[+_, +_]: psbp.specification.Sum
]: psbp.specification.program.Program[[Z, Y] =>> Z => C[Y], &&, ||]
  with Function[C, &&]
  with SequentialComposition[C]
  with DataStructure[C, &&, ||]