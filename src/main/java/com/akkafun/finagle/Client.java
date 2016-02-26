package com.akkafun.finagle;

import com.twitter.finagle.Http;
import com.twitter.finagle.Service;
import com.twitter.finagle.http.*;
import com.twitter.finagle.stats.DefaultStatsReceiver$;
import com.twitter.finagle.zipkin.thrift.ZipkinTracer;
import com.twitter.io.Reader;
import com.twitter.io.Reader$;
import com.twitter.util.Await;
import com.twitter.util.Future;
import com.twitter.util.TimeoutException;
import scala.runtime.AbstractFunction0;
import scala.runtime.BoxedUnit;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static scala.compat.java8.JFunction.*;

/**
 * Created by liubin on 2016/2/25.
 */
public class Client {

    public static void main(String[] args) throws TimeoutException, InterruptedException {
        Service<Request, Response> service = Http.client().
                withLabel("echo-client").
                withTracer(ZipkinTracer.mk("192.168.99.100", 9410, DefaultStatsReceiver$.MODULE$, 1.0f)).
                newService("127.0.0.1:8081");

        //create a "Greetings!" request.
        Reader data = Reader$.MODULE$.fromStream(new ByteArrayInputStream("Greetings!".getBytes(StandardCharsets.UTF_8)));
        Request request = Request.apply(Version.Http11$.MODULE$, Method.Post$.MODULE$, "/", data);

        Future<Response> responseFuture = Await.ready(service.apply(request));
        responseFuture.onSuccess(func(response -> {
            System.out.println(String.format("response status: %s, response string: %s",
                    response.status().toString(), response.contentString()));
            return BoxedUnit.UNIT;
        }));
        responseFuture.onFailure(func(e -> {
            System.out.println("error: " + e.toString());
            return BoxedUnit.UNIT;
        }));
        responseFuture.ensure(func(() -> {
            service.close();
            return BoxedUnit.UNIT;
        }));
        /**
         * @see https://groups.google.com/forum/#!topic/scala-user/3cxrabsFmAY
         */
//        responseFuture.ensure(new AbstractFunction0<BoxedUnit>() {
//            @Override
//            public BoxedUnit apply() {
//                service.close();
//                return BoxedUnit.UNIT;
//            }
//        });


    }
}
