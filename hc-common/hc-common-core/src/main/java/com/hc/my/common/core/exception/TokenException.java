package com.hc.my.common.core.exception;

public class TokenException extends  RuntimeException {

    private static final long   serialVersionUID = 2400008380982132191L;
    protected            IError error            = IError.SYSTEM;
    protected            IError token            = IError.TOKEN;
    public               LabSystemEnum           labSystemEnum;
    /**
     * Message value parameters
     * <p>
     * <pre>
     * Such as:
     * <code>"message {} {}", 1, 2</code> print message 1 2
     * </pre>
     */
    public TokenException(IError error, Object... args) {
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
    public TokenException(IError error, Throwable e, Object... args) {
        super(error.format(args), e);
        this.error = error;
    }

    public TokenException(String message, Object... args) {
        super(IError.format(message, args));
        this.error = SampleError.create(message, args);
    }

    public TokenException(LabSystemEnum labSystemEnum,Object... args){
        super(IError.format(labSystemEnum.getMessage(), args));
        this.error = SampleError.create(labSystemEnum.getMessage(), args);
    }



    public TokenException(String message, Throwable e, Object... args) {
        super(IError.format(message, args), e);
        this.error = SampleError.create(message, args);
    }


    public TokenException(Throwable e) {
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
