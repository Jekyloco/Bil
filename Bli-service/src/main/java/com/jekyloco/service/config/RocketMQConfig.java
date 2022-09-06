package com.jekyloco.service.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jekyloco.constant.UserMomentsConstant;
import com.jekyloco.domain.UserFollowing;
import com.jekyloco.domain.UserMoment;
import com.jekyloco.service.UserFollowingService;
import com.mysql.cj.util.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

//@Configuration
public class RocketMQConfig {

    @Value("${rocketmq.name.server.address}")
    private String nameServerAddr;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserFollowingService userFollowingService;


    @Bean("momentsProducer")
    public DefaultMQProducer momentsProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer(UserMomentsConstant.GROUP_MOMENTS);
        producer.setNamesrvAddr(nameServerAddr);
        producer.start();
        return producer;
    }

    @Bean("momentsConsumer")
    public DefaultMQPushConsumer momentsConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(UserMomentsConstant.GROUP_MOMENTS);
        consumer.setNamesrvAddr(nameServerAddr);
        consumer.subscribe(UserMomentsConstant.TOPIC_MOMENTS, "*");
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            MessageExt messageExt = msgs.get(0);
            if (messageExt == null) {
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
            String bodyStr = new String(messageExt.getBody());
            UserMoment userMoment = JSONObject.toJavaObject(JSONObject.parseObject(bodyStr), UserMoment.class);
            Long userId = userMoment.getUserId();
            List<UserFollowing> fanList = userFollowingService.getUserFans(userId);
            for (UserFollowing fan : fanList) {
                String key = "subscribed-" + fan.getUserId();
                String subscribedListStr = redisTemplate.opsForValue().get(key);
                List<UserMoment> subscribedList;
                if (StringUtils.isNullOrEmpty(subscribedListStr)) {
                    subscribedList = new ArrayList<>();
                } else {
                    subscribedList = JSONArray.parseArray(subscribedListStr, UserMoment.class);
                }
                subscribedList.add(userMoment);
                redisTemplate.opsForValue().set(key, JSONObject.toJSONString(subscribedList));
            }

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        return consumer;
    }
}
