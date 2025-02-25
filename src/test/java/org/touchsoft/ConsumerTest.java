package org.touchsoft;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.touchsoft.handler.Consumer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsumerTest {
    private Consumer consumer;
    private final long testDelay;

    ConsumerTest() {
        Properties properties = new Properties();
        testDelay = getTestDelay(properties);
    }


    @BeforeEach
    void setUp() {
        consumer = new Consumer("test-config.properties");
    }

    @Test
    void testAcceptAndMean_SingleValue() {
        consumer.accept(10);
        assertEquals(10.0, consumer.mean(), 0.0001);
    }

    @Test
    void testAcceptAndMean_MultipleValues() {
        consumer.accept(10);
        consumer.accept(20);
        assertEquals(15.0, consumer.mean(), 0.0001);
    }

    @Test
    void testMean_NoValues() {
        assertEquals(0.0, consumer.mean(), 0.0001);
    }

    @Test
    void testAcceptAndMean_ExpiredValues() throws InterruptedException {
        // Adding value that will be expired
        consumer.accept(10);

        // Simulate waiting for more than 5 seconds (so that some values expire)
        Thread.sleep(consumer.getIntervalInMillis() + testDelay);

        // Adding a new value in the last 5 seconds
        consumer.accept(20);

        // The mean should consider only the last value as the first one has expired
        assertEquals(20.0, consumer.mean(), 0.0001);
    }

    @Test
    void testAcceptAndMean_MixOfValidAndExpiredValues() throws InterruptedException {
        // Accept and add a large number of values
        int count = 100;
        long sum = 0;
        for (int i = 1; i <= count; i++) {
            consumer.accept(i);
            sum += i;
            Thread.sleep(10); // Small delay between adding values
        }

        // Verify that the mean value correctly considers only the current values
        double expectedMean = (double) sum / count; // Expect the mean to be calculated only for the last value
        assertEquals(expectedMean, consumer.mean(), 0.0001);

        // Simulate waiting for more than 5 seconds (so that some values expire)
        Thread.sleep(consumer.getIntervalInMillis() + 100);

        // Add a new value after the interval has expired
        consumer.accept(200);

        // Verify that the mean value correctly considers only the current values
        double expectedDelayMean = 200.0; // Expect the mean to be calculated only for the last value
        assertEquals(expectedDelayMean, consumer.mean(), 0.0001);
    }

    @Test
    void testAccept10MIntegers() throws InterruptedException {
        // Check the mean value for a large number of values
        int largeCount = 10_000_000;
        long sum = 0;
        for (int i = 1; i <= largeCount; i++) {
            consumer.accept(i);
            sum += i;
        }

        // Check the mean value for a large number of values
        double expectedMean = (double) sum / largeCount;
        assertEquals(expectedMean, consumer.mean(), 0.0001);
    }

    private static long getTestDelay(Properties properties) {
        try (InputStream input = Consumer.class.getClassLoader().getResourceAsStream("test-config.properties")) {
            if (input == null) {
                throw new IOException("Unable to find config.properties");
            }
            properties.load(input);
            String intervalString = properties.getProperty("testDelay");
            return Long.parseLong(intervalString);
        } catch (IOException | NumberFormatException e) {
            // Exception handling and setting the default value
            System.err.println("Error loading intervalInMillis from properties file: " + e.getMessage());
            // Set the default value in case of an error
            return 100;
        }
    }
}
