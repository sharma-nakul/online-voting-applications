package api;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Nakul Sharma on 10-04-2015.
 */

@Component
public class ScheduledTasks {
    private static final Logger logger = Logger.getLogger(ScheduledTasks.class);

    @Autowired
    PController pController;

    private ArrayList<String> getExpiredPolls() {

        ConcurrentHashMap<String, ArrayList<String>> expiredPolls = pController.getExpiredPoll();
        ArrayList<String> pollMessageList = new ArrayList<>();
        try {
            if (expiredPolls.isEmpty())
                logger.info("There is no poll expired recently!!");
            else {
                for (ArrayList<String> value : expiredPolls.values()) {
                    for (String msg : value) {
                        pollMessageList.add(msg);
                    }
                }
            }
        } catch (NullPointerException e) {
            logger.info("There is no expired poll found, NullPointerException generated");
        }

        return pollMessageList;
    }

    @Scheduled(fixedRate = 5000)
    public void reportExpiredPolls() {
        try {
            ArrayList<String> pollMessageList = getExpiredPolls();
            for (String msg : pollMessageList) {
                logger.info(msg);
                String topic = "cmpe273-topic";
                KafkaProducer kafka = new KafkaProducer("54.149.84.25:9092");
                kafka.send(topic, msg);
            }
        }
        catch (NullPointerException e)
        {
            logger.info("NullPointException generated while sending mail to Kafka server");
        }
    }
}
