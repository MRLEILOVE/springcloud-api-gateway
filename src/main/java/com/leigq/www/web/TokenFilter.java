package com.leigq.www.web;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Token令牌过滤器
 * <p>
 * 创建人：LeiGQ <br>
 * 创建时间：2019-03-14 16:45 <br>
 * <p>
 * 修改人： <br>
 * 修改时间： <br>
 * 修改备注： <br>
 * </p>
 */
@Component
public class TokenFilter extends ZuulFilter {
    @Override
    public String filterType() {
        // 过滤器类型,前置类型
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        // 过滤器顺序，越小越靠前
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        // 具体过滤逻辑
        final RequestContext currentContext = RequestContext.getCurrentContext();
        final HttpServletRequest request = currentContext.getRequest();
        final String token = request.getParameter("token");
        if (StringUtils.isBlank(token)) {
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }
}
