package com.akkafun.finagle.thrift

import com.twitter.finagle.Thrift
import com.twitter.util.{Await, Future, Futures}
import thrift.DemoService

/**
  * Created by liubin on 2016/3/1.
  */
object ThriftScalaClient extends App{

  val client = Thrift.newIface[DemoService[Future]]("127.0.0.1:8081")

  val f1 = client.method1()
  val f2 = client.method2(1, 2)
  val f3 = client.method3()

  val result = Futures.join(f1, f2, f3)

  Await.ready(result).onSuccess {
    case (r1, r2, r3) => println(s"r1: $r1, r2: $r2, r3: $r3")
  }.onFailure(e => println("error: " + e))

}
