package tech.corefinance.common.audit;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import tech.corefinance.common.annotation.CustomAuditor;
import tech.corefinance.common.context.ApplicationContextHolder;
import tech.corefinance.common.dto.BasicUserDto;
import tech.corefinance.common.enums.CustomAuditorField;
import tech.corefinance.common.ex.ServiceProcessingException;
import tech.corefinance.common.model.AuditableEntity;
import tech.corefinance.common.util.CoreFinanceUtil;

import java.io.Serializable;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Configurable
@Slf4j
@ConditionalOnProperty(name = "tech.corefinance.audit.enabled.basic-user", havingValue = "true", matchIfMissing = true)
public class EntityBasicUserAuditorListener {

    @PrePersist
    private void beforeInsert(Object obj) {
        log.debug("Before inserting {}", obj);
        var context = ApplicationContextHolder.getInstance().getApplicationContext();
        var userAuditorAware = context.getBean(BasicUserAuditorAware.class);
        var util = context.getBean(CoreFinanceUtil.class);
        var objClass = obj.getClass();
        var customAuditor = objClass.getAnnotation(CustomAuditor.class);
        if (obj instanceof AuditableEntity en) {
            setAuditorToAuditableEntity(en, userAuditorAware);
        } else if (customAuditor != null) {
            setAuditorToAuditableEntity(userAuditorAware, customAuditor, obj, objClass, util);
        } else {
            log.debug("{} is not auditable.", obj);
        }
    }

    @PreUpdate
    private void beforeUpdate(Object obj) {
        log.debug("Before updating {}", obj);
        var context = ApplicationContextHolder.getInstance().getApplicationContext();
        var userAuditorAware = context.getBean(BasicUserAuditorAware.class);
        var util = context.getBean(CoreFinanceUtil.class);
        var objClass = obj.getClass();
        var customAuditor = objClass.getAnnotation(CustomAuditor.class);
        if (obj instanceof AuditableEntity en) {
            updateAuditorToAuditableEntity(en, userAuditorAware);
        } else if (customAuditor != null) {
            updateAuditorToAuditableEntity(userAuditorAware, customAuditor, obj, objClass, util);
        } else {
            log.debug("{} is not auditable.", obj);
        }
    }

    private void setAuditorToAuditableEntity(AuditableEntity<Serializable> en, BasicUserAuditorAware userAuditorAware) {
        var auditor = userAuditorAware.getCurrentAuditor();
        if (en.getCreatedBy() == null) {
            log.debug("Setting {} to createdBy attribute.", auditor);
            auditor.ifPresent(en::setCreatedBy);
        } else {
            log.debug("createdBy attribute already set manually!");
        }
        log.debug("Setting {} to lastModifiedBy attribute.", auditor);
        auditor.ifPresent(en::setLastModifiedBy);
    }

    private void updateAuditorToAuditableEntity(AuditableEntity<Serializable> en,
                                                BasicUserAuditorAware userAuditorAware) {
        var auditor = userAuditorAware.getCurrentAuditor();
        log.debug("Setting {} to lastModifiedBy attribute.", auditor);
        auditor.ifPresent(en::setLastModifiedBy);
    }

    private void setAuditorToAuditableEntity(BasicUserAuditorAware userAuditorAware, CustomAuditor customAuditor,
                                             Object obj, Class<?> objClass, CoreFinanceUtil util) {
        var auditorOptional = userAuditorAware.getCurrentAuditor();
        if (auditorOptional.isPresent()) {
            var auditor = auditorOptional.get();
            try {
                var createdByField = util.findAnnotatedFieldOrName(obj, objClass, CreatedBy.class, "createdBy");
                createdByField.setAccessible(true);
                Object createdBy = null;
                if (createdByField instanceof Field f) {
                    createdBy = f.get(obj);
                } else {
                    createdBy = util.findGetterBySetter(obj, objClass,(Method) createdByField).invoke(obj);
                }
                var fieldName = (createdByField instanceof Field f) ? f.getName() : ((Executable) createdByField).getName();
                if (createdBy == null) {
                    var createdVal = retrieveAuditor(customAuditor.createdByType(), auditor);
                    log.debug("Setting [{}] to [{}] attribute.", auditor, fieldName);
                    util.triggerSetFieldValue(createdByField, obj, objClass, createdVal);
                } else {
                    log.debug("[{}] attribute already set manually!", fieldName);
                }
                var lastModifiedByVal = retrieveAuditor(customAuditor.lastModifiedByType(), auditor);
                var lastModifiedByField = util.findAnnotatedFieldOrName(obj, objClass, LastModifiedBy.class, "lastModifiedBy");
                lastModifiedByField.setAccessible(true);
                fieldName = (lastModifiedByField instanceof Field f) ? f.getName() : ((Executable) lastModifiedByField).getName();
                log.debug("Setting {} to {} attribute.", lastModifiedByVal, fieldName);
                util.triggerSetFieldValue(lastModifiedByField, obj, objClass, lastModifiedByVal);
            } catch (ReflectiveOperationException ex) {
                throw new ServiceProcessingException(ex.getMessage(), ex);
            }
        }
    }

    private void updateAuditorToAuditableEntity(BasicUserAuditorAware userAuditorAware, CustomAuditor customAuditor,
                                                Object obj, Class<?> objClass, CoreFinanceUtil util) {
        var auditorOptional = userAuditorAware.getCurrentAuditor();
        if (auditorOptional.isPresent()) {
            var auditor = auditorOptional.get();
            try {
                var lastModifiedByVal = retrieveAuditor(customAuditor.lastModifiedByType(), auditor);
                var lastModifiedByField =
                        util.findAnnotatedFieldOrName(obj, objClass, LastModifiedBy.class, "lastModifiedBy");
                lastModifiedByField.setAccessible(true);
                var fieldName = (lastModifiedByField instanceof Field f) ? f.getName() :
                        ((Executable) lastModifiedByField).getName();
                log.debug("Setting {} to {} attribute.", lastModifiedByVal, fieldName);
                util.triggerSetFieldValue(lastModifiedByField, obj, objClass, lastModifiedByVal);
            } catch (ReflectiveOperationException ex) {
                throw new ServiceProcessingException(ex.getMessage(), ex);
            }
        }
    }

    private Object retrieveAuditor(CustomAuditorField field, BasicUserDto auditor) {
        return switch (field) {
            case USER_ID -> auditor.getUserId();
            case USER_DISPLAY_NAME -> auditor.getDisplayName();
            case USERNAME -> auditor.getUsername();
            default -> auditor;
        };
    }
}
