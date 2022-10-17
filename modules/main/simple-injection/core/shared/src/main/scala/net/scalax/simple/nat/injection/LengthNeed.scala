package net.scalax.simple.nat.injection

trait LengthNeedFuture extends NeedFuture {
  override def future: LengthNeedPass
  def size: Int
}

trait LengthNeedPass extends NeedPass {
  def length: Int
  def index: Int

  override def pass: LengthNeedFuture
}

trait LengthCurrent extends LengthNeedFuture with LengthNeedPass {
  override def future: LengthNeedPass
  override def pass: LengthNeedFuture
  override def size: Int  = pass.size + 1
  override def index: Int = size - 1

  override def length: Int = future.length
}
