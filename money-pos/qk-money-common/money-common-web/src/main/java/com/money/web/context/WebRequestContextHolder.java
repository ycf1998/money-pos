package com.money.web.context;

import lombok.experimental.UtilityClass;

/**
 * Web 请求上下文持有者
 *
 * @author : money
 * @since : 1.0.0
 */
@UtilityClass
public class WebRequestContextHolder {

    private final static ThreadLocal<WebRequestContext> CONTEXT = ThreadLocal.withInitial(WebRequestContext::new);

    public void setContext(WebRequestContext webRequestContext) {
        CONTEXT.set(webRequestContext);
    }

    public WebRequestContext getContext() {
        return CONTEXT.get();
    }

}
