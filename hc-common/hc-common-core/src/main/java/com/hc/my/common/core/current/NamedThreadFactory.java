package com.hc.my.common.core.current;

import java.io.Serializable;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 14:05
 * 描述:
 **/
public final class NamedThreadFactory implements ThreadFactory,
        Serializable {

    private static final long          serialVersionUID      = -380843684411677285L;
    /**  */
    public static final  String        ARRAY_SUFFIX          = "[]";
    /**  */
    private static final char          PACKAGE_SEPARATOR     = '.';
    /**  */
    private static final char          INNER_CLASS_SEPARATOR = '$';
    /**  */
    public static final  String        CGLIB_CLASS_SEPARATOR = "$$";
    /**  */
    public static final  String        CLASS_FILE_SUFFIX     = ".class";
    /**  */
    private              String        threadNamePrefix;
    /**  */
    private              int           threadPriority        = Thread.NORM_PRIORITY;
    /**  */
    private              boolean       daemon                = false;
    /**  */
    private              ThreadGroup   threadGroup;
    /**  */
    private final AtomicInteger threadCount           = new AtomicInteger(0);

    public NamedThreadFactory() {
        this.threadNamePrefix = getDefaultThreadNamePrefix();
    }

    public NamedThreadFactory(String threadNamePrefix) {
        this.threadNamePrefix = (threadNamePrefix != null ? threadNamePrefix : getDefaultThreadNamePrefix());
    }

    public void setThreadNamePrefix(String threadNamePrefix) {
        this.threadNamePrefix = (threadNamePrefix != null ? threadNamePrefix : getDefaultThreadNamePrefix());
    }

    public String getThreadNamePrefix() {
        return this.threadNamePrefix;
    }

    public void setThreadPriority(int threadPriority) {
        this.threadPriority = threadPriority;
    }

    public int getThreadPriority() {
        return this.threadPriority;
    }

    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }

    public boolean isDaemon() {
        return this.daemon;
    }

    public void setThreadGroupName(String name) {
        this.threadGroup = new ThreadGroup(name);
    }

    public void setThreadGroup(ThreadGroup threadGroup) {
        this.threadGroup = threadGroup;
    }

    public ThreadGroup getThreadGroup() {
        return this.threadGroup;
    }

    public Thread createThread(Runnable runnable) {
        Thread thread = new Thread(getThreadGroup(), runnable, nextThreadName());
        thread.setPriority(getThreadPriority());
        thread.setDaemon(isDaemon());
        return thread;
    }

    protected String nextThreadName() {
        return getThreadNamePrefix() + this.threadCount.incrementAndGet();
    }

    protected String getDefaultThreadNamePrefix() {
        return getShortName(getClass()) + "-";
    }

    public static String getShortName(Class<?> clazz) {
        return getShortName(getQualifiedName(clazz));
    }

    public static String getShortName(String className) {
        int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
        int nameEndIndex = className.indexOf(CGLIB_CLASS_SEPARATOR);
        if (nameEndIndex == -1) {
            nameEndIndex = className.length();
        }
        String shortName = className.substring(lastDotIndex + 1, nameEndIndex);
        shortName = shortName.replace(INNER_CLASS_SEPARATOR, PACKAGE_SEPARATOR);
        return shortName;
    }

    public static String getQualifiedName(Class<?> clazz) {
        if (clazz.isArray()) {
            return getQualifiedNameForArray(clazz);
        } else {
            return clazz.getName();
        }
    }

    private static String getQualifiedNameForArray(Class<?> clazz) {
        StringBuilder result = new StringBuilder();
        while (clazz.isArray()) {
            clazz = clazz.getComponentType();
            result.append(ARRAY_SUFFIX);
        }
        result.insert(0, clazz.getName());
        return result.toString();
    }

    @Override
    public Thread newThread(Runnable runnable) {
        return createThread(runnable);
    }
}

