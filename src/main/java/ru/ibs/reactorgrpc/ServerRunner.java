package ru.ibs.reactorgrpc;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class ServerRunner {
    public static void main(String[] args) throws InterruptedException {

        Flux<Integer> source = Flux.range(1, 3)
                .doOnSubscribe(s -> System.out.println("subscribed to source"));

        Flux<Integer> autoCo = source.publish().autoConnect(2);

        autoCo.publishOn(Schedulers.single()).subscribe(el->{
            System.out.println(el);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, e -> {}, () -> {});
        System.out.println("subscribed first");
        Thread.sleep(500);
        System.out.println("subscribing second");
        autoCo.subscribe(System.out::println, e -> {}, () -> {});
    }
}