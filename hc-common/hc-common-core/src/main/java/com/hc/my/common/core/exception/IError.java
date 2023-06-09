package com.hc.my.common.core.exception;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 10:14
 * 描述:
 **/
import java.io.Serializable;
import java.util.regex.Pattern;
public interface IError extends Serializable {
    /**
     * Error code
     * <p>
     * <pre>
     * advise:
     *     000            00
     * business code   error code
     * </pre>
     */
    int code();

    /**
     * Error message for client. Support {} template
     */
    String text();

    /**
     * Error message for developer. Support {} template
     */
    String cause();

    IError SYSTEM = new IError() {

        private static final long serialVersionUID = -5326097417886035981L;

        @Override
        public int code() {
            return 99999;
        }

        @Override
        public String text() {
            return "system error";
        }

        @Override
        public String cause() {
            return "system error";
        }
    };


    IError TOKEN = new IError() {

        private static final long serialVersionUID = -5326097417886035981L;

        @Override
        public int code() {
            return 9998;
        }

        @Override
        public String text() {
            return "token error";
        }

        @Override
        public String cause() {
            return "token error";
        }
    };


    IError TIMEOUT = new IError() {

        private static final long serialVersionUID = -5326097417886035981L;

        @Override
        public int code() {
            return 9997;
        }

        @Override
        public String text() {
            return "token time out";
        }

        @Override
        public String cause() {
            return "token time out";
        }
    };

    default String format(Object... args) {
        String formatter = cause();
        return format(formatter, args);
    }

    Pattern split = Pattern.compile("\\{\\s*}");

    static String format(String formatter, Object... args) {
        if (null == args || args.length < 1 || null == formatter || !split.matcher(formatter).find()) {
            return formatter;
        }
        StringBuilder bu = new StringBuilder();
        String[] cuts = formatter.split(split.pattern());
        for (int index = 0; index < cuts.length; index++) {
            bu.append(cuts[index]);
            if (index < args.length) {
                bu.append(args[index]);
            }
        }
        return bu.toString();
    }
}
