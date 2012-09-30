package com.beyondtechnicallycorrect.graphicalhttpclient.connection
import java.net.HttpURLConnection
import collection.JavaConversions._
import java.io.IOException
import java.net.SocketTimeoutException
import scala.io.Source
import scala.actors.Actor

object Attempt {

  def launchConnection(request: Request): Option[Response] = {
    
    val connection = setUpConnection(request) 
    try {
      connection.connect()
      Some(extractResponse(connection))
    } catch {
      case e: SocketTimeoutException => return None
      case e: IOException => return None
    }
  }
  
  private def setUpConnection(request: Request): HttpURLConnection = {
    
    val connection: HttpURLConnection = request.url.openConnection match {
      case huc: HttpURLConnection => huc
      case _ => throw new ClassCastException
    }
    connection.setRequestMethod(request.verb.verb.toString)
    val timeoutInMillis = 15000
    connection.setReadTimeout(timeoutInMillis)
    request.headers.foreach(
        header => connection.addRequestProperty(header._1, header._2)
      )
    val requestHasBody = request.body != ""
    if(requestHasBody) {
      connection.setDoOutput(true)
      usingStream(connection.getOutputStream) {
        _.write(request.body.getBytes("UTF-8"))
      }
    }
    connection
  }
  
  private def extractResponse(connection: HttpURLConnection): Response = {
    
    val headers = connection.getHeaderFields.map(header => (header._1, header._2(0)))
    val statusCode = headers.get(null) match {
      case Some(sc) => sc
      case None => ""
    }
    headers -= null // remove since this is now duplicate data being stored in statusCode
    val contentType = headers.get("Content-Type") match {
      case Some(ct) => ct
      case None => ""
    }
    val contentTypePattern = """text.*;\s*charset=([^\s]+)""".r
    val encoding = contentType match {
      case contentTypePattern(enc) => Some(enc)
      case _ => None
    }
    
    usingStream(connection.getInputStream) { s =>
      val responseBody = encoding match {
        case Some(enc) => Source.fromInputStream(s, enc).mkString
        case None => Source.fromInputStream(s, "ISO-8859-1").mkString
      }
      new Response(statusCode, headers, responseBody)
    }
  }
  
  private def usingStream[T <% { def close(): Unit }, U](stream: T)(func: T => U): U = {
    try {
      func(stream)
    } finally {
      stream.close()
    }
  }
}