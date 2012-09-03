package com.beyondtechnicallycorrect.graphicalhttpclient.connection
import java.net.URL

final class Request(
  val verb: Verb,
  val url: URL,
  val headers: Iterable[(String, String)],
  val body: String
)