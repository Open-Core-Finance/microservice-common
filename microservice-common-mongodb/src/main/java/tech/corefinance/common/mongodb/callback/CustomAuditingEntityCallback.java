package tech.corefinance.common.mongodb.callback;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.auditing.IsNewAwareAuditingHandler;
import org.springframework.data.mongodb.core.mapping.event.AuditingEntityCallback;
import org.springframework.stereotype.Component;
import tech.corefinance.common.converter.ZonedDateTimeProvider;

@Component
@ConditionalOnProperty(name = "tech.corefinance.common-mongodb.converter.zonedDateTime", havingValue = "true", matchIfMissing = true)
public class CustomAuditingEntityCallback extends AuditingEntityCallback {

    /**
     * Creates a new {@link AuditingEntityCallback} using the given {@link ObjectFactory}.
     *
     * @param auditingHandlerFactory must not be {@literal null}.
     */
    public CustomAuditingEntityCallback(
            ObjectFactory<IsNewAwareAuditingHandler> auditingHandlerFactory) {
        super(new CustomerAuditingHandlerObjectFactory(auditingHandlerFactory));

    }

    static class CustomerAuditingHandlerObjectFactory implements ObjectFactory<IsNewAwareAuditingHandler> {
        ObjectFactory<IsNewAwareAuditingHandler> originalObject;
        DateTimeProvider dateTimeProvider;

        CustomerAuditingHandlerObjectFactory(ObjectFactory<IsNewAwareAuditingHandler> originalObject) {
            this.originalObject = originalObject;
            this.dateTimeProvider = new ZonedDateTimeProvider();
        }

        @Override
        public IsNewAwareAuditingHandler getObject() throws BeansException {
            var result = originalObject.getObject();
            result.setDateTimeProvider(dateTimeProvider);
            return result;
        }
    }
}
