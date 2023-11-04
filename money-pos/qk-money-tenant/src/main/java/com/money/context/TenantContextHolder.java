package com.money.context;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 租户上下文
 * @createTime : 2022-01-01 17:44:11
 */
public class TenantContextHolder {

   private static final ThreadLocal<Long> tenantContext = new ThreadLocal<>();

   public static void setTenant(Long tenantId) {
       tenantContext.set(tenantId);
   }

   public static Long getTenant() {
       return tenantContext.get();
   }

}
