package com.beyondtechnicallycorrect.graphicalhttpclient.bindings

final class ButtonBinding extends Updatable with Enablable {
  
  private var _enabled: Boolean = _
  private var _clicked: () => Unit = _
  private var _signalUpdate: Updatable => Unit = _
  
  def this(enabled: Boolean, clicked: () => Unit, signalUpdate: Updatable => Unit) {
    this()
    _enabled = enabled
    _clicked = clicked
    _signalUpdate = signalUpdate
  }
  
  def enabled: Boolean = _enabled
  
  def enabled_=(enabled: Boolean) {
    _enabled = enabled
    _signalUpdate(this)
  }
  
  def clicked() {
    if(_enabled) {
      _clicked()
    }
  }

}