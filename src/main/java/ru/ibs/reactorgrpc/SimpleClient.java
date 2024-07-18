package ru.ibs.reactorgrpc;

import io.grpc.netty.NettyChannelBuilder;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import reactor.core.publisher.Flux;
import ru.ibs.reactorgrpc.stubs.ReactorSimpleServiceGrpc;
import ru.ibs.reactorgrpc.stubs.SimpleRequest;

import java.util.concurrent.ThreadFactory;

public class SimpleClient {

    public static void main(String[] args) {
        ThreadFactory tf = new DefaultThreadFactory("client-elg-", true /*daemon */);
        NioEventLoopGroup worker = new NioEventLoopGroup(0, tf);
        var mannagedChamnnnels = NettyChannelBuilder.forAddress("127.0.0.1", 8070)
                .channelType(NioSocketChannel.class)
                .eventLoopGroup(worker)
                .usePlaintext()
                .build();
        ReactorSimpleServiceGrpc.ReactorSimpleServiceStub serviceStub = ReactorSimpleServiceGrpc.newReactorStub(mannagedChamnnnels);
        /*
        serviceStub
                .requestReply(SimpleRequest.newBuilder().setRequestMessage("echo msg").build())
                .log()
                .block();




        serviceStub.fireAndForget(SimpleRequest.newBuilder().setRequestMessage("fire and forgrt").build())
                .doOnNext(el-> System.err.println("empty!!!"))
                .log()
                .block();

        serviceStub.requestStream(SimpleRequest.newBuilder().setRequestMessage("streammsg").build())
                .doOnNext(System.out::println)
                .log()
                .blockLast();


        serviceStub.streamingRequestAndResponse(Flux.range(0,10).map(el->SimpleRequest.newBuilder().setRequestMessage(el.toString()).build()))
                .doOnNext(el-> System.out.println(el.toString()))
                .log()
                .blockLast();
           */
        serviceStub.streamingRequestSingleResponse(Flux.range(0,10).map(el->SimpleRequest.newBuilder().setRequestMessage(el.toString()).build()))
                .doOnNext(el-> System.out.println(el.toString()))
                .log()
                .block();



    }
}
