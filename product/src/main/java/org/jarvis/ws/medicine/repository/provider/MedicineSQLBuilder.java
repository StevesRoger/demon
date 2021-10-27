package org.jarvis.ws.medicine.repository.provider;

import org.apache.ibatis.jdbc.SQL;
import org.jarvis.core.model.audit.AuditTrailValue;
import org.jarvis.core.model.audit.context.AuditContext;
import org.jarvis.core.util.DateUtil;
import org.jarvis.ws.medicine.model.entity.MedicineEntity;
import org.jarvis.ws.medicine.model.entity.enums.Status;
import org.jarvis.ws.medicine.model.request.MedicineUpdate;

public final class MedicineSQLBuilder {

    public String medicineUpdate(MedicineEntity entity, MedicineUpdate medicine, AuditContext context) {
        return new SQL() {
            boolean isUpdate = false;

            {
                UPDATE("ph_product");
                if (medicine.getBrandName() != null && !medicine.getBrandName().equals(entity.getBrandName())) {
                    SET("brand = #{medicine.brandName}");
                    context.addAuditTrailValue(new AuditTrailValue("brand", entity.getBrandName(), medicine.getBrandName()));
                    isUpdate = true;
                }
                if (medicine.getNameKh() != null && !medicine.getNameKh().equals(entity.getNameKh())) {
                    SET("name_kh = #{medicine.nameKh}");
                    context.addAuditTrailValue(new AuditTrailValue("name_kh", entity.getNameKh(), medicine.getNameKh()));
                    isUpdate = true;
                }
                if (medicine.getName() != null && !medicine.getName().equals(entity.getName())) {
                    SET("name = #{medicine.name}");
                    context.addAuditTrailValue(new AuditTrailValue("name", entity.getName(), medicine.getName()));
                    isUpdate = true;
                }
                if (medicine.getComposition() != null && !medicine.getComposition().equals(entity.getComposition())) {
                    SET("composition = #{medicine.composition}");
                    context.addAuditTrailValue(new AuditTrailValue("composition", entity.getComposition(), medicine.getComposition()));
                    isUpdate = true;
                }
                if (medicine.getCountry() != null && !medicine.getCountry().equals(entity.getCountry())) {
                    SET("country = #{medicine.country}");
                    context.addAuditTrailValue(new AuditTrailValue("country", entity.getCountry(), medicine.getCountry()));
                    isUpdate = true;
                }
                if (medicine.getDesc() != null && !medicine.getDesc().equals(entity.getDesc())) {
                    SET("\"desc\" = #{medicine.desc}");
                    context.addAuditTrailValue(new AuditTrailValue("desc", entity.getDesc(), medicine.getDesc()));
                    isUpdate = true;
                }
                if (medicine.getLabel() != null && !medicine.getLabel().equals(entity.getLabel())) {
                    SET("label = #{medicine.label}");
                    context.addAuditTrailValue(new AuditTrailValue("label", entity.getLabel(), medicine.getLabel()));
                    isUpdate = true;
                }
                if (medicine.getStatus() != null && !medicine.getStatus().equals(entity.getStatus())) {
                    SET("status = #{medicine.status}");
                    if (Status.RECYCLABLE.equals(medicine.getStatus())) {
                        SET("deleted_date = now()");
                        context.addAuditTrailValue(new AuditTrailValue("deleted_date", null, DateUtil.getNow()));
                    } else if (Status.INACTIVE.equals(medicine.getStatus())) {
                        SET("deleted_date = null");
                        context.addAuditTrailValue(new AuditTrailValue("deleted_date", DateUtil.format(entity.getDeletedDate()), null));
                    }
                    context.addAuditTrailValue(new AuditTrailValue("status", entity.getStatus().toString(), medicine.getStatus().toString()));
                    isUpdate = true;
                }
                if (medicine.getUnitType() != null && !medicine.getUnitType().equals(entity.getUnitType())) {
                    SET("unit_type = #{medicine.unitType}");
                    context.addAuditTrailValue(new AuditTrailValue("unit_type", entity.getUnitType(), medicine.getUnitType()));
                    isUpdate = true;
                }
                if (medicine.getUnitPrice() != null) {
                    SET("unit_price = #{medicine.unitPrice}");
                    context.addAuditTrailValue(new AuditTrailValue("unit_price", String.valueOf(entity.getUnitPrice()), String.valueOf(medicine.getUnitPrice())));
                    isUpdate = true;
                }
                if (isUpdate) {
                    SET("modified_by = #{medicine.username}");
                    SET("modified_date = now()");
                }
                if (!isUpdate) {
                    SET("created_by = #{entity.username}");
                }
                WHERE("id = #{medicine.id}");
            }
        }.toString();
    }
}
