package com.beyondtechnicallycorrect.graphicalhttpclient
import java.net.URL
import java.net.MalformedURLException
import java.net.URISyntaxException
import com.beyondtechnicallycorrect.graphicalhttpclient.bindings._

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
        },
        signalUpdate = this.updateView
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
        },
        signalUpdate = this.updateView
      )
  val requestBody: InputField[String] =
    new InputField(
        value = "",
        enabled = true,
        toUnderlying = input => (true, input),
        signalUpdate = this.updateView
      )
  
  val getButton =
    new ButtonBinding(
        enabled = true,
        clicked = () => {},
        signalUpdate = this.updateView
      )
  val postButton =
    new ButtonBinding(
        enabled = true,
        clicked = () => {},
        signalUpdate = this.updateView
      )
  val putButton =
    new ButtonBinding(
        enabled = true,
        clicked = () => {},
        signalUpdate = this.updateView
      )
  val deleteButton =
    new ButtonBinding(
        enabled = true,
        clicked = () => {},
        signalUpdate = this.updateView
      )
  val cancelButton =
    new ButtonBinding(
        enabled = false,
        clicked = () => {},
        signalUpdate = this.updateView
      )
  
  val response = new OutputField(value = "", signalUpdate = this.updateView)
  
  def updateView() {
    UserInterface.valueChanged()
  }
}