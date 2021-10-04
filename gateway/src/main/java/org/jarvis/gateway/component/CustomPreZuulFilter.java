package org.jarvis.gateway.component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * Created: KimChheng
 * Date: 01-May-2021 Sat
 * Time: 10:49 PM
 */
//@Component
public class CustomPreZuulFilter extends ZuulFilter {

    private static Logger logger = LoggerFactory.getLogger(CustomPreZuulFilter.class);

    @Value("${phstrore.client.id}")
    private String clientId;

    @Value("${phstrore.client.secret}")
    private String clientSecret;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return -2;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        if (ctx.getRequest().getRequestURI().contains("auth/user")) {
            String encoded = Base64.encodeBase64String((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));
            ctx.addZuulRequestHeader("Authorization", "Basic " + encoded);
        }
        return null;
    }
}
