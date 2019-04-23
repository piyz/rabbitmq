package by.matrosov.appl.rabbit.producer;

import by.matrosov.appl.generators.ChooseGeneratorImpl;
import by.matrosov.appl.generators.RandomGeneratorImpl;
import by.matrosov.appl.rabbit.RabbitMQ;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class Producer extends RabbitMQ {

    private static final boolean durable = true;
    private static final boolean exclusive = false;
    private static final boolean autodelete = false;

    private static String routingKey;
    private static String message;

    public Producer() throws IOException, TimeoutException, InterruptedException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try(Connection connection = factory.newConnection(); Channel channel = connection.createChannel()){

            channel.exchangeDeclare(EXCHANGE_DIRECT, "direct");
            channel.exchangeDeclare(EXCHANGE_FANOUT, "fanout");

            channel.queueDeclare(NUMBER_QUEUE, durable, exclusive, autodelete, null);
            channel.queueDeclare(STRING_QUEUE, durable, exclusive, autodelete, null);
            channel.queueDeclare(ALL_QUEUE, durable, exclusive, autodelete, null);

            RandomGeneratorImpl generator = new RandomGeneratorImpl();
            ChooseGeneratorImpl chooseGenerator = new ChooseGeneratorImpl();

            while (true){

                if (chooseGenerator.choose(new Random().nextInt(2))){
                    message = generator.generateRandomNumber().toString();
                }else {
                    message = generator.generateRandomString();
                }

                channel.basicPublish(EXCHANGE_FANOUT, "", null, message.getBytes());

                if (isNumber(message)){
                    routingKey = "number";
                }else {
                    routingKey = "string";
                }

                channel.basicPublish(EXCHANGE_DIRECT, routingKey, null, message.getBytes());
                System.out.println("[x] Sent '" + routingKey + ": " + message + "'");
                Thread.sleep(4000);
            }
        }
    }

    private boolean isNumber(String message){
        boolean flag = true;
        try{
            Integer.parseInt(message);
        }catch (Exception e){
            flag = false;
        }
        return flag;
    }

    public static void main(String[] args) throws InterruptedException, IOException, TimeoutException {
        new Producer();
    }
}
