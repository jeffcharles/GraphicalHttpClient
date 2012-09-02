package com.beyondtechnicallycorrect.graphicalhttpclient.bindings

final class OutputField {

  private var _value: String = _
  private var _signalUpdate: () => Unit = _
  
  def this(value: String, signalUpdate: () => Unit) {
    this()
    _value = value
    _signalUpdate = signalUpdate
  }
  
  def value: String = _value
  
  def value_=(value: String) {
    _value = value
    _signalUpdate()
  }
}