package tech.corefinance.common.context;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TenantContext {

    private static final TenantContext INSTANCE = new TenantContext();

    private ThreadLocal<String> tenantIdThreadLocal = new InheritableThreadLocal<>();

    private TenantContext() {
        // Singleton
        log.debug("Created TenantContext [{}]", this);
    }

    public static TenantContext getInstance() {
        return INSTANCE;
    }

    public void setTenantId(String tenantId) {
        tenantIdThreadLocal.set(tenantId);
    }

    public String getTenantId() {
        return tenantIdThreadLocal.get();
    }

    public void clearTenantId() {
        tenantIdThreadLocal.remove();
    }
}
