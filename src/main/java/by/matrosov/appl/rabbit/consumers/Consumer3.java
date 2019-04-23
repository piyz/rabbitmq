package by.matrosov.appl.rabbit.consumers;

import by.matrosov.appl.rabbit.RabbitMQ;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Consumer3 extends RabbitMQ {

    public Consumer3() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_FANOUT,"fanout");
        channel.queueBind(ALL_QUEUE, EXCHANGE_FANOUT, "");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");
        };

        boolean noAck = true;
        channel.basicConsume(ALL_QUEUE, noAck, deliverCallback, consumerTag -> { });
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        new Consumer3();
    }
}
