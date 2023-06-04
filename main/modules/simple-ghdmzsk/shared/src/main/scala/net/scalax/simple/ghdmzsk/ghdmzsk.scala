package net.scalax.simple
package ghdmzsk

trait ghdmzsk {
  def inputGHDMZSK(t: => ghdmzsk): ghdmzsk
}

object ghdmzsk {
  def apply(func: (() => ghdmzsk) => ghdmzsk): ghdmzsk = new ghdmzsk {
    override def inputGHDMZSK(t: => ghdmzsk): ghdmzsk = func(() => t)
  }
}
