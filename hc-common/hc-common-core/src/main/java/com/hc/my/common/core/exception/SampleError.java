package com.hc.my.common.core.exception;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 10:18
 * 描述:
 **/
public final class SampleError implements IError {

    private static final long   serialVersionUID = -2792027284525825398L;
    private              int    code;
    private              String text;

    public static SampleError create(String format, Object... args) {
        return create(102, format, args);
    }

    public static SampleError create(int code, String format, Object... args) {
        return new SampleError(code, IError.format(format, args));
    }

    private SampleError(int code, String text) {
        this.code = code;
        this.text = text;
    }

    @Override
    public int code() {
        return this.code;
    }

    @Override
    public String text() {
        return this.text;
    }

    @Override
    public String cause() {
        return this.text;
    }

}

