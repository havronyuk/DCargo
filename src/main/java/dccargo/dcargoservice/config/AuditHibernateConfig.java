package dccargo.dcargoservice.config;

import dccargo.dcargoservice.audit.AuditHibernateInterceptor;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuditHibernateConfig {

    private final AuditHibernateInterceptor interceptor;

    public AuditHibernateConfig(AuditHibernateInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Bean
    public HibernatePropertiesCustomizer auditHibernatePropertiesCustomizer() {
        return properties -> properties.put("hibernate.session_factory.interceptor", interceptor);
    }
}