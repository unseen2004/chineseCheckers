package org.chinesecheckers.common;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Character.isDigit;

public class ClientMessage {
    private final String m_code;
    private final int[] m_numbers;
    private final String[] m_words;

    ClientMessage(String m_code, int[] m_numbers, String[] m_words) {
        this.m_code = m_code;
        this.m_numbers = m_numbers;
        this.m_words = m_words;
    }

    public static ClientMessage[] getResponses(String line) {
        line = line.replace("\n", "");

        String[] strResponses = line.split("@");

        List<ClientMessage> response = new ArrayList<>();
        for (String strResponse : strResponses) {
            String[] parts = strResponse.split(" ");

            if (parts.length < 1) throw new RuntimeException("Error Server Response: '" + line + "'");

            String code = parts[0];

            List<Integer> numbers = new ArrayList<>();
            List<String> words = new ArrayList<>();
            for (int i = 1; i < parts.length; i++) {
                if (isDigit(parts[i].charAt(0))) {
                    numbers.add(Integer.valueOf(parts[i]));
                } else if (parts[i].length() > 3 && parts[i].charAt(0) == '-' && isDigit(parts[i].charAt(1))) {
                    numbers.add(Integer.valueOf(parts[i]));
                } else {
                    words.add(parts[i]);
                }
            }

            int[] numbersArray = numbers.stream().mapToInt(i -> i).toArray();
            String[] wordsArray = words.toArray(new String[0]);
            ClientMessage clientMessage = new ClientMessage(code, numbersArray, wordsArray);

            response.add(clientMessage);
        }

        return response.toArray(new ClientMessage[0]);
    }

    public String getCode() {
        return m_code;
    }

    public int[] getNumbers() {
        return m_numbers;
    }

    public String[] getWords() {
        return m_words;
    }
}