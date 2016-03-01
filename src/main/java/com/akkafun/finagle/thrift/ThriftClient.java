package com.akkafun.finagle.thrift;

import com.twitter.finagle.Thrift;
import com.twitter.util.Await;
import com.twitter.util.Future;
import scala.Function1;
import scala.runtime.BoxedUnit;
import thrift.DemoService;

import static scala.compat.java8.JFunction.func;


/**
 * Created by liubin on 2016/3/1.
 */
@SuppressWarnings("unchecked")
public class ThriftClient {

    static Function1<Throwable, BoxedUnit> errorFunc = func(e -> {
        System.out.println("error: " + e.toString());
        return BoxedUnit.UNIT;
    });


    public static void main(String[] args) throws Exception {
        DemoService<Future> demoService = Thrift.newIface("127.0.0.1:8081", DemoService.class);

        Future<String> future1 = demoService.method1();
        Future<Integer> future2 = demoService.method2(1, 2);
        Future<BoxedUnit> future3 = demoService.method3();

        future1.onSuccess(func(r -> {
            System.out.println(r);
            return BoxedUnit.UNIT;
        }));
        future1.onFailure(errorFunc);

        future2.onSuccess(func(r -> {
            System.out.println(r);
            return BoxedUnit.UNIT;
        }));
        future2.onFailure(errorFunc);

        future3.onSuccess(func(r -> {
            System.out.println(r);
            return BoxedUnit.UNIT;
        }));
        future3.onFailure(errorFunc);

        Await.ready(future1);
        Await.ready(future2);
        Await.ready(future3);

    }
}
