package me.j360.rpc.client;

import com.google.common.collect.Lists;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import me.j360.rpc.api.Sample;
import me.j360.rpc.api.SampleService;
import me.j360.rpc.core.Consumer;
import me.j360.rpc.core.IpPort;

import java.util.List;

public class RPCClientTest {

    public static void main(String[] args) {
        RPCClientOption clientOption = new RPCClientOption();
        clientOption.setWriteTimeoutMillis(200);
        clientOption.setReadTimeoutMillis(500);


        List<Consumer> list = Lists.newArrayList();
        list.add(new Consumer("me.j360.rpc.api.SampleService",new IpPort("127.0.0.1",8766)));

        RPCClient rpcClient = new RPCClient(list, clientOption);

        // build request
        Sample.SampleRequest request = Sample.SampleRequest.newBuilder()
                .setA(1)
                .setB("hello").build();

        final JsonFormat.Printer printer = JsonFormat.printer().omittingInsignificantWhitespace();
        // sync call
        SampleService sampleService = RPCProxy.getProxy(rpcClient, SampleService.class);
        Sample.SampleResponse response = sampleService.sampleRPC(request);
        if (response != null) {
            try {
                System.out.printf("sync call service=SampleService.sampleRPC success, " +
                                "request=%s response=%s\n",
                        printer.print(request), printer.print(response));
            } catch (InvalidProtocolBufferException ex) {
                System.out.println(ex.getMessage());
            }

        } else {
            System.out.println("server error, service=SampleService.sampleRPC");
        }

        // async call
        RPCCallback callback = new RPCCallback<Sample.SampleResponse>() {
            @Override
            public void success(Sample.SampleResponse response) {
                try {
                    System.out.printf("async call SampleService.sampleRPC success, response=%s\n",
                            printer.print(response));
                } catch (InvalidProtocolBufferException ex) {
                    System.out.println(ex.getMessage());
                }
            }

            @Override
            public void fail(Throwable e) {
                System.out.printf("async call SampleService.sampleRPC failed, %s\n", e.getMessage());
            }
        };

        rpcClient.createAsync("SampleService.sampleRPC", request, callback);
    }

}
