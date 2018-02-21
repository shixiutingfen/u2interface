package cn.jiuling.recog.sendmsg.protocol.impl;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.loocme.sys.datastruct.Var;
import com.loocme.sys.util.StringUtil;

import cn.jiuling.recog.sendmsg.constants.MsgConst;
import cn.jiuling.recog.sendmsg.protocol.IObjextSendProtocol;

public class SendProtocolKafkaNormal
        implements IObjextSendProtocol, java.io.Serializable
{

    private static final long serialVersionUID = 1L;

    private Properties props = null;
    private KafkaProducer<String, String> producer;
    private String TOPIC_PERSON = "person";
    private String TOPIC_BIKE = "bike";
    private String TOPIC_CAR = "car";

    @Override
    public void initParams(Var params)
    {
        this.props = new Properties();
        props.put("bootstrap.servers", params.getString("brokerList"));
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("acks", "all");
        props.put("retries", 3);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);

        producer = new KafkaProducer<String, String>(
                props);
        String topicPerson = params.getString("topicPerson");
        if (StringUtil.isNotNull(topicPerson))
        {
            TOPIC_PERSON = topicPerson;
        }
        String topicBike = params.getString("topicBike");
        if (StringUtil.isNotNull(topicBike))
        {
            TOPIC_BIKE = topicBike;
        }
        String topicCar = params.getString("topicCar");
        if (StringUtil.isNotNull(topicCar))
        {
            TOPIC_CAR = topicCar;
        }
    }

    @Override
    public int send(int objType, Object msgObj)
    {
        if (null == producer)
        {
            return 1;
        }

        if (MsgConst.OBJTYPE_PERSON == objType)
        {
            producer.send(
                    new ProducerRecord<String, String>(TOPIC_PERSON, msgObj.toString()));
        }
        else if (MsgConst.OBJTYPE_BIKE == objType)
        {
            producer.send(
                    new ProducerRecord<String, String>(TOPIC_BIKE, msgObj.toString()));
        }
        else if (MsgConst.OBJTYPE_CAR == objType)
        {
            producer.send(
                    new ProducerRecord<String, String>(TOPIC_CAR, msgObj.toString()));
        }
        producer.flush();
        return 0;
    }

    @Override
    public void close()
    {
        if (null != this.producer)
        {
            this.producer.close();
        }
    }

}
