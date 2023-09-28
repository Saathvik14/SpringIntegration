package com.example.ServiceBus.service;

import com.azure.messaging.servicebus.*;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class ReceiverService {


    //Receiver logic for the payload
    private static final String connectionString = "Endpoint=sb://javaservicebus.servicebus.windows.net/;SharedAccessKeyName=javapolicy;SharedAccessKey=bE8c7BhDGiZ0oqG0XT0k9b2FNYJtxFuWH+ASbGOcFDs=;EntityPath=javatopic";
    private static final String queueName = "javatopic";

    public void receiveMessage() {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        ServiceBusProcessorClient processorClient = new ServiceBusClientBuilder()
                .connectionString(connectionString)
                .processor()
                .queueName(queueName)
                .processMessage(this::processMessage)
                .processError(context -> processError(context, countDownLatch))
                .buildProcessorClient();

        System.out.println("Starting message receiver...");
        processorClient.start();

        try {
            // Add any desired logic here, e.g., sleep for a period or perform other tasks.
            TimeUnit.SECONDS.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Stopping message receiver...");
        processorClient.close();
    }

    private void processError(ServiceBusErrorContext context, CountDownLatch countDownLatch) {
        // Add error handling logic here, if needed.
    }

    private void processMessage(ServiceBusReceivedMessageContext context) {
        ServiceBusReceivedMessage msg = context.getMessage();
        System.out.printf("Processing message. Session: %s, Sequence #: %s. Contents: %s%n",
                msg.getMessageId(), msg.getSequenceNumber(), msg.getBody());
    }
}