package com.editor.utils;

import com.google.common.base.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingUtils {
    private final static Logger log = LoggerFactory.getLogger(LoggingUtils.class);

    public static void logTiming(String name, Runnable action) {
        long start = System.currentTimeMillis();
        try {
            action.run();
        } finally {
            long end = System.currentTimeMillis();
            log.info("Timing of '{}': {}ms", name, end - start);
        }
    }

    public static <T> T loggedTiming(String name, Supplier<T> action) {
        long start = System.currentTimeMillis();
        try {
            return action.get();
        } finally {
            long end = System.currentTimeMillis();
            log.debug("Timing of '{}': {}ms", name, end - start);
        }
    }
}
