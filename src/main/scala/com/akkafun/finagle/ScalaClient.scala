package com.akkafun.finagle

import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

import com.twitter.finagle.Http
import com.twitter.finagle.http.Method.Post
import com.twitter.finagle.http.Request
import com.twitter.finagle.http.Version.Http11
import com.twitter.finagle.stats.DefaultStatsReceiver
import com.twitter.finagle.zipkin.thrift.ZipkinTracer
import com.twitter.io.Reader
import com.twitter.util.Await

/**
  * Created by liubin on 2016/3/1.
  */
object ScalaClient extends App{

  val service = Http.client.
    withLabel("echo-client").
    withTracer(ZipkinTracer.mk("192.168.99.100", 9410, DefaultStatsReceiver, 1.0f)).
    newService("127.0.0.1:8081");

  //create a "Greetings!" request.
  val data = Reader.fromStream(new ByteArrayInputStream("Greetings!".getBytes(StandardCharsets.UTF_8)));
  val request = Request(Http11, Post, "/", data);

  Await.ready(service(request)) onSuccess {response =>
    println(s"response status: ${response.status}, response string: ${response.contentString}")
  } onFailure {e =>
    println("error: " + e.toString)
  } ensure {
    service.close()
  }

}
