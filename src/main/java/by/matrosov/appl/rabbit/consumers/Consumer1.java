package by.matrosov.appl.rabbit.consumers;

import by.matrosov.appl.rabbit.RabbitMQ;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Consumer1 extends RabbitMQ {

    private static final String bindingKey = "number";

    public Consumer1() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_DIRECT,"direct");
        channel.queueBind(NUMBER_QUEUE, EXCHANGE_DIRECT, bindingKey);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };

        boolean noAck = true;
        channel.basicConsume(NUMBER_QUEUE, noAck, deliverCallback, consumerTag -> { });
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        new Consumer1();
    }
}
