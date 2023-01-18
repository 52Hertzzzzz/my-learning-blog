package com.producer.serivce;

import com.blog.entity.MessageEntity;

public interface ProducerService {

    void sendTopic1(MessageEntity entity);

    void sendTopic2(MessageEntity entity);

}
