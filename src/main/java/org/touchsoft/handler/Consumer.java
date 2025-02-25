package org.touchsoft.handler;

import org.touchsoft.dto.TimestampedNumber;

import java.io.IOException;
import java.io.InputStream;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Properties;

public class Consumer {
    private final Deque<TimestampedNumber> deque;
    private long sum;
    private final long intervalInMillis;

    public Consumer() {
        this("config.properties");
    }

    public Consumer(String propertiesFileName) {
        deque = new LinkedList<>();
        sum = 0;
        this.intervalInMillis = loadIntervalFromProperties(propertiesFileName);
    }

    private long loadIntervalFromProperties(String propertiesFileName) {
        Properties properties = new Properties();
        try (InputStream input = Consumer.class.getClassLoader().getResourceAsStream(propertiesFileName)) {
            if (input == null) {
                throw new IOException("Unable to find " + propertiesFileName);
            }
            properties.load(input);
            String intervalString = properties.getProperty("intervalInMillis");
            return Long.parseLong(intervalString);
        } catch (IOException | NumberFormatException e) {
            // Exception handling and setting the default value
            System.err.println("Error loading intervalInMillis from properties file: " + e.getMessage());
            // Set the default value in case of an error
            return 300000; // 5 minutes in milliseconds
        }
    }

    public void accept(int number) {
        long currentTime = System.currentTimeMillis();
        deque.offerLast(new TimestampedNumber(number, currentTime));
        sum += number;
        removeExpiredNumbers(currentTime);
    }

    public double mean() {
        long currentTime = System.currentTimeMillis();
        removeExpiredNumbers(currentTime);
        if (deque.isEmpty()) {
            return 0.0;
        }
        return (double) sum / deque.size();
    }

    private void removeExpiredNumbers(long currentTime) {
        while (!deque.isEmpty()) {
            TimestampedNumber first = deque.peekFirst();
            if (first == null) {
                break; // Precaution: exit the loop if the element is null
            }

            if (currentTime - first.getTimestamp() > intervalInMillis) {
                sum -= deque.pollFirst().getNumber();
            } else {
                break; // Exit the loop if there are no more expired elements
            }
        }
    }

    public long getIntervalInMillis() {
        return intervalInMillis;
    }
}