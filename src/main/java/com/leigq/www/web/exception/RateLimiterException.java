package com.leigq.www.web.exception;

/**
 * 限流异常
 * <p>
 * 创建人：LeiGQ <br>
 * 创建时间：2019-03-14 17:29 <br>
 * <p>
 * 修改人： <br>
 * 修改时间： <br>
 * 修改备注： <br>
 * </p>
 */
public class RateLimiterException extends RuntimeException{
    public RateLimiterException() {
        super();
    }

    public RateLimiterException(String message) {
        super(message);
    }

    public RateLimiterException(String message, Throwable cause) {
        super(message, cause);
    }
}
