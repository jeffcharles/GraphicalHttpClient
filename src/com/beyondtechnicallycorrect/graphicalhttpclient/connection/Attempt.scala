package com.beyondtechnicallycorrect.graphicalhttpclient.connection
import java.net.HttpURLConnection
import collection.JavaConversions._
import java.io.IOException
import java.net.SocketTimeoutException
import scala.io.Source

object Attempt {

  def launchConnection(request: Request): Option[Response] = {
    
    val connection = setUpConnection(request) 
    try {
      connection.connect()
    } catch {
      case e: SocketTimeoutException => return None
      case e: IOException => return None
    }
    Some(extractResponse(connection))
  }
  
  private def setUpConnection(request: Request): HttpURLConnection = {
    
    val connection: HttpURLConnection = request.url.openConnection match {
      case huc: HttpURLConnection => huc
      case _ => throw new ClassCastException
    }
    connection.setRequestMethod(request.verb.toString)
    request.headers.foreach(
        header => connection.addRequestProperty(header._1, header._2)
      ) 
    usingStream(connection.getOutputStream) {
      _.write(request.body.getBytes("UTF-8"))
    }
    connection
  }
  
  private def extractResponse(connection: HttpURLConnection): Response = {
    
    val statusCode = connection.getResponseCode
    val headers = connection.getHeaderFields.map(header => (header._1, header._2(0)))
    usingStream(connection.getInputStream) { s =>
      val responseBody = Source.fromInputStream(s).mkString("UTF-8")
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