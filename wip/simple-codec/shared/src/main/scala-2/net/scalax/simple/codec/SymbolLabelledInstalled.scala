package net.scalax.simple.codec

import shapeless._

trait SymbolLabelledInstalled[F[_[_]]] {
  def model: F[SymbolLabelledInstalled.ToNamedSymbol]
}

object SymbolLabelledInstalled {

  trait LabelledGeneric[Model] {
    def generic[H1](implicit l: DefaultSymbolicLabelling.Aux[Model, H1]): H1 = l.apply()
    def law[ModelImpl >: Model <: Model]: LabelledGeneric[ModelImpl]         = LabelledGeneric[ModelImpl]
  }
  object LabelledGeneric {
    def apply[T]: LabelledGeneric[T] = new LabelledGeneric[T] {
      //
    }
  }

  type ToNamedSymbol[_] = Symbol

  class DerivedApply[F[_[_]]] {
    def derived2[HTypeTemp](
      genericType: LabelledGeneric[F[SymbolLabelledInstalled.ToNamedSymbol]] => HTypeTemp
    ): InnerApply1[HTypeTemp] = new InnerApply1[HTypeTemp](genericType(LabelledGeneric[F[SymbolLabelledInstalled.ToNamedSymbol]]))

    class InnerApply1[HTemp](genericType: HTemp) {
      def apply(
        t: SimpleFromGeneric[F[SymbolLabelledInstalled.ToNamedSymbol]] => HTemp => F[SymbolLabelledInstalled.ToNamedSymbol]
      ): SymbolLabelledInstalled[F] =
        new SymbolLabelledInstalled[F] {
          def model: F[SymbolLabelledInstalled.ToNamedSymbol] =
            t(SimpleFromGeneric[F[SymbolLabelledInstalled.ToNamedSymbol]])(genericType)
        }
    }
  }

  def apply[F[_[_]]]: DerivedApply[F] = new DerivedApply[F]

}
