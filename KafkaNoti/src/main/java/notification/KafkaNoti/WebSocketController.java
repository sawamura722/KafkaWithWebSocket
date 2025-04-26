package notification.KafkaNoti;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class WebSocketController {

    private final NotificationService notificationService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public WebSocketController(NotificationService notificationService, KafkaTemplate<String, String> kafkaTemplate) {
        this.notificationService = notificationService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @MessageMapping("/fetch-history")
    @SendToUser("/queue/history")
    public List<String> fetchHistory() {
        return notificationService.getReceivedMessages();
    }

    @MessageMapping("/send-message")
    public void sendMessage(String message) {
        // This method sends the message to Kafka
        kafkaTemplate.send("martial_art", message);
    }

    // Spring automatically prepends /user/{sessionId} to the destination.
    //
    //How It Works
    //The full destination path becomes: /user/{sessionId}/queue/history
    //When using the STOMP client in JavaScript, you subscribe to /user/queue/history
    //Spring handles directing the message to the correct user session
    //This is Spring's convention for user-targeted messages. You don't need to configure it explicitly in your WebSocketConfig - it's built into the framework.
}
