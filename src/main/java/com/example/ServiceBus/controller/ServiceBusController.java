package com.example.ServiceBus.controller;

import com.example.ServiceBus.model.GithubPayload;
import com.example.ServiceBus.model.WeatherForecast;
import com.example.ServiceBus.service.GithubPayloadService;
import com.example.ServiceBus.service.ReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ServiceBusController {
    String sname="Saaaaathvikkk";

    private final GithubPayloadService payloadService;
    @Autowired
    public ServiceBusController(GithubPayloadService payloadService) {
        this.payloadService = payloadService;
    }

    @Autowired
    private ReceiverService receiverService;


    private static final String[] Summaries = {
            "Freezing", "Bracing", "Chilly", "Cool", "Mild", "Warm", "Balmy", "Hot", "Sweltering", "Scorching"
    };

    @GetMapping("/view")
    public List<WeatherForecast> getWeatherForecasts() {
        return Arrays.stream(Summaries)
                .map(summary -> new WeatherForecast(
                        LocalDate.now().plusDays(1),
                        ThreadLocalRandom.current().nextInt(-20, 55),
                        summary))
                .collect(Collectors.toList());
    }
    @PostMapping("/webhook") // http://localhost:8080/api/webhook
    public ResponseEntity<String> print(@RequestBody String requestBody) {
        System.out.println("###### Webhook #####");
        System.out.println("###### Webhook ##### " + requestBody);
        return new ResponseEntity<String >(requestBody, HttpStatus.OK);

    }


//    @PostMapping("/SentToServiceBus")
//    public ResponseEntity<String> sendToServiceBus(@RequestBody GithubPayload payload) {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode jsonData = objectMapper.readTree(String.valueOf(payload));
//            String commitId = jsonData.get("after").asText();
//
//            String connectionString = "Endpoint=sb://javaservicebus.servicebus.windows.net/;SharedAccessKeyName=javatopicpolicy;SharedAccessKey=Kk/4YJT6N0le4iXkGcuh/cwpvwhqIdhef+ASbA8UurI=;EntityPath=japatopic";
//            ServiceBusSenderClient senderClient = new ServiceBusClientBuilder()
//                    .connectionString(connectionString)
//                    .sender()
//                    .topicName("japatopic")
//                    .buildClient();
//
//            ServiceBusMessage message = new ServiceBusMessage(String.valueOf(payload));
//            message.setContentType("application/json");
//
//            senderClient.sendMessage(message);
//            senderClient.close();
//
//            return ResponseEntity.ok("Data Sent To Topic");
//        } catch (IOException | ServiceBusException ex) {
//            return ResponseEntity.ok(ex.toString());
//        }
//    }

    @PostMapping("/SentToServiceBus")
    public ResponseEntity<String> sendToServiceBus(@RequestBody GithubPayload payload) {
        return payloadService.sendPayloadToServiceBus(payload);
    }

    @PostMapping("/post")
    public ResponseEntity<List<WeatherForecast>> createPost(@RequestBody WeatherForecast weatherForecast) {
        List<WeatherForecast> weatherForecasts = new ArrayList<>();
        weatherForecasts.add(weatherForecast);
        return ResponseEntity.ok(weatherForecasts);
    }


    //Get Mapping to receive payload from service bus

    @GetMapping("/start")
    public void startReceivingMessages() {
        receiverService.receiveMessage();
    }

}

