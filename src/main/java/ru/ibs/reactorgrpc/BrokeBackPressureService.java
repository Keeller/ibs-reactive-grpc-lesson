package ru.ibs.reactorgrpc;

import com.google.protobuf.Empty;
import io.grpc.netty.NettyServerBuilder;
import reactor.core.publisher.Flux;
import ru.ibs.reactorgrpc.stubs.BrokeResponse;
import ru.ibs.reactorgrpc.stubs.ReactorBrokeBackPressureServiceGrpc;

import java.io.IOException;

public class BrokeBackPressureService extends ReactorBrokeBackPressureServiceGrpc.BrokeBackPressureServiceImplBase {
    @Override
    public Flux<BrokeResponse> brokeBackPressure(Empty request) {
        return Flux.range(0, Integer.MAX_VALUE)
                .log()
                .map(el -> BrokeResponse.newBuilder()
                        .setNum(el)
                        .build());
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        var builder = NettyServerBuilder.forPort(8070)
                .addService(new BrokeBackPressureService());
        builder.build().start().awaitTermination();
    }
}
