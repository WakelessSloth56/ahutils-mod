package org.auioc.mods.ahutils.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class LogUtil {

    private static final Logger MOD_LOGGER = getNamedLogger("ahutils");

    private static StackTraceElement getCaller() {
        StackTraceElement[] callStack = Thread.currentThread().getStackTrace();

        StackTraceElement caller = null;

        String logClassName = LogUtil.class.getName();

        int i = 0;
        for (int len = callStack.length; i < len; i++) {
            if (logClassName.equals(callStack[i].getClassName())) {
                break;
            }
        }

        caller = callStack[i + 3];
        return caller;
    }

    public static Logger getLoggerByCaller() {
        return LogManager.getLogger(getCaller().getClassName());
    }

    public static Logger getNamedLogger(String name) {
        return LogManager.getLogger(name);
    }

    public static Logger getModLogger() {
        return MOD_LOGGER;
    }

    public static Marker getMarker(String marker) {
        return MarkerManager.getMarker(marker);
    }



    public static void trace(Object msg) {
        getLoggerByCaller().trace(msg);
    }

    public static void debug(Object msg) {
        getLoggerByCaller().debug(msg);
    }

    public static void info(Object msg) {
        getLoggerByCaller().info(msg);
    }

    public static void warn(Object msg) {
        getLoggerByCaller().warn(msg);
    }

    public static void error(Object msg) {
        getLoggerByCaller().error(msg);
    }

    public static void error(Object msg, Throwable t) {
        getLoggerByCaller().error(msg, t);
    }


    public static void trace(Marker marker, Object msg) {
        MOD_LOGGER.trace(marker, msg);
    }

    public static void debug(Marker marker, Object msg) {
        MOD_LOGGER.debug(marker, msg);
    }

    public static void info(Marker marker, Object msg) {
        MOD_LOGGER.info(marker, msg);
    }

    public static void warn(Marker marker, Object msg) {
        MOD_LOGGER.warn(marker, msg);
    }

    public static void error(Marker marker, Object msg) {
        MOD_LOGGER.error(marker, msg);
    }

    public static void error(Marker marker, Object msg, Throwable t) {
        MOD_LOGGER.error(marker, msg, t);
    }


    public static void trace(String name, Object msg) {
        getNamedLogger(name).trace(msg);
    }

    public static void debug(String name, Object msg) {
        getNamedLogger(name).debug(msg);
    }

    public static void info(String name, Object msg) {
        getNamedLogger(name).info(msg);
    }

    public static void warn(String name, Object msg) {
        getNamedLogger(name).warn(msg);
    }

    public static void error(String name, Object msg) {
        getNamedLogger(name).error(msg);
    }

    public static void error(String name, Object msg, Throwable t) {
        getNamedLogger(name).error(msg, t);
    }


    public static void trace(String name, Marker marker, Object msg) {
        getNamedLogger(name).trace(marker, msg);
    }

    public static void debug(String name, Marker marker, Object msg) {
        getNamedLogger(name).debug(marker, msg);
    }

    public static void info(String name, Marker marker, Object msg) {
        getNamedLogger(name).info(marker, msg);
    }

    public static void warn(String name, Marker marker, Object msg) {
        getNamedLogger(name).warn(marker, msg);
    }

    public static void error(String name, Marker marker, Object msg) {
        getNamedLogger(name).error(marker, msg);
    }

    public static void error(String name, Marker marker, Object msg, Throwable t) {
        getNamedLogger(name).error(marker, msg, t);
    }

}
