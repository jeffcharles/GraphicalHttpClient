package com.beyondtechnicallycorrect.graphicalhttpclient.connection
import com.beyondtechnicallycorrect.graphicalhttpclient.Prelude._

final class Response (
  val statusCode: Int,
  val headers: Iterable[(String, String)],
  val body: String
) {
  
  override def toString(): String = {
    val sb = new StringBuilder
    sb.append(statusCode)
    sb.append(newline)
    for(header <- headers) {
      sb.append(header._1)
      sb.append(":")
      sb.append(header._2)
      sb.append(newline)
    }
    sb.append(newline)
    sb.append(body)
    sb.toString
  }
}