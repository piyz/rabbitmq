package by.matrosov.appl.generators;

import java.util.Random;

public class RandomGeneratorImpl implements RandomGenerator {

    @Override
    public String generateRandomString() {
        String s = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        while (sb.length() < 8){
            int index = (int) (Math.random() * s.length());
            sb.append(s, index, index + 1);
        }
        return sb.toString();
    }

    @Override
    public Number generateRandomNumber() {
        return new Random().nextInt(50) + 1;
    }
}
