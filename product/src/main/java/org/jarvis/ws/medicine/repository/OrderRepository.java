package org.jarvis.ws.medicine.repository;

import org.jarvis.orm.mybatis.annotation.IBatisRepository;
import org.jarvis.ws.medicine.model.entity.MedicineOrderEntity;
import org.jarvis.ws.medicine.model.entity.OrderEntity;
import org.jarvis.ws.medicine.model.request.Order;

import java.util.List;
import java.util.Map;

/**
 * Created: KimChheng
 * Date: 09-Nov-2020 Mon
 * Time: 10:58 AM
 */
@IBatisRepository
public interface OrderRepository {

    int createOrder(Order order);

    int saveOrderItem(Order order);

    int countOrder(Map<String, Object> param);

    List<OrderEntity> listOrder(Map<String, Object> param);

    List<MedicineOrderEntity> listOrderItem(int id);
}
