package com.hc.my.common.core.current;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 14:04
 * 描述:
 **/
public class LimitedQueue<E> extends LinkedBlockingQueue<E> {

    private static final long serialVersionUID = 7653998069916805368L;

    public LimitedQueue(int maxSize) {
        super(maxSize);
    }

    @Override
    public boolean offer(E e) {
        // turn offer() and add() into a blocking calls (unless interrupted)
        try {
            put(e);
            return true;
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        return false;
    }
}
