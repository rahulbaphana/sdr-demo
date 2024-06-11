package com.rahul.sdr;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class RetryUtil {

    /**
     * Retries a statement until a condition is satisfied or a timeout is reached.
     *
     * @param supplier  The statement to be executed.
     * @param condition The condition to be satisfied.
     * @param timeout   The timeout duration.
     * @param interval  The interval between retries.
     * @param <T>       The type of the statement result.
     * @return The result of the statement if the condition is satisfied within the timeout.
     * @throws RuntimeException if the condition is not satisfied within the timeout.
     */
    public static <T> T retryUntil(Supplier<T> supplier, Predicate<T> condition, Duration timeout, Duration interval) {
        Instant end = Instant.now().plus(timeout);
        while (Instant.now().isBefore(end)) {
            T result = supplier.get();
            if (condition.test(result)) {
                return result;
            }
            try {
                Thread.sleep(interval.toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread was interrupted", e);
            }
        }
        throw new RuntimeException("Condition was not satisfied within the timeout");
    }
}