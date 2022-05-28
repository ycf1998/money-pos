package com.money.common.context;

import lombok.experimental.UtilityClass;

/**
 * @author : money
 * @version : 1.0.0
 * @description : web请求上下文持有人
 * @createTime : 2022-05-02 11:23:36
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
