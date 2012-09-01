package com.beyondtechnicallycorrect.graphicalhttpclient

object ViewBinder {
  
  val url: InputField = new InputField(value = "", enabled = true)
  val headers: InputField = new InputField(value = "", enabled = true)
  val requestBody: InputField = new InputField(value = "", enabled = true)
  
  val getButton = new ButtonBinding(enabled = true, clicked = () => {})
  val postButton = new ButtonBinding(enabled = true, clicked = () => {})
  val putButton = new ButtonBinding(enabled = true, clicked = () => {})
  val deleteButton = new ButtonBinding(enabled = true, clicked = () => {})
  val cancelButton = new ButtonBinding(enabled = false, clicked = () => {})
  
  val response = new OutputField(value = "")
  
  def updateView() {
    UserInterface.valueChanged()
  }
}

class InputField {
  
  private var _value: String = null
  private var _enabled: Boolean = false
  
  def this(value: String, enabled: Boolean) {
    this()
    _value = value
    _enabled = enabled
  }
  
  def value: String = _value
  
  def value_=(value: String) {
    _value = value
  }
  
  def enabled: Boolean = _enabled
  
  def enabled_=(enabled: Boolean) {
    _enabled = enabled
    ViewBinder.updateView()
  }
}

class ButtonBinding {
  
  private var _enabled: Boolean = false
  private var _clicked: () => Unit = () => {}
  
  def this(enabled: Boolean, clicked: () => Unit) {
    this()
    _enabled = enabled
    _clicked = clicked
  }
  
  def enabled: Boolean = _enabled
  
  def enabled_=(enabled: Boolean) {
    _enabled = enabled
    ViewBinder.updateView()
  }
  
  def clicked() {
    _clicked()
  }
}

class OutputField {
  
  private var _value: String = null
  
  def this(value: String) {
    this()
    _value = value
  }
  
  def value: String = _value
  
  def value_=(value: String) {
    _value = value
    ViewBinder.updateView()
  }
}