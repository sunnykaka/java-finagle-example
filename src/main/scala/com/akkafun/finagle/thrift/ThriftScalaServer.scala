package com.akkafun.finagle.thrift

import _root_.thrift.DemoService
import com.twitter.finagle.Thrift
import com.twitter.util.{Await, Future}

/**
  * Created by liubin on 2016/3/1.
  */
class ThriftScalaServer extends DemoService[Future] {

  override def method1(): Future[String] = {
    println("implement method1")
    Future("abc")
  }

  override def method2(a: Int, b: Int): Future[Int] = {
    println("implement method2")
    Future(a + b)
  }

  override def method3(): Future[Unit] = {
    println("implement method3")
    Future.Unit
  }

}

object ThriftScalaServer extends App {

  val server = Thrift.serveIface("127.0.0.1:8081", new ThriftScalaServer)
  Await.result(server)

}


