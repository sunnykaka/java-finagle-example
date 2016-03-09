package com.akkafun.finagle

import java.net.InetSocketAddress

import com.twitter.finagle.http.Version.Http11
import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.finagle.stats.DefaultStatsReceiver
import com.twitter.finagle.zipkin.thrift.ZipkinTracer
import com.twitter.finagle.{Http, Service}
import com.twitter.util.{Await, Future}

/**
  * Created by liubin on 2016/3/1.
  */
class ScalaServer extends Service[Request, Response] {
  override def apply(request: Request): Future[Response] = {
    Future {
      println("request: " + request.getContentString)
      val response = Response(Http11, Status.Ok)
      response.setContentString(request.getContentString())
      response
    }
  }
}

object ScalaServer extends App {
  val service = new ScalaServer

  val server = Http.server.
    withLabel("echo-server").
    withTracer(ZipkinTracer.mk("192.168.99.100", 9410, DefaultStatsReceiver, 1.0f)).
    serve(new InetSocketAddress(8081), service);

  Await.result(server);
}


