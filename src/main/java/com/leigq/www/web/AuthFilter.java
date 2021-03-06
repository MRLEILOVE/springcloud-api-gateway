package com.leigq.www.web;

import com.leigq.www.constant.CookieConstant;
import com.leigq.www.constant.RedisConstant;
import com.leigq.www.util.CookieUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 权限过滤器
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
public class AuthFilter extends ZuulFilter {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String filterType() {
        // 过滤器类型,前置类型
        /*
        * Zuul一共有四种过滤：
        * 1、PRE：这种过滤器在请求被路由之前调用。我们可利用这种过滤器实现身份验证、在集群中选择请求的微服务、记录调试信息等。
        * 2、ROUTING：这种过滤器将请求路由到微服务。这种过滤器用于构建发送给微服务的请求，并使用Apache HttpClient或Netfilx Ribbon请求微服务。
        * 3、POST：这种过滤器在路由到微服务以后执行。这种过滤器可用来为响应添加标准的HTTP Header、收集统计信息和指标、将响应从微服务发送给客户端等。
        * 4、ERROR：在其他阶段发生错误时执行该过滤器。
        * */
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

        // 拦截路径
        final String uri = request.getRequestURI();
        final String url = request.getRequestURL().toString();

        System.out.println(uri);
        System.out.println(url);

        if (uri.startsWith("/commodity/commodities/")) {
            // 从Cookie拿
            final Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
            if (Objects.isNull(cookie)
                    || StringUtils.isBlank(cookie.getValue())
                    || StringUtils.isBlank(stringRedisTemplate.opsForValue()
                    .get(String.format(RedisConstant.USER_KEY, cookie.getValue())))) {
                currentContext.setSendZuulResponse(false);
                currentContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            }
        }
        return null;
    }
}
