# Program Specification Based Programming

This is lesson 005 of a "Program Specification Based Programming" course.

All comments are welcome at luc.duponcheel[at]gmail.com.

## Introduction

In lesson 004 we have defined almost all `trait Program` members in terms of `trait Computation` members and function
level `trait Product` members.

Below the, yet to be defined, declared `trait` members so far are briefly repeated.

```scala
private[psbp] trait IfThenElse[
    >-->[-_, +_]: [>-->[-_, +_]] =>> LocalDefinition[>-->, &&],
    &&[+_, +_]
]:

  // internal declared

  extension [Z, Y](`z>-t->y`: => Z >--> Y)
    private[psbp] def OrElse(`z>-f->y`: => Z >--> Y): (Z && Boolean) >--> Y
```

## Content

The member `OrElse` above involves `&&` but, as its name suggests, there is more to it than meets the eye.

### Function level Data Structures

Below the sum related data structure function concepts are specified.

```scala
package psbp.specification

private[psbp] trait Sum[||[+_, +_]]:

  // internal declared

  private[psbp] def `y=>(y||z)`[Z, Y]: Y => (Y || Z)

  private[psbp] def `z=>(y||z)`[Z, Y]: Z => (Y || Z)

  private[psbp] def foldSum[Z, Y, X]: (X => Z) => (Y => Z) => (X || Y) => Z
```

### Data Structures

Below the basic sum related data structure program concepts are specified.

```scala
package psbp.specification.dataStructure

private[psbp] trait Sum[>-->[-_, +_], ||[+_, +_]]:

  // internal declared

  private[psbp] def sum[Z, Y, X](
      `x>-->z`: => X >--> Z,
      `y>-->z`: => Y >--> Z
  ): (X || Y) >--> Z

  // internal defined

  extension [Z, Y, X](`x>-->z`: => X >--> Z)
    private[psbp] def ||(`y>-->z`: => Y >--> Z): (X || Y) >--> Z = sum(`x>-->z`, `y>-->z`)
```

### Updating `DataStructure`

```scala
package psbp.specification.dataStructure

private[psbp] trait DataStructure[>-->[-_, +_], &&[+_, +_], ||[+_, +_]]
    extends Product[>-->, &&],
      Sum[>-->, ||]
```

###  Updating `Program`

`DataStructure` now has an extra argument `||` and therefore `Program` needs to be updated accordingly.

```scala
package psbp.specification.program

import psbp.specification.function.{Function}

import psbp.specification.algorithm.{Algorithm}

import psbp.specification.dataStructure.{DataStructure}

trait Program[>-->[-_, +_], &&[+_, +_], ||[+_, +_]]
    extends Function[>-->, &&],
      Algorithm[>-->, &&],
      DataStructure[>-->, &&, ||]
```

### Updating definitions that use `Program`

`Program` now has an extra argument `||` and therefore definitions that use `Program` needs to be updated accordingly.

Please have a look at the code in `src`.

### Updating `IfThenElse`

Recall that for technical reasons we always define extension members in terms of declared method members.

Therefore we declare an extra internal method member `ifThenElse` in `IfThenElse`.

```scala
package psbp.specification.algorithm

private[psbp] trait IfThenElse[
    >-->[-_, +_]: [>-->[-_, +_]] =>> LocalDefinition[>-->, &&],
    &&[+_, +_],
    ||[+_, +_]
]:

  private lazy val summonedLocalDefinition = summon[LocalDefinition[>-->, &&]]

  import summonedLocalDefinition.{Let}

  // external defined

  def If[Z, Y](`z>-->b`: Z >--> Boolean): Then[Z, Y] =
    new:
      override def Then(`z>-t->y`: => Z >--> Y): Else[Z, Y] =
        new:
          override def Else(`z>-f->y`: => Z >--> Y): Z >--> Y =
            Let {
              `z>-->b`
            } In {
              `z>-t->y` OrElse `z>-f->y`
            }

  // internal declared

  private[psbp] trait Then[Z, Y]:
    def Then(`z>-t->y`: => Z >--> Y): Else[Z, Y]

  private[psbp] trait Else[Z, Y]:
    def Else(`z>-f->y`: => Z >--> Y): Z >--> Y

  private[psbp] def ifThenElse[Z, Y](
      `z>-t->y`: => Z >--> Y,
      `z>-f->y`: => Z >--> Y
  ): (Z && Boolean) >--> Y

  // internal defined

  extension [Z, Y](`z>-t->y`: => Z >--> Y)
    private[psbp] def OrElse(`z>-f->y`: => Z >--> Y): (Z && Boolean) >--> Y =
        ifThenElse(`z>-f->y`, `z>-f->y`)
```

