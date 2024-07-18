val protobufVersion = "3.6.1"
val reactiveGrpc = "1.2.4"
val grpcCommonsVersion = "1.65.0"
val grpcVersion = "1.65.0"
val nettyEpollVersion = "4.1.33.Final"
val reactorCoreVersion = "3.2.6.RELEASE"

plugins {
    id("java")
    id("application")
    id("com.google.protobuf") version ("0.9.4")
    id("idea")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.api.grpc:proto-google-common-protos:$grpcCommonsVersion")
    implementation("io.grpc:grpc-netty:$grpcVersion")
    implementation("io.netty:netty-transport-native-epoll:$nettyEpollVersion")
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-stub:$grpcVersion")
    implementation("io.projectreactor:reactor-core:$reactorCoreVersion")
    implementation("com.salesforce.servicelibs:reactive-grpc-common:$reactiveGrpc")
    implementation("com.salesforce.servicelibs:reactor-grpc-stub:$reactiveGrpc")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

sourceSets {
    main {
        proto {
            srcDir("src/main/proto")
        }
    }

    test {
        proto {
            srcDir("src/test/proto")
        }
    }
}


protobuf {

    protoc {
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }

    plugins {

        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }

        create("reactor") {
            artifact = "com.salesforce.servicelibs:reactor-grpc:$reactiveGrpc"
        }

        generateProtoTasks {
            ofSourceSet("main").forEach {
                it.plugins {
                    this.register("grpc")
                    this.register("reactor")
                }
            }
        }

    }
}


idea {
    module {
        sourceDirs.plusAssign(file("src/main/proto"))
        generatedSourceDirs.plusAssign(file("build/generated/source/proto/main/grpc"))
        generatedSourceDirs.plusAssign(file("build/generated/source/proto/main/reactor"))
        generatedSourceDirs.plusAssign(file("build/generated/source/proto/main/java"))
    }
}

tasks.clean {
    this.delete("src/generated")
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}