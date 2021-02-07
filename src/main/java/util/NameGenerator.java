package util;

import org.apache.log4j.Logger;

import java.util.Random;

public class NameGenerator {

    private static final Logger LOG = Logger.getLogger(NameGenerator.class);

    public String generateName() {

        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int length = 7;

        for(int i = 0; i < length; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }

}
