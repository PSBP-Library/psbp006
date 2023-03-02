# Program Specification Based Programming

This is lesson 006 of a "Program Specification Based Programming" course.

All comments are welcome at luc.duponcheel[at]gmail.com.

## Introduction

In lesson 005 we have defined all `trait Program` members in terms of `trait Computation`, function level 
`trait Product` members and function level `trait Sum` members.

## Content

### Implementing `DataStructure` in terms of `Computation`, function level `Product` and function level `Sum`

```scala
package psbp.implementation.dataStructure

import psbp.specification.function.{Function}

import psbp.specification.algorithm.{SequentialComposition}

import psbp.specification.computation.{Computation}

private[psbp] trait DataStructure[
    C[+_]: Computation: [C[+_]] =>> Function[[Z, Y] =>> Z => C[Y], &&]: [C[
        +_
    ]] =>> SequentialComposition[[Z, Y] =>> Z => C[Y]],
    &&[+_, +_]: psbp.specification.Product,
    ||[+_, +_]: psbp.specification.Sum
] extends psbp.implementation.dataStructure.Product[C, &&],
      psbp.implementation.dataStructure.Sum[C, ||],
      psbp.specification.dataStructure.DataStructure[[Z, Y] =>> Z => C[Y], &&, ||]
```

### Implementing `Program` in terms of `Computation`, function level `Product` and function level `Sum`

```scala
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
```

This implementation is a `given` instead of a `trait`.

## Conclusion

We have defined all `trait Program` members in terms of `trait Computation` members, function level `trait Product`
members and function level `trait Sum` members as a `given`.