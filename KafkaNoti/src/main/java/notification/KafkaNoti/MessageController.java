package notification.KafkaNoti;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("api/v1/messages")
public class MessageController {

    private final NotificationService notificationService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public MessageController(NotificationService notificationService, KafkaTemplate<String, String> kafkaTemplate) {
        this.notificationService = notificationService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public void publish(@RequestBody MessageRequest request) {
        kafkaTemplate.send("martial_art", request.message());
    }

    @GetMapping
    public List<String> getMessages() {
        List<String> messages = notificationService.getReceivedMessages();

        notificationService.handleKafkaMessage("Successful");

        return messages;
    }
}