## Implementing `IfThenElse` in terms of function level `Product` and `Sum`

```scala
package psbp.specification.algorithm

import psbp.specification.function.{Function}

import psbp.specification.dataStructure.{Sum}

private[psbp] trait IfThenElse[
    >-->[-_, +_]
      : [>-->[-_, +_]] =>> Function[>-->, &&]
      : SequentialComposition
      : [>-->[-_, +_]] =>> LocalDefinition[>-->, &&]
      : [>-->[-_, +_]] =>> Sum[>-->, ||],
    &&[+_, +_]: psbp.specification.Product,
    ||[+_, +_]: psbp.specification.Sum
]:

  private lazy val summonedFunction = summon[Function[>-->, &&]]

  import summonedFunction.{functionLift}

  private lazy val summonedLocalDefinition = summon[LocalDefinition[>-->, &&]]

  import summonedLocalDefinition.{Let}

  private lazy val summonedFunctionLevelProduct = summon[psbp.specification.Product[&&]]

  import summonedFunctionLevelProduct.{`(z&&y)=>z`, `(z&&y)=>y`}

  private lazy val summonedFunctionLevelSum = summon[psbp.specification.Sum[||]]

  import summonedFunctionLevelSum.{`y=>(y||z)`, `z=>(y||z)`}

  // external defined

  def If[Z, Y](`z>-->b`: Z >--> Boolean): Then[Z, Y] =
    new:
      override def Then(`z>-t->y`: => Z >--> Y): Else[Z, Y] =
        new:
          override def Else(`z>-f->y`: => Z >--> Y): Z >--> Y =
            Let {
              `z>-->b`
            } In {
              `z>-t->y` OrElse `z>-f->y`
            }

  // internal declared

  private[psbp] trait Then[Z, Y]:
    def Then(`z>-t->y`: => Z >--> Y): Else[Z, Y]

  private[psbp] trait Else[Z, Y]:
    def Else(`z>-f->y`: => Z >--> Y): Z >--> Y

  private[psbp] def ifThenElse[Z, Y](
      `z>-t->y`: => Z >--> Y,
      `z>-f->y`: => Z >--> Y
  ): (Z && Boolean) >--> Y =
    (functionLift[Z && Boolean, Z || Z] { `z&&b` =>
      if (`(z&&y)=>y`(`z&&b`))
      then `y=>(y||z)`(`(z&&y)=>z`(`z&&b`))
      else `z=>(y||z)`(`(z&&y)=>z`(`z&&b`))
    }) >--> (`z>-t->y` || `z>-f->y`)

  // internal defined

  extension [Z, Y](`z>-t->y`: => Z >--> Y)
    private[psbp] def OrElse(`z>-f->y`: => Z >--> Y): (Z && Boolean) >--> Y =
      ifThenElse(`z>-f->y`, `z>-f->y`)
```

## Implementing `Sum` in terms of `Computation` and function level `Sum`

We have defined all `trait Program` members but we have also added a declared one.

```scala
package psbp.implementation.dataStructure

import psbp.specification.computation.{Computation}

private[psbp] trait Sum[C[+_]: Computation, ||[+_, +_]: psbp.specification.Sum]
    extends psbp.specification.dataStructure.Sum[[Z, Y] =>> Z => C[Y], ||]:

  private type >--> = [Z, Y] =>> Z => C[Y]

  private lazy val summonedSum = summon[psbp.specification.Sum[||]]

  import summonedSum.{foldSum}

  // internal defined

  private[psbp] def sum[Z, Y, X](
      `x>-->z`: => X >--> Z,
      `y>-->z`: => Y >--> Z
  ): (X || Y) >--> Z = foldSum(`x>-->z`)(`y>-->z`)
```

## Updating `Algorithm`

`IfThenElse` now has an extra argument `||` and therefore `Algorithm` needs to be updated accordingly.

```scala
package psbp.specification.algorithm

private[psbp] trait Algorithm[>-->[-_, +_], &&[+_, +_], ||[+_, +_]]
    extends IfThenElse[>-->, &&, ||],
      LocalDefinition[>-->, &&],
      SequentialComposition[>-->]
```

## Updating `Program`

`Algorithm` now has an extra argument `||` and therefore `Program` needs to be updated accordingly.

```scala
package psbp.specification.program

import psbp.specification.function.{Function}

import psbp.specification.algorithm.{Algorithm}

import psbp.specification.dataStructure.{DataStructure}

trait Program[>-->[-_, +_], &&[+_, +_], ||[+_, +_]]
    extends Function[>-->, &&],
      Algorithm[>-->, &&, ||],
      DataStructure[>-->, &&, ||]
```

## Conclusion

We have defined all `trait Program` members.
