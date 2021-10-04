package org.jarvis.ws.medicine.service;

import org.jarvis.core.I18N;
import org.jarvis.core.collection.LazyMap;
import org.jarvis.core.model.http.response.JResponseEntity;
import org.jarvis.core.util.LogSuffix;
import org.jarvis.ws.medicine.helper.SecurityHelper;
import org.jarvis.ws.medicine.model.entity.ReportEntity;
import org.jarvis.ws.medicine.model.entity.ReportItemEntity;
import org.jarvis.ws.medicine.model.entity.enums.ReportType;
import org.jarvis.ws.medicine.repository.ReportRepository;
import org.jarvis.ws.medicine.repository.StockRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Created: KimChheng
 * Date: 20-Nov-2020 Fri
 * Time: 4:25 PM
 */
@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepo;

    @Autowired
    private StockRepository stockRepository;

    private static final LogSuffix LOG = LogSuffix.of(LoggerFactory.getLogger(OrderService.class));

    private static final SimpleDateFormat SF = new SimpleDateFormat("yyyy-MM-dd");

    public JResponseEntity listReport(Date startDate, Date endDate) {
        Map<String, Object> params = new LazyMap("userId", SecurityHelper.getUserId());
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        CompletableFuture<Map<String, ReportItemEntity>> supplyingTask = CompletableFuture.supplyAsync(() -> this.listSupplyingItem(params));
        Map<String, ReportEntity> reportMap = listOrder(params);
        Map<String, ReportItemEntity> supplyingMap = supplyingTask.join();
        for (Map.Entry<String, ReportItemEntity> entry : supplyingMap.entrySet()) {
            String key = entry.getKey().split(",")[0];
            ReportItemEntity supplying = entry.getValue();
            ReportEntity report = reportMap.get(key);
            if (report == null) {
                report = new ReportEntity();
                report.setCreatedDate(supplying.getCreatedDate());
                reportMap.put(key, report);
            }
            report.addTotalSupplying(supplying.getUnitPrice() * supplying.getQuantity());
            report.getItems().add(supplying);
        }
        List<ReportEntity> sort = new ArrayList<>(reportMap.values());
        Collections.sort(sort);
        return JResponseEntity.ok(I18N.getMessage("successful")).jsonObject("reports", sort).build();
    }

    private Map<String, ReportEntity> listOrder(Map<String, Object> params) {
        LOG.info("list order report");
        Map<String, ReportEntity> reportMap = new HashMap<>();
        List<ReportEntity> reports = reportRepo.listOrder(params);
        for (ReportEntity report : reports) {
            String createDate = SF.format(report.getCreatedDate());
            ReportEntity tempReport = reportMap.putIfAbsent(createDate, report);
            if (tempReport == null)
                tempReport = report;
            if (!tempReport.equals(report) && SF.format(tempReport.getCreatedDate()).equals(createDate))
                tempReport.addTotalOrder(report.getTotalOrder());
            List<ReportItemEntity> itemOrders = reportRepo.listOrderItem(report.getId());
            for (ReportItemEntity item : itemOrders) {
                String key = item.getId() + ":" + item.getUnitPrice();
                ReportItemEntity tempItem = tempReport.putIfAbsent(key, item);
                if (tempItem == null) {
                    item.setType(ReportType.SOLD);
                    continue;
                }
                if (tempItem.equals(item))
                    tempItem.addQuantity(item.getQuantity());
            }
        }
        return reportMap;
    }

    private Map<String, ReportItemEntity> listSupplyingItem(Map<String, Object> params) {
        LOG.info("list supplying report");
        Map<String, ReportItemEntity> reportMap = new HashMap<>();
        List<ReportItemEntity> supplyingItem = reportRepo.listSupplyingItem(params);
        for (ReportItemEntity item : supplyingItem) {
            String createDate = SF.format(item.getCreatedDate());
            String expiryDate = SF.format(item.getExpiryDate());
            String publishDate = SF.format(item.getPublishDate());
            String key = createDate + "," + expiryDate + "," + publishDate;
            ReportItemEntity tempItem = reportMap.putIfAbsent(key, item);
            if (tempItem == null) {
                item.setType(ReportType.IMPORTED);
                continue;
            }
            createDate = SF.format(tempItem.getCreatedDate());
            expiryDate = SF.format(tempItem.getExpiryDate());
            publishDate = SF.format(tempItem.getPublishDate());
            String tempKey = createDate + "," + expiryDate + "," + publishDate;
            if (tempItem.equals(item) && tempKey.equals(key))
                tempItem.addQuantity(item.getQuantity());
        }
        return reportMap;
    }
}
