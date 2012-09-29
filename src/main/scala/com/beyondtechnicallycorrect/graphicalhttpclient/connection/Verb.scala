package com.beyondtechnicallycorrect.graphicalhttpclient.connection

sealed trait Verb { val verb: String }
case object Get extends Verb { val verb = "GET" }
case object Post extends Verb { val verb = "POST" }
case object Put extends Verb { val verb = "PUT" }
case object Delete extends Verb { val verb = "DELETE" }