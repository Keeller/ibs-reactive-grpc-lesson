package ru.ibs.reactorgrpc;

import com.google.protobuf.Empty;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import ru.ibs.reactorgrpc.stubs.ReactorBrokeBackPressureServiceGrpc;

import java.util.concurrent.ThreadFactory;

public class BrokeBackPressureClient {

    public static void main(String[] args) {
        ThreadFactory tf = new DefaultThreadFactory("client-elg-", true /*daemon */);
        NioEventLoopGroup worker = new NioEventLoopGroup(0, tf);
        var mannagedChamnnnels = NettyChannelBuilder.forAddress("127.0.0.1", 8070)
                .channelType(NioSocketChannel.class)
                .eventLoopGroup(worker)
                .usePlaintext()
                .build();

        var stub = ReactorBrokeBackPressureServiceGrpc.newReactorStub(mannagedChamnnnels);
        var dispose = stub.brokeBackPressure(Empty.getDefaultInstance())
                .log()
                .subscribe(
                        el -> {},
                        Throwable::printStackTrace,
                        () -> System.out.println("complete recieved"),
                        subscription -> {
                            subscription.request(10);
                        }
                );


    }
}
