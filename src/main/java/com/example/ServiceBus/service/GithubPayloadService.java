package com.example.ServiceBus.service;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusException;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.example.ServiceBus.model.GithubPayload;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class GithubPayloadService {

    // You can use RestTemplate to send HTTP requests if needed
//    private final RestTemplate restTemplate;
//
//    @Autowired
//    public GithubPayloadService(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }

    public ResponseEntity<String> sendPayloadToServiceBus(GithubPayload payload) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonData = objectMapper.valueToTree(payload);
            String commitId = jsonData.get("after").asText();

            String connectionString = "Endpoint=sb://javaservicebus.servicebus.windows.net/;SharedAccessKeyName=javatopicpolicy;SharedAccessKey=Kk/4YJT6N0le4iXkGcuh/cwpvwhqIdhef+ASbA8UurI=;EntityPath=japatopic";
            ServiceBusSenderClient senderClient = new ServiceBusClientBuilder()
                    .connectionString(connectionString)
                    .sender()
                    .topicName("japatopic")
                    .buildClient();

            ServiceBusMessage message = new ServiceBusMessage(jsonData.toString());
            message.setContentType("application/json");

            senderClient.sendMessage(message);
            senderClient.close();

            return ResponseEntity.ok("Data Sent To Topic");
        }
//        catch (IOException | ServiceBusException ex) {
        catch (Exception ex) {
//            return ResponseEntity.ok(ex.toString());
            return ResponseEntity.status(500).body("Error: " + ex.getMessage());
        }
    }

//    public ResponseEntity<String> sendPayloadToServiceBus(GithubPayload payload) {
//        try {
//            // Perform any necessary processing on the GithubPayload object
//            // For example, you can access payload.getAction(), payload.getAfter(), etc.
//
//            // If you have Azure Service Bus integration code, you can send the payload here
//            // Replace the following code with your Azure Service Bus logic
//            // Example: serviceBusSender.send(payload);
//
//            // Simulate success
//            return ResponseEntity.ok("Data Sent To Topic");
//        } catch (Exception e) {
//            // Handle exceptions or errors here
//            // You can log the error and return an error response if needed
//            return ResponseEntity.status(500).body("Error: " + e.getMessage());
//        }
//    }
}

