package com.admin.service.impl;

import com.admin.service.StreamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

/**
 * @Description TODO
 * @Date 2024-02-17 19:32
 * @Author YanXing
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
public class StreamServiceImpl implements StreamService {

    public static final String MASTER = "MASTER";

    public static final String SLAVE = "SLAVE";

    @Override
    public void calculate() {
        StreamExecutionEnvironment execution = StreamExecutionEnvironment.getExecutionEnvironment();
        execution.setRuntimeMode(RuntimeExecutionMode.STREAMING);

        //source
        DataStreamSource<String> source = execution.addSource(new SourceFunction<String>() {

            @Override
            public void run(SourceContext<String> sourceContext) throws Exception {
                sourceContext.emitWatermark(new Watermark(60 * 1000));
                IntStream.range(1, 100)
                         .boxed()
                         .map(String::valueOf)
                         .forEach(sourceContext::collect);
            }

            @Override
            public void cancel() {

            }

        });

        //operator
        SingleOutputStreamOperator<Pair<String, String>> operator = source.filter(one -> !Strings.isNullOrEmpty(one))
                                                                          .map(s -> {
                                                                              if (Integer.parseInt(s) % 10 == 0) {
                                                                                  return Pair.of(MASTER, s);
                                                                              }
                                                                              return Pair.of(SLAVE, s);
                                                                          })
                                                                          .keyBy(Pair::getLeft)
                                                                          .sum(1);

        //sink
        operator.print();

        //customer define sink
        //operator.addSink(new SinkFunction<Pair<String, String>>() {
        //    @Override
        //    public void invoke(Pair<String, String> value) throws Exception {
        //        SinkFunction.super.invoke(value);
        //    }
        //});

        //execute
        try {
            execution.execute();
        } catch (Exception e) {
            log.error("Flink task execute error:", e);
            throw new RuntimeException(e);
        }
    }

}
