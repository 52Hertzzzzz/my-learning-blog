package com.admin.service.impl;

import com.admin.service.StreamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description TODO
 * @Date 2024-02-17 19:32
 * @Author YanXing
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
public class StreamServiceImpl implements StreamService {

    @Override
    public void calculate() {
        StreamExecutionEnvironment execution = StreamExecutionEnvironment.getExecutionEnvironment();
        execution.setRuntimeMode(RuntimeExecutionMode.STREAMING);
        execution.addSource(new SourceFunction<Object>() {
            @Override
            public void run(SourceContext<Object> sourceContext) throws Exception {

            }

            @Override
            public void cancel() {

            }
        });
    }

}
