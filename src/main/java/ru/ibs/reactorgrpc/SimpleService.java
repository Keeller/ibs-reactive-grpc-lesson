package ru.ibs.reactorgrpc;

import com.google.protobuf.Empty;
import io.grpc.netty.NettyServerBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.ibs.reactorgrpc.stubs.ReactorSimpleServiceGrpc;
import ru.ibs.reactorgrpc.stubs.SimpleRequest;
import ru.ibs.reactorgrpc.stubs.SimpleResponse;

import java.io.IOException;
import java.util.UUID;

public class SimpleService extends ReactorSimpleServiceGrpc.SimpleServiceImplBase {

    @Override
    public Mono<SimpleResponse> requestReply(SimpleRequest request) {
        return Mono.just(SimpleResponse.newBuilder().setResponseMessage(request.getRequestMessage()).build())
                .log();
    }

    @Override
    public Mono<Empty> fireAndForget(SimpleRequest request) {
        return Mono.just(Empty.getDefaultInstance());
    }

    @Override
    public Flux<SimpleResponse> requestStream(SimpleRequest request) {
        return Flux.range(0, 10)
                .map(el -> UUID.randomUUID().toString() + el)
                .map(el -> SimpleResponse.newBuilder().setResponseMessage(el).build())
                .log();
    }

    @Override
    public Mono<SimpleResponse> streamingRequestSingleResponse(Flux<SimpleRequest> request) {
        return request.reduce(new StringBuilder(),
                        (StringBuilder ac, SimpleRequest next) -> ac.append(next.getRequestMessage()))
                .map(StringBuilder::toString)
                .map(el -> SimpleResponse.newBuilder().setResponseMessage(el).build())
                .log();
    }

    @Override
    public Flux<SimpleResponse> streamingRequestAndResponse(Flux<SimpleRequest> request) {
        return request
                .map(el -> SimpleResponse.newBuilder().setResponseMessage(el.getRequestMessage()).build());
    }

    @Override
    protected Throwable onErrorMap(Throwable throwable) {
        return super.onErrorMap(throwable);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        var builder = NettyServerBuilder.forPort(8070)
                .addService(new SimpleService());
        builder.build().start().awaitTermination();
    }
}
