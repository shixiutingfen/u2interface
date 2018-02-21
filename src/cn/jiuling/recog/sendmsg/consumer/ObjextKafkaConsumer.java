package cn.jiuling.recog.sendmsg.consumer;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.loocme.sys.datastruct.Var;
import com.loocme.sys.util.StringUtil;

import cn.jiuling.recog.sendmsg.constants.MsgConst;
import cn.jiuling.recog.sendmsg.converse.IObjextDataConverse;
import cn.jiuling.recog.sendmsg.feature.IObjextFeatureHandler;
import cn.jiuling.recog.sendmsg.protocol.IObjextSendProtocol;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public class ObjextKafkaConsumer implements Runnable
{

    private final static Logger logger = Logger
            .getLogger(ObjextKafkaConsumer.class);

    private ConsumerConnector consumer;
    
    private AtomicInteger currIndex = new AtomicInteger(0);

    public ObjextKafkaConsumer()
    {
        Properties props = new Properties();

        props.put("zookeeper.connect", "192.168.0.39:32181");
        // properties.put("metadata.broker.list", "192.168.96.203:9092");
        props.put("group.id", "objext"); // 必须要使用别的组名称，
                                         // 如果生产者和消费者都在同一组，则不能访问同一组内的topic数据
        props.put("zookeeper.session.timeout.ms", "10000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        props.put("rebalance.backoff.ms", "2000");
        props.put("rebalance.max.retries", "10");
        /*
         * zookeeper中没有初始化的offset时，如果offset是以下值的回应：
         * smallest：自动复位offset为smallest的offset(默认)
         * largest：自动复位offset为largest的offset anything  else：向consumer抛出异常
         */
        props.put("auto.offset.reset", "smallest");
        // props.put("auto.commit.enable", "false");

        try
        {
            consumer = Consumer
                    .createJavaConsumerConnector(new ConsumerConfig(props));
        }
        catch (Exception e)
        {
            consumer = null;
            logger.error(e);
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        logger.info("启动接收kafka数据线程...." + (null == consumer ? "失败" : "成功"));
        if (null == consumer) return;
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put("objext", new Integer(1));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer
                .createMessageStreams(topicCountMap);
        KafkaStream<byte[], byte[]> stream = consumerMap.get("objext").get(0);
        ConsumerIterator<byte[], byte[]> it = stream.iterator();
        // 通过接口获取数据
        while (it.hasNext())
        {
            try
            {
                String rsvMsg = new String(it.next().message(), "UTF-8");
                this.dealMessage(rsvMsg);
            }
            catch (UnsupportedEncodingException e)
            {
                logger.error(e);
                e.printStackTrace();
            }
        }
    }

    private void dealMessage(String message)
    {
        if (StringUtil.isNull(message)) return;

        IObjextDataConverse converse = MsgConst.getDataConverse();
        if (null == converse)
        {
            logger.error(String.format(
                    "[%s] not found converse func, discard. ", message));
            return;
        }

        IObjextSendProtocol protocol = MsgConst.getSendProtocol();
        if (null == protocol)
        {
            logger.error(String.format(
                    "[%s] not found protocol func, discard. ", message));
            return;
        }

        IObjextFeatureHandler featureHandler = MsgConst.getFeatureHandler();

        Var messageVar = Var.fromJson(message);
        Var featureVar = featureHandler.getFeature(messageVar);
        int objType = messageVar.getInt("objType");
        Object converseData = null;
        switch (objType)
        {
            case MsgConst.OBJTYPE_PERSON:
                converseData = converse.getPerson(messageVar);
                break;
            case MsgConst.OBJTYPE_CAR:
                converseData = converse.getCar(messageVar);
                break;
            case MsgConst.OBJTYPE_BIKE:
                converseData = converse.getBick(messageVar);
                break;
            default:
                logger.error(String.format("[%s] objtype [%d] is not found.",
                        message, objType));
        }

        if (null == converseData)
        {
            logger.error(String.format("[%s] data converse is null, discard. ",
                    message));
            return;
        }

        int sendRet = protocol.send(objType, converseData);
        System.out.println("count==================" + currIndex.getAndIncrement());
        logger.info(String.format("[%s] send to protocol %s [%d]. ", message,
                0 == sendRet ? "success" : "failed", sendRet));

        if (0 == sendRet && null != featureVar && !featureVar.isNull())
        {
            featureHandler.sendFeature(featureVar);
        }
    }
}
