package com.akkafun.finagle

import _root_.thrift.DemoService
import com.twitter.finagle.Thrift
import com.twitter.util.{Await, Future}

/**
  * Created by liubin on 2016/3/1.
  */
class ScalaServer extends DemoService[Future] {

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

object ScalaServer extends App {

  val server = Thrift.serveIface("127.0.0.1:8081", new ScalaServer)
  Await.result(server)

}


