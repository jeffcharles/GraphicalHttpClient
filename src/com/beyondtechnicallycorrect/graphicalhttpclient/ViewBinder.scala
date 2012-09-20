package com.beyondtechnicallycorrect.graphicalhttpclient
import java.net.URL
import java.net.MalformedURLException
import java.net.URISyntaxException
import scala.actors.Futures._
import com.beyondtechnicallycorrect.graphicalhttpclient.bindings._
import com.beyondtechnicallycorrect.graphicalhttpclient.connection._
import com.beyondtechnicallycorrect.graphicalhttpclient.Prelude._
import java.util.Date

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
  
  var lastRequestBodyUpdate = new Date()
  
  val get = createButton(clicked = launchConnectionFunc(verb = Get))
  val post = createButton(clicked = launchConnectionFunc(verb = Post))
  val put = createButton(clicked = launchConnectionFunc(verb = Put))
  val delete = createButton(clicked = launchConnectionFunc(verb = Delete))
  val cancel = createButton(enabled = false, clicked = () => reenable)
  
  val response = new OutputField(value = "", signalUpdate = this.updateView)
  
  val inputs = Array(url, headers, requestBody, get, post, put, delete)
  
  private def updateView(updatedInput: Updatable) {
    UserInterface.valueChanged(updatedInput)
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
  
  private def reenable() {
    inputs.foreach(_.enabled = true)
    cancel.enabled = false
  }
  
  private def launchConnectionFunc(verb: Verb): () => Unit = () => {
    if(this.allValid) {
      inputs.foreach(_.enabled = false)
      cancel.enabled = true
      response.value = "Waiting for response..."
      val futureResponse = future {
        (
          new Date(),
          Attempt.launchConnection(
              new Request(
                  verb = verb,
                  url = url.underlyingValue,
                  headers = headers.underlyingValue,
                  body = requestBody.underlyingValue
                )
            )
        )
      }
      val displayMsgAndReenable = (timeReqLaunched: Date, msg: String) => {
        if(timeReqLaunched after lastRequestBodyUpdate) {
          lastRequestBodyUpdate = timeReqLaunched
          response.value = msg
          reenable()
        }
      }
      futureResponse respond {
        case (d: Date, Some(resp)) => displayMsgAndReenable(d, resp.toString)
        case (d: Date, None) => displayMsgAndReenable(d, "Some sort of error occurred")
      }
    }
  } 
}