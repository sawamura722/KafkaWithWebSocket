package notification.KafkaNoti;

import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final List<String> receivedMessages = new ArrayList<>();

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public List<String> getReceivedMessages() {
        return new ArrayList<>(receivedMessages);  // Return a copy to avoid concurrent modification
    }

    public void sendNotification(String message) {
        messagingTemplate.convertAndSend("/topic/martial_art", message);
    }

    @KafkaListener(topics = "martial_art", groupId = "artId")
    public void handleKafkaMessage(String message) {
        System.out.println("Listener received: " + message);
        receivedMessages.add(message);
        // send message to WebSocket
        sendNotification(message);
    }


}
