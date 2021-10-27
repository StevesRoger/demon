package org.jarvis.ws.medicine.repository;

import org.jarvis.orm.mybatis.annotation.IBatisRepository;
import org.jarvis.ws.medicine.model.entity.ReportEntity;
import org.jarvis.ws.medicine.model.entity.ReportItemEntity;

import java.util.List;
import java.util.Map;

/**
 * Created: KimChheng
 * Date: 20-Nov-2020 Fri
 * Time: 4:40 PM
 */
@IBatisRepository
public interface ReportRepository {

    List<ReportEntity> listOrder(Map<String, Object> param);

    List<ReportItemEntity> listOrderItem(int orderId);

    List<ReportItemEntity> listSupplyingItem(Map<String, Object> params);
}
