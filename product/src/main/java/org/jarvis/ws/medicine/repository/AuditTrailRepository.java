package org.jarvis.ws.medicine.repository;

import org.jarvis.core.model.audit.AuditTrail;
import org.jarvis.core.model.audit.AuditTrailAction;
import org.jarvis.core.model.audit.AuditTrailValue;
import org.jarvis.orm.mybatis.annotation.IBatisRepository;

import java.util.Set;

/**
 * Created: chheng
 * Date: 25-Sep-2020 Fri
 * Time: 10:45
 */
@IBatisRepository
public interface AuditTrailRepository {

    int saveAuditTrail(AuditTrail auditTrail);

    int saveAuditTrailValue(AuditTrailValue auditTrailValue);

    Set<AuditTrailAction> listAuditTrailAction();

    AuditTrailAction getAuditTrailAction(String action);

    interface Table {
        String PRODUCT = "ph_product";
        String STOCK = "ph_stock";
        String SUPPLYING = "ph_supplying";
        String SUPPLIER = "ph_supplier";
        String ORDER = "ph_order";
        String PRODUCT_ORDER = "ph_product_order";
    }
}
