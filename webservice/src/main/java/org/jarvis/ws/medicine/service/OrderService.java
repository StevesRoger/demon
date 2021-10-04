package org.jarvis.ws.medicine.service;

import org.jarvis.core.I18N;
import org.jarvis.core.collection.LazyMap;
import org.jarvis.core.exception.JdbcDataAccessException;
import org.jarvis.core.model.audit.AuditTrailValue;
import org.jarvis.core.model.audit.context.AuditContext;
import org.jarvis.core.model.audit.context.AuditInsert;
import org.jarvis.core.model.audit.context.AuditUpdate;
import org.jarvis.core.model.http.response.JResponseEntity;
import org.jarvis.core.util.LogSuffix;
import org.jarvis.core.util.StringUtil;
import org.jarvis.ws.medicine.helper.SecurityHelper;
import org.jarvis.ws.medicine.model.entity.OrderEntity;
import org.jarvis.ws.medicine.model.entity.StockEntity;
import org.jarvis.ws.medicine.model.request.MedicineOrder;
import org.jarvis.ws.medicine.model.request.Order;
import org.jarvis.ws.medicine.model.request.MedicineStock;
import org.jarvis.ws.medicine.repository.AuditTrailRepository.Table;
import org.jarvis.ws.medicine.repository.OrderRepository;
import org.jarvis.ws.medicine.repository.StockRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created: KimChheng
 * Date: 09-Nov-2020 Mon
 * Time: 11:17 AM
 */

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private StockRepository stockRepo;

    @Autowired
    private AuditTrailService auditService;

    private static final LogSuffix LOG = LogSuffix.of(LoggerFactory.getLogger(OrderService.class));

    @Transactional(rollbackFor = Exception.class)
    public JResponseEntity creteOrder(Order order) {
        LOG.info("create order");
        orderRepo.createOrder(order);
        auditService.auditInsert(Table.ORDER, order.getId().toString(), AuditTrailValue.insert(StringUtil.oneLineTrim(order.toJsonString())));
        LOG.info("order item:{}", order.getOrderItems().size());
        orderRepo.saveOrderItem(order);
        LOG.info("stock movement");
        List<AuditContext> audits = new ArrayList<>();
        for (MedicineOrder item : order.getOrderItems()) {
            item.setOrderId(order.getId());
            StockEntity stockEntity = stockRepo.getStockByMedicineId(item.getMedicineId());
            LOG.info("stock id:{}", stockEntity.getId());
            MedicineStock medicineStock = stockEntity.movement(item);
            LOG.info("post quantity:{}", stockEntity.getPreQuantity());
            LOG.info("pre quantity:{}", stockEntity.getPreQuantity());
            AuditUpdate auditUpdate = new AuditUpdate(Table.STOCK, stockEntity.getId().toString());
            stockRepo.updateStock(stockEntity, medicineStock, auditUpdate);
            audits.add(auditUpdate);
            AuditContext auditInsert = new AuditInsert(Table.PRODUCT_ORDER, item.getMedicineId() + "," + item.getOrderId(), AuditTrailValue.insert(StringUtil.oneLineTrim(item.toJsonString())));
            audits.add(auditInsert);
        }
        audits.forEach(item -> auditService.doAudit(item));
        LOG.info("successful");
        return JResponseEntity.ok(I18N.getMessage("order.successful")).jsonObject("order_id", order.getId()).build();
    }

    public JResponseEntity listOrder(int page, int limit, Date startDate, Date endDate) {
        LOG.info("list order");
        Map<String, Object> params = new LazyMap("userId", SecurityHelper.getUserId());
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        int offset = (page - 1) * limit;
        int total = orderRepo.countOrder(params);
        int totalPage = (int) Math.ceil((float) total / limit);
        boolean hasNext = page < totalPage;
        params.put("offset", offset);
        params.put("limit", limit);
        List<OrderEntity> list = orderRepo.listOrder(params);
        return JResponseEntity.ok(I18N.getMessage("order.successful")).jsonObject("orders", list)
                .put("current_page", page).put("limit", limit).put("has_next", hasNext)
                .put("total", total).put("total_page", totalPage).build();
    }

}
