package com.beyondtechnicallycorrect.graphicalhttpclient.bindings

final class InputField[T <: AnyRef] {
  
  private var _value: String = _
  private var _enabled: Boolean = _
  private var _underlyingValue: T = _
  private var _toUnderlying: String => (Boolean, T) = _
  private var _hasValidState: Boolean = true
  private var _signalUpdate: () => Unit = _
  
  def this(
      value: String,
      enabled: Boolean,
      toUnderlying: String => (Boolean, T),
      signalUpdate: () => Unit
    ) {
      
    this()
    _value = value
    _enabled = enabled
    _toUnderlying = toUnderlying
    _signalUpdate = signalUpdate
  }
  
  def value: String = _value
  
  def value_=(value: String) {
    _value = value
    val conversionPair = _toUnderlying(value)
    _hasValidState = conversionPair._1
    _underlyingValue = conversionPair._2
  }
  
  def enabled: Boolean = _enabled
  
  def enabled_=(enabled: Boolean) {
    _enabled = enabled
    _signalUpdate()
  }
  
  def underlyingValue: T = _underlyingValue
  
  def hasValidState: Boolean = _hasValidState
}