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

            String connectionString = "Endpoint=sb://javaservicebus.servicebus.windows.net/;SharedAccessKeyName=javaqueuepolicy;SharedAccessKey=Q5VJN3BXXXc5ZICuMw6uw+mlXyR7z45w9+ASbHCfMIo=;EntityPath=javaqueue";
            ServiceBusSenderClient senderClient = new ServiceBusClientBuilder()
                    .connectionString(connectionString)
                    .sender()
                    .queueName("javaqueue")
                    .buildClient();

            ServiceBusMessage message = new ServiceBusMessage(jsonData.toString());
            message.setContentType("application/json");

            senderClient.sendMessage(message);
            senderClient.close();

            return ResponseEntity.ok("Data Sent To Queue");
        }
//        catch (IOException | ServiceBusException ex) {
        catch (Exception ex) {

            // Log the exception for debugging purposes
            ex.printStackTrace();

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

