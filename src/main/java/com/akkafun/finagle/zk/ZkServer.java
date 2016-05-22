package com.akkafun.finagle.zk;

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
public class ZkServer extends Service<Request, Response> {

    public static final String ZOOKEEPER_DEST = "127.0.0.1:2181";

    public static final String SERVICE_PATH = "/services/echo";

    @Override
    public Future<Response> apply(Request request) {
        System.out.println("request: " + request.getContentString());
        Response response = Response.apply(Version.Http11$.MODULE$, Status.Ok());
        response.setContentString(request.getContentString());
        return Future.value(response);
    }

    public static void main(String[] args) throws Exception {
        ZkServer service = new ZkServer();

        //echoServicePath: zk!127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183!/services/echo/!0
        //syntax:          schema!host!path!shardId
        //schema:          for server, use zk, for client, use zk2
        //host:            zookeeper connection string
        //path:            service registration path in zookeeper
        //shardId:         it's used internally by Twitter, can be set to 0 in most cases
        String echoServicePath = buildProviderPath(SERVICE_PATH, ZOOKEEPER_DEST);

        ListeningServer server = Http.server().
                withLabel("echo-server").
                withTracer(ZipkinTracer.mk("192.168.99.100", 9410, DefaultStatsReceiver$.MODULE$, 1.0f)).
                serveAndAnnounce(echoServicePath, new InetSocketAddress(8081), service);

        Await.result(server);
    }

    public static String buildProviderPath(String servicePath, String zookeeperDest) {
        return String.format("zk!%s!%s!0", zookeeperDest, servicePath);
    }

    public static String buildConsumerPath(String servicePath, String zookeeperDest) {
        return String.format("zk2!%s!%s", zookeeperDest, servicePath);
    }


}
