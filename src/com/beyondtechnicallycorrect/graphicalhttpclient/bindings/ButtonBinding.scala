package com.beyondtechnicallycorrect.graphicalhttpclient.bindings

final class ButtonBinding extends Enablable {
  
  private var _enabled: Boolean = _
  private var _clicked: () => Unit = _
  private var _signalUpdate: () => Unit = _
  
  def this(enabled: Boolean, clicked: () => Unit, signalUpdate: () => Unit) {
    this()
    _enabled = enabled
    _clicked = clicked
    _signalUpdate = signalUpdate
  }
  
  def enabled: Boolean = _enabled
  
  def enabled_=(enabled: Boolean) {
    _enabled = enabled
    _signalUpdate()
  }
  
  def clicked() {
    if(_enabled) {
      _clicked()
    }
  }

}