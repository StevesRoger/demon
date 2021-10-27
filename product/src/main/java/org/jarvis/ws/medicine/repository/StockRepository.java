package org.jarvis.ws.medicine.repository;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;
import org.jarvis.core.model.audit.context.AuditUpdate;
import org.jarvis.orm.mybatis.annotation.IBatisRepository;
import org.jarvis.ws.medicine.model.entity.StockEntity;
import org.jarvis.ws.medicine.model.request.MedicineStock;
import org.jarvis.ws.medicine.model.request.Supplying;
import org.jarvis.ws.medicine.repository.provider.StockSQLBuilder;

import java.util.List;
import java.util.Map;

@IBatisRepository
public interface StockRepository {

    int createSupplying(Supplying stock);

    int createStock(MedicineStock medicineStock);

    @UpdateProvider(type = StockSQLBuilder.class, method = "stockUpdate")
    int updateStock(StockEntity entity, MedicineStock stock, AuditUpdate context);

    StockEntity getStockByMedicineId(@Param("id") int medicineId);

    List<Map<String, Object>> listSupplying(Integer userId);

    /*List<StockEntity> listMedicineStockByMedicineId(@Param("medicineId") int medicineId);

    StockEntity getMedicineStockById(@Param("id") int id);*/
}
