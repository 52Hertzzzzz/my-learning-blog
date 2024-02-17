package com.admin.service.impl;

import com.admin.service.StreamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
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
    }

}
