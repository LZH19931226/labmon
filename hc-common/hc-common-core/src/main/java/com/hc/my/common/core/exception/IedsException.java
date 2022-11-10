package com.hc.my.common.core.exception;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 10:14
 * 描述:
 **/
public class IedsException extends RuntimeException {

    private static final long   serialVersionUID = 2400008380982132191L;
    protected            IError error            = IError.SYSTEM;
    protected            IError token            = IError.TOKEN;
    /**
     * Message value parameters
     * <p>
     * <pre>
     * Such as:
     * <code>"message {} {}", 1, 2</code> print message 1 2
     * </pre>
     */
    public IedsException(IError error, Object... args) {
        super(error.format(args));
        this.error = error;
    }

    /**
     * Message value parameters
     * <p>
     * <pre>
     * Such as:
     * <code>"message {} {}", 1, 2</code> print message 1 2
     * </pre>
     */
    public IedsException(IError error, Throwable e, Object... args) {
        super(error.format(args), e);
        this.error = error;
    }

    public IedsException(String message, Object... args) {
        super(IError.format(message, args));
        this.error = SampleError.create(message, args);
    }


    public IedsException(String message, Throwable e, Object... args) {
        super(IError.format(message, args), e);
        this.error = SampleError.create(message, args);
    }


    public IedsException(Throwable e) {
        super(e);
    }

    public IError getError() {
        return error;
    }

    public int getCode() {
        return error.code();
    }

    public String getText() {
        return error.text();
    }
}
