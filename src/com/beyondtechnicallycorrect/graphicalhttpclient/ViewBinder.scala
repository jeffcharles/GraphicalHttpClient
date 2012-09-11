package com.beyondtechnicallycorrect.graphicalhttpclient
import java.net.URL
import java.net.MalformedURLException
import java.net.URISyntaxException
import scala.actors.Futures._
import com.beyondtechnicallycorrect.graphicalhttpclient.bindings._
import com.beyondtechnicallycorrect.graphicalhttpclient.connection._
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
        if(input == "") {
          Some(Nil)
        } else {
          val headerKeyValuePairs = input
            .split(newline)
            .map(_.split(":").map(_.trim))
          val valid = headerKeyValuePairs.forall(_.length == 2)
          if(valid)
            Some(headerKeyValuePairs.map(pair => (pair(0), pair(1))))
          else
            None
        }
      }
    )
  val requestBody = createInputField[String](
      toUnderlying = input => Some(input)
    )
  
  val getButton = createButton(clicked = launchConnectionFunc(verb = Get))
  val postButton = createButton(clicked = launchConnectionFunc(verb = Post))
  val putButton = createButton(clicked = launchConnectionFunc(verb = Put))
  val deleteButton = createButton(clicked = launchConnectionFunc(verb = Delete))
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
  
  private def allValid(): Boolean =
    url.hasValidState && headers.hasValidState && requestBody.hasValidState
  
  private def launchConnectionFunc(verb: Verb): () => Unit = () => {
    if(this.allValid) {
      val inputs = Array(url, headers, requestBody, getButton, postButton, putButton, deleteButton)
      inputs.foreach(_.enabled = false)
      cancelButton.enabled = true
      response.value = "Waiting for response..."
      val futureResponse = future { Attempt.launchConnection(
          new Request(
              verb = verb,
              url = url.underlyingValue,
              headers = headers.underlyingValue,
              body = requestBody.underlyingValue
            )
        )
      }
      val displayMsgAndReenable = (msg: String) => {
        response.value = msg
        inputs.foreach(_.enabled = true)
        cancelButton.enabled = false
      }
      futureResponse respond {
        case Some(resp) => displayMsgAndReenable(resp.toString)
        case None => displayMsgAndReenable("Some sort of error occurred")
      }
    }
  } 
}