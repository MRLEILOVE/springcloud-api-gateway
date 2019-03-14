package com.leigq.www.web;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 添加响应头过滤器，在路由到微服务以后执行
 * <p>
 * 创建人：LeiGQ <br>
 * 创建时间：2019-03-14 17:13 <br>
 * <p>
 * 修改人： <br>
 * 修改时间： <br>
 * 修改备注： <br>
 * </p>
 */
@Component
public class AddResponseHeadFilter extends ZuulFilter {
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
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        // 具体过滤逻辑
        final RequestContext currentContext = RequestContext.getCurrentContext();
        final HttpServletResponse response = currentContext.getResponse();
        response.setHeader("X-Foo", UUID.randomUUID().toString());
        return null;
    }
}
