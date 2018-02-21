package cn.jiuling.recog.sendmsg.feature.impl;

import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.loocme.sys.datastruct.Var;
import com.loocme.sys.util.PatternUtil;
import com.loocme.sys.util.StringUtil;

import cn.jiuling.recog.sendmsg.constants.MsgConst;
import cn.jiuling.recog.sendmsg.feature.IObjextFeatureHandler;

public class FeatureHandlerKafka
        implements IObjextFeatureHandler, java.io.Serializable
{

    private static final long serialVersionUID = 1L;

    private Properties props = null;
    private KafkaProducer<String, String> producer;
    private String TOPIC_FEATURE = "feature";

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
        
        String brokerList = params.getString("brokerList");
        MsgConst.FEATURE_KAFKA_IP = PatternUtil.getMatch(brokerList, "^([^:]+):.+$", Pattern.CASE_INSENSITIVE, 1);

        producer = new KafkaProducer<String, String>(props);
        String topicFeature = params.getString("topicFeature");
        if (StringUtil.isNotNull(topicFeature))
        {
            TOPIC_FEATURE = topicFeature;
        }
    }

    @Override
    public Var getFeature(Var msgVar)
    {
        Var featureVar = Var.newObject();
        featureVar.set("uuid", msgVar.getString("uuid"));
        featureVar.set("objType", msgVar.get("objType"));
        featureVar.set("serialNumber", msgVar.getString("serialNumber"));
        featureVar.set("createTime", msgVar.getString("createTime"));
        featureVar.set("features.featureData", msgVar.getString("features.featureData"));
        msgVar.set("features.featureData", "");
        return featureVar;
    }

    @Override
    public int sendFeature(Var feature)
    {
        if (null == producer)
        {
            return 1;
        }

        producer.send(new ProducerRecord<String, String>(TOPIC_FEATURE, feature.toString() + "\0"));
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
