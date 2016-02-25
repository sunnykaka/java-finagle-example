package com.akkafun.finagle;

import com.twitter.finagle.Http;
import com.twitter.finagle.ListeningServer;
import com.twitter.finagle.Service;
import com.twitter.finagle.http.Request;
import com.twitter.finagle.http.Response;
import com.twitter.finagle.http.Status;
import com.twitter.finagle.http.Version;
import com.twitter.finagle.stats.DefaultStatsReceiver$;
import com.twitter.finagle.zipkin.thrift.ZipkinTracer;
import com.twitter.util.Await;
import com.twitter.util.Future;

import java.net.InetSocketAddress;

/**
 * Created by liubin on 2016/2/25.
 */
public class Server extends Service<Request, Response> {

    @Override
    public Future<Response> apply(Request request) {
        System.out.println("request: " + request.getContentString());
        Response response = Response.apply(Version.Http11$.MODULE$, Status.Ok());
        response.setContentString(request.getContentString());
        return Future.value(response);
    }

    public static void main(String[] args) throws Exception {
        Server service = new Server();

        ListeningServer server = Http.server().
                withLabel("echo-server").
                withTracer(ZipkinTracer.mk("192.168.99.100", 9410, DefaultStatsReceiver$.MODULE$, 1.0f)).
                serve(new InetSocketAddress(8081), service);

        Await.result(server);
    }
}
