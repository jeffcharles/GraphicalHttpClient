package com.beyondtechnicallycorrect.graphicalhttpclient
import java.net.URL
import java.net.MalformedURLException
import java.net.URISyntaxException
import com.beyondtechnicallycorrect.graphicalhttpclient.bindings._
import com.beyondtechnicallycorrect.graphicalhttpclient.Prelude._

object ViewBinder {
  
  val url = createInputField[URL](
      toUnderlying = input => {
        try {
          val url = new URL(input)
          url.toURI() // performs additional validation
          Some(url)
        } catch {
          case e: URISyntaxException => None
          case e: MalformedURLException => None
        }
      }
    )
  val headers = createInputField[Iterable[(String, String)]](
      toUnderlying = input => {
        val headerKeyValuePairs =
          input
            .split(newline)
            .map(_.split(":").map(_.trim))
        val valid = headerKeyValuePairs.forall(_.length == 2)
        if(valid)
          Some(headerKeyValuePairs.map(pair => (pair(0), pair(1))))
        else
          None
      }
    )
  val requestBody = createInputField[String](
      toUnderlying = input => Some(input)
    )
  
  val getButton = createButton(clicked = () => {})
  val postButton = createButton(clicked = () => {})
  val putButton = createButton(clicked = () => {})
  val deleteButton = createButton(clicked = () => {})
  val cancelButton = createButton(enabled = false, clicked = () => {})
  
  val response = new OutputField(value = "", signalUpdate = this.updateView)
  
  private def updateView() {
    UserInterface.valueChanged()
  }
  
  private def createInputField[T <: AnyRef](toUnderlying: String => Option[T]): InputField[T] =
    new InputField(
        value = "",
        enabled = true,
        toUnderlying = toUnderlying,
        signalUpdate = this.updateView
    )
  
  private def createButton(enabled: Boolean = true, clicked: () => Unit): ButtonBinding =
    new ButtonBinding(
        enabled = enabled,
        clicked = clicked,
        signalUpdate = this.updateView
      )
}