package com.beyondtechnicallycorrect.graphicalhttpclient.bindings

final class InputField[T <: AnyRef] {
  
  private var _value: String = _
  private var _enabled: Boolean = _
  private var _underlyingValue: T = _
  private var _toUnderlying: String => Option[T] = _
  private var _hasValidState: Boolean = true
  private var _signalUpdate: () => Unit = _
  
  def this(
      value: String,
      enabled: Boolean,
      toUnderlying: String => Option[T],
      signalUpdate: () => Unit
    ) {
      
    this()
    _value = value
    _enabled = enabled
    _toUnderlying = toUnderlying
    _signalUpdate = signalUpdate
    evaluateToUnderlying(_value)
  }
  
  def value: String = _value
  
  def value_=(value: String) {
    _value = value
    evaluateToUnderlying(_value)
  }
  
  def enabled: Boolean = _enabled
  
  def enabled_=(enabled: Boolean) {
    _enabled = enabled
    _signalUpdate()
  }
  
  def underlyingValue: T = _underlyingValue
  
  def hasValidState: Boolean = _hasValidState
  
  private def evaluateToUnderlying(value: String) {
    _toUnderlying(_value) match {
      case Some(underlyingValue) => _underlyingValue = underlyingValue; _hasValidState = true
      case None => _hasValidState = false
    }
  }
}