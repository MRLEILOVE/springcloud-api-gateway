package com.leigq.www.web;

import com.google.common.util.concurrent.RateLimiter;
import com.leigq.www.web.exception.RateLimiterException;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * 限流过滤器，顺序应该放在请求转发前执行
 * <p>
 * 创建人：LeiGQ <br>
 * 创建时间：2019-03-14 17:23 <br>
 * <p>
 * 修改人： <br>
 * 修改时间： <br>
 * 修改备注： <br>
 * </p>
 */
@Component
public class RateLimiterFilter extends ZuulFilter {
    //速率限制器,每秒100个许可
    // 测试限流的时候，可以改成1个许可，然后不停F5刷新请求，就会看到效果
    private static final RateLimiter RATE_LIMITER = RateLimiter.create(100);

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SERVLET_DETECTION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        if (!RATE_LIMITER.tryAcquire()) {
            // 没有取到令牌
            throw new RateLimiterException("没有取到令牌, 请稍候重试！");
        }
        return null;
    }
}
