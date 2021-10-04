package org.jarvis.ws.medicine.repository.provider;

import org.apache.ibatis.jdbc.SQL;
import org.jarvis.core.model.audit.AuditTrailValue;
import org.jarvis.core.model.audit.context.AuditUpdate;
import org.jarvis.ws.medicine.model.entity.StockEntity;
import org.jarvis.ws.medicine.model.request.MedicineStock;

/**
 * Created: KimChheng
 * Date: 02-Nov-2020 Mon
 * Time: 10:23 AM
 */
public final class StockSQLBuilder {

    public String stockUpdate(StockEntity entity, MedicineStock stock, AuditUpdate context) {
        return new SQL() {
            boolean isUpdate = false;

            {
                UPDATE("ph_stock");
                if (stock.getPostQuantity() != null) {
                    SET("post_quantity = #{stock.postQuantity}");
                    AuditTrailValue trailValue = new AuditTrailValue("post_quantity", String.valueOf(entity.getPostQuantity()), String.valueOf(stock.getPostQuantity()));
                    context.addAuditTrailValue(trailValue);
                    isUpdate = true;
                }
                if (stock.getPreQuantity() != null) {
                    SET("pre_quantity = #{stock.preQuantity}");
                    AuditTrailValue trailValue = new AuditTrailValue("pre_quantity", String.valueOf(entity.getPreQuantity()), String.valueOf(stock.getPreQuantity()));
                    context.addAuditTrailValue(trailValue);
                    isUpdate = true;
                }
                if (stock.getRemainingAlert() != null) {
                    SET("remaining_alert = #{stock.remainingAlert}");
                    AuditTrailValue trailValue = new AuditTrailValue("remaining_alert", String.valueOf(entity.getRemainingAlert()), String.valueOf(stock.getRemainingAlert()));
                    context.addAuditTrailValue(trailValue);
                    isUpdate = true;
                }
                if (isUpdate) {
                    SET("modified_by = #{stock.username}");
                    SET("modified_date = now()");
                }
                if (!isUpdate) {
                    SET("created_by = #{stock.username}");
                }
                WHERE("product_id = #{stock.medicineId}");
            }
        }.toString();
    }
}
