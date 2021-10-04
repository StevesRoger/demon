package org.jarvis.ws.medicine.repository;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;
import org.jarvis.core.model.audit.context.AuditContext;
import org.jarvis.orm.mybatis.annotation.IBatisRepository;
import org.jarvis.ws.medicine.model.entity.MedicineAlertEntity;
import org.jarvis.ws.medicine.model.entity.MedicineEntity;
import org.jarvis.ws.medicine.model.entity.SharedEntity;
import org.jarvis.ws.medicine.model.entity.SupplyingEntity;
import org.jarvis.ws.medicine.model.entity.enums.Status;
import org.jarvis.ws.medicine.model.request.MedicineCreate;
import org.jarvis.ws.medicine.model.request.MedicineUpdate;
import org.jarvis.ws.medicine.model.request.Search;
import org.jarvis.ws.medicine.repository.provider.MedicineSQLBuilder;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created: kim chheng
 * Date: 27-Sep-2019 Fri
 * Time: 9:55 PM
 */
@IBatisRepository
public interface MedicineRepository {

    int addMedicine(MedicineCreate request);

    @UpdateProvider(type = MedicineSQLBuilder.class, method = "medicineUpdate")
    int updateMedicine(MedicineEntity entity, MedicineUpdate medicine, AuditContext context);

    MedicineEntity getMedicineByIdAndStatus(@Param("id") int id, @Param("status") Status status, @Param("userId") int userId);

    List<MedicineEntity> listMedicine(Map<String, Object> map);

    List<MedicineEntity> filterMedicine(Map<String, Object> map);

    Set<MedicineAlertEntity> listMedicineAlert(int userId);

    int countMedicine(int userId);

    List<SupplyingEntity> listSupplying(Map<String, Object> params);

    int countSupplying(int medicineId);

    List<Map<String, Object>> listMedicineIdByUser(Map<String, Object> params);

    int sharedMedicine(SharedEntity params);

    List<SharedEntity> getSharedMedicine(@Param("ownerUserId") int ownerId, @Param("guestUserId") int guestUserId);

    List<MedicineEntity> searchMedicine(Search search);
}
