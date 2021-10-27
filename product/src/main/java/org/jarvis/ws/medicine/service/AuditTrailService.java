package org.jarvis.ws.medicine.service;

import org.apache.logging.log4j.util.Strings;
import org.jarvis.core.exception.AuditTrailException;
import org.jarvis.core.model.audit.AuditAction;
import org.jarvis.core.model.audit.AuditTrail;
import org.jarvis.core.model.audit.AuditTrailAction;
import org.jarvis.core.model.audit.AuditTrailValue;
import org.jarvis.core.model.audit.context.AuditContext;
import org.jarvis.core.util.LogSuffix;
import org.jarvis.core.util.StringUtil;
import org.jarvis.ws.medicine.helper.SecurityHelper;
import org.jarvis.ws.medicine.repository.AuditTrailRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Created: chheng
 * Date: 25-Sep-2020 Fri
 * Time: 14:37
 */
@Service
public class AuditTrailService {

    @Autowired
    private AuditTrailRepository repository;

    private static final LogSuffix LOG = LogSuffix.of(LoggerFactory.getLogger(AuditTrailService.class));

    public void auditInsert(String tableName, String entityId, AuditTrailValue auditTrailValues) {
        createAuditTrail(AuditAction.INSERT, tableName, entityId, Collections.singletonList(auditTrailValues));
    }

    public void auditUpdate(String tableName, String entityId, AuditTrailValue... auditTrailValues) {
        createAuditTrail(AuditAction.UPDATE, tableName, entityId, Arrays.asList(auditTrailValues));
    }

    public void auditUpdate(String tableName, String entityId, Collection<AuditTrailValue> auditTrailValues) {
        createAuditTrail(AuditAction.UPDATE, tableName, entityId, auditTrailValues);
    }

    public void auditDelete(String tableName, String entityId, AuditTrailValue... auditTrailValues) {
        createAuditTrail(AuditAction.DELETE, tableName, entityId, Arrays.asList(auditTrailValues));
    }

    public void auditDelete(String tableName, String entityId, Collection<AuditTrailValue> auditTrailValues) {
        createAuditTrail(AuditAction.DELETE, tableName, entityId, auditTrailValues);
    }

    public void doAudit(AuditContext context) {
        createAuditTrail(AuditAction.valueOf(context.getAction().toUpperCase()), context.getTableName(), context.getEntityId(), context.getAuditTrailValues());
    }

    private void createAuditTrail(AuditAction action, String tableName, String entityId, Collection<AuditTrailValue> collection) {
        if (collection.isEmpty())
            return;
        if (Strings.isEmpty(tableName))
            throw new AuditTrailException("Table name can not be null or empty");
        if (StringUtil.isEmpty(entityId))
            throw new AuditTrailException("Entity id can not be null or zero");
        AuditTrailAction trailAction = repository.getAuditTrailAction(action.toString());
        if (trailAction == null)
            throw new AuditTrailException("Audit trail action " + action + " not found");
        LOG.debug("do audit table:{}", tableName);
        LOG.debug("action:{}", action);
        LOG.debug("entity id:{}", entityId);
        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setAction(trailAction.getAction());
        auditTrail.setTableName(tableName);
        auditTrail.setEntityId(entityId);
        auditTrail.setUserId((long) SecurityHelper.getUserId());
        repository.saveAuditTrail(auditTrail);
        if (auditTrail.getId() == null || auditTrail.getId() <= 0)
            throw new AuditTrailException("Error create audit trail");
        for (AuditTrailValue value : collection) {
            value.setAuditTrailId(auditTrail.getId());
            repository.saveAuditTrailValue(value);
            LOG.debug(value.toString());
        }
    }
}