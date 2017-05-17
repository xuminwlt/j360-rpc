package me.j360.rpc.api;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;

/**
 * Package: me.j360.rpc.api
 * User: min_xu
 * Date: 2017/5/17 下午6:37
 * 说明：
 */
public class MainTest {

    public static void main(String[] args) throws InvalidProtocolBufferException {

        Sample.SampleRequest request = Sample.SampleRequest.newBuilder()
                .setA(1)
                .setB("hello").build();

        final JsonFormat.Printer printer = JsonFormat.printer().omittingInsignificantWhitespace();

        System.out.printf("request=%s ",
                printer.print(request));
    }
}
