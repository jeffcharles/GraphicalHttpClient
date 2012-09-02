package com.beyondtechnicallycorrect.graphicalhttpclient
import java.net.URL
import java.net.MalformedURLException
import java.net.URISyntaxException

object ViewBinder {
  
  val url: InputField[URL] =
    new InputField(
        value = "",
        enabled = true,
        toUnderlying = input => {
          try {
            val url = new URL(input)
            url.toURI() // performs additional validation
            (true, url)
          } catch {
            case e: URISyntaxException => (false, null)
            case e: MalformedURLException => (false, null)
          }
        }
      )
  val headers: InputField[Iterable[(String, String)]] =
    new InputField(
        value = "",
        enabled = true,
        toUnderlying = input => {
          val headerKeyValuePairs =
            input
              .split(sys.props("line.separator"))
              .map(_.split(":").map(_.trim))
          val valid = headerKeyValuePairs.forall(_.length == 2)
          if(valid) {
            (true, headerKeyValuePairs.map(pair => (pair(0), pair(1))))
          } else {
            (false, null)
          }
        }
      )
  val requestBody: InputField[String] =
    new InputField(
        value = "",
        enabled = true,
        toUnderlying = input => (true, input)
      )
  
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

class InputField[T <: AnyRef] {
  
  private var _value: String = _
  private var _enabled: Boolean = _
  private var _underlyingValue: T = _
  private var _toUnderlying: String => (Boolean, T) = _
  private var _hasValidState: Boolean = true
  
  def this(
      value: String,
      enabled: Boolean,
      toUnderlying: String => (Boolean, T)
    ) {
      
    this()
    _value = value
    _enabled = enabled
    _toUnderlying = toUnderlying
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
    ViewBinder.updateView()
  }
  
  def underlyingValue: T = _underlyingValue
  
  def hasValidState: Boolean = _hasValidState
}

class ButtonBinding {
  
  private var _enabled: Boolean = _
  private var _clicked: () => Unit = _
  
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
  
  private var _value: String = _
  
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