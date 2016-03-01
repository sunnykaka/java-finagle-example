package com.akkafun.finagle.thrift;

import com.twitter.finagle.ListeningServer;
import com.twitter.finagle.Thrift;
import com.twitter.util.Await;
import com.twitter.util.Future;
import scala.runtime.BoxedUnit;
import thrift.DemoService;

/**
 * Created by liubin on 2016/3/1.
 */
public class ThriftServer implements DemoService<Future> {


    public static void main(String[] args) throws Exception {
        ListeningServer server = Thrift.serveIface("127.0.0.1:8081", new ThriftServer());

        Await.result(server);
    }

    @Override
    public Future<String> method1() {
        System.out.println("implement method1");
        return Future.value("abc");
    }

    @Override
    public Future<Integer> method2(int a, int b) {
        System.out.println("implement method2");
        return Future.value(a + b);
    }

    @Override
    public Future<BoxedUnit> method3() {
        System.out.println("implement method3");
        return Future.value(BoxedUnit.UNIT);
    }
}
