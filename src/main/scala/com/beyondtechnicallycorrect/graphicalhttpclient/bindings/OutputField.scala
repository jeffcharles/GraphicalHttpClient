package com.beyondtechnicallycorrect.graphicalhttpclient.bindings

final class OutputField extends Updatable {

  private var _value: String = _
  private var _signalUpdate: Updatable => Unit = _
  
  def this(value: String, signalUpdate: Updatable => Unit) {
    this()
    _value = value
    _signalUpdate = signalUpdate
  }
  
  def value: String = _value
  
  def value_=(value: String) {
    _value = value
    _signalUpdate(this)
  }
}