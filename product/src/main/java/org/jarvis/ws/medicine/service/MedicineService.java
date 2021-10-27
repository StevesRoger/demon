package org.jarvis.ws.medicine.service;

import org.jarvis.core.I18N;
import org.jarvis.core.collection.LazyMap;
import org.jarvis.core.exception.JdbcDataAccessException;
import org.jarvis.core.model.audit.AuditTrailValue;
import org.jarvis.core.model.audit.context.AuditUpdate;
import org.jarvis.core.model.http.request.RequestBodyWrapper;
import org.jarvis.core.model.http.response.JResponseEntity;
import org.jarvis.core.util.LogSuffix;
import org.jarvis.core.util.StringUtil;
import org.jarvis.security.util.SecurityUtil;
import org.jarvis.ws.medicine.helper.SecurityHelper;
import org.jarvis.ws.medicine.helper.ValidationHelper;
import org.jarvis.ws.medicine.model.entity.MedicineAlertEntity;
import org.jarvis.ws.medicine.model.entity.MedicineEntity;
import org.jarvis.ws.medicine.model.entity.SupplyingEntity;
import org.jarvis.ws.medicine.model.entity.enums.Status;
import org.jarvis.ws.medicine.model.request.*;
import org.jarvis.ws.medicine.repository.AuditTrailRepository.Table;
import org.jarvis.ws.medicine.repository.MedicineRepository;
import org.jarvis.ws.medicine.repository.StockRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Created: kim chheng
 * Date: 28-Sep-2019 Sat
 * Time: 9:10 AM
 */
@Service
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepo;

    @Autowired
    private StockRepository stockRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private AuditTrailService auditService;

    private final LogSuffix log = LogSuffix.of(LoggerFactory.getLogger(MedicineService.class));

    @Transactional(rollbackFor = Exception.class)
    public JResponseEntity addMedicine(MedicineCreate medicine) {
        log.info("add new medicine");
        log.info("name:{}", medicine.getName());
        log.info("name_kh:{}", medicine.getNameKh());
        log.info("brand:{}", medicine.getBrandName());
        log.info("composition:{}", medicine.getComposition());
        log.info("country:{}", medicine.getCountry());
        log.info("qty:{}", medicine.getQuantity());
        log.info("unit price import:{}", medicine.getUnitPrice());
        log.info("unit sell price:{}", medicine.getUnitSellPrice());
        log.info("expiry:{}", medicine.getExpiryDate());
        log.info("publish:{}", medicine.getPublishDate());
        int result = medicineRepo.addMedicine(medicine);
        if (result > 0) {
            auditService.auditInsert(Table.PRODUCT, medicine.getId().toString(), AuditTrailValue.insert(StringUtil.oneLineTrim(medicine.toJsonString())));
            log.info("create stock");
            MedicineStock medicineStock = MedicineStock.create(medicine);
            stockRepo.createStock(medicineStock);
            auditService.auditInsert(Table.STOCK, medicineStock.getId().toString(), AuditTrailValue.insert(StringUtil.oneLineTrim(medicineStock.toJsonString())));
            log.info("create supplying");
            Supplying supplying = new Supplying(medicine);
            stockRepo.createSupplying(supplying);
            auditService.auditInsert(Table.SUPPLYING, medicineStock.getId().toString(), AuditTrailValue.insert(StringUtil.oneLineTrim(supplying.toJsonString())));
            log.info("successful");
            userService.addUserMedicine(medicine.getId());
            return JResponseEntity.ok(I18N.getMessage("medicine.add.successful"))
                    .jsonObject("medicine_id", medicine.getId())
                    .put("stock_id", medicineStock.getId())
                    .put("supplying_id", supplying.getId()).build();
        }
        return JResponseEntity.fail(I18N.getMessage("medicine.add.fail"));
    }

    @Transactional(rollbackFor = Exception.class)
    public JResponseEntity updateMedicine(MedicineUpdate medicine) {
        log.info("update medicine");
        log.info("medicine id:{}", medicine.getId());
        MedicineEntity entity = ValidationHelper.isMedicineExist(medicine.getId());
        AuditUpdate auditContext = new AuditUpdate(Table.PRODUCT, entity.getId().toString());
        int result = medicineRepo.updateMedicine(entity, medicine, auditContext);
        if (result > 0)
            auditService.doAudit(auditContext);
        log.info("update medicine successful");
        return JResponseEntity.ok(I18N.getMessage("update.medicine.success"));
    }

    @Transactional(rollbackFor = Exception.class)
    public JResponseEntity inactiveMedicine(int medicineId) {
        log.info("delete medicine");
        log.info("medicine id:{}", medicineId);
        MedicineUpdate update = new MedicineUpdate(medicineId, Status.RECYCLABLE);
        return updateMedicine(update).message(I18N.getMessage("delete.medicine.success"));
    }

    @Transactional(rollbackFor = Exception.class)
    public JResponseEntity recycleMedicine(int medicineId) {
        log.info("recycle medicine");
        log.info("medicine id:{}", medicineId);
        MedicineEntity entity = ValidationHelper.isMedicineExist(medicineId, Status.RECYCLABLE);
        MedicineUpdate medicine = new MedicineUpdate(medicineId, Status.ACTIVE);
        AuditUpdate auditContext = new AuditUpdate(Table.PRODUCT, entity.getId().toString());
        int result = medicineRepo.updateMedicine(entity, medicine, auditContext);
        if (result > 0)
            auditService.doAudit(auditContext);
        return JResponseEntity.ok(I18N.getMessage("recycle.medicine.success"));
    }

    @Transactional(rollbackFor = Exception.class)
    public JResponseEntity addStock(Supplying supplying) {
        log.info("add new supplying");
        log.info("medicine id:{}", supplying.getMedicineId());
        MedicineEntity entity = ValidationHelper.isMedicineExist(supplying.getMedicineId());
        int result = stockRepo.createSupplying(supplying);
        if (result > 0) {
            auditService.auditInsert(Table.SUPPLYING, supplying.getId().toString(), AuditTrailValue.insert(StringUtil.oneLineTrim(supplying.toJsonString())));
            AuditUpdate auditContext = new AuditUpdate(Table.STOCK, entity.getStock().getId().toString());
            stockRepo.updateStock(entity.getStock(), MedicineStock.supplying(supplying, entity.getStock()), auditContext);
            auditService.doAudit(auditContext);
            auditContext = new AuditUpdate(Table.PRODUCT, entity.getId().toString());
            medicineRepo.updateMedicine(entity, new MedicineUpdate(supplying), auditContext);
            auditService.doAudit(auditContext);
            log.info("successful");
            return JResponseEntity.ok(I18N.getMessage("medicine.stock.add.successful")).jsonObject("supplying_id", supplying.getId()).build();
        }
        return JResponseEntity.fail(I18N.getMessage("medicine.stock.add.fail"), "Error create supplying medicine for stock");
    }

    @Transactional(rollbackFor = Exception.class)
    public JResponseEntity updateRemainingAlert(int medicineId, int remain) {
        log.info("update remaining alert");
        log.info("medicine id:{}", medicineId);
        log.info("reaming:{}", remain);
        MedicineEntity entity = ValidationHelper.isMedicineExist(medicineId);
        AuditUpdate auditContext = new AuditUpdate(Table.STOCK, entity.getStock().getId().toString());
        int result = stockRepo.updateStock(entity.getStock(), MedicineStock.updateRemainingAlert(medicineId, remain), auditContext);
        if (result < 0)
            throw new JdbcDataAccessException(I18N.getMessage("medicine.set.remain.fail"), "Error set stock remaining alert");
        auditService.doAudit(auditContext);
        log.info("successful");
        return JResponseEntity.ok(I18N.getMessage("successful"));
    }

    public JResponseEntity getMedicineById(int id) {
        log.debug("authentication user id:{}", SecurityHelper.getUserId());
        MedicineEntity medicineRequest = ValidationHelper.isMedicineExist(id);
        log.info("get medicine by id successful");
        return JResponseEntity.ok(I18N.getMessage("successful"), medicineRequest);
    }

    public JResponseEntity listMedicine(int page, int limit) {
        log.info("list medicine");
        Map<String, Object> params = new LazyMap("limit", limit);
        int offset = (page - 1) * limit;
        int total = medicineRepo.countMedicine(SecurityHelper.getUserId());
        int totalPage = (int) Math.ceil((float) total / limit);
        boolean hasNext = page < totalPage;
        params.put("offset", offset);
        params.put("userId", SecurityHelper.getUserId());
        List<MedicineEntity> list = medicineRepo.listMedicine(params);
        return JResponseEntity.ok(I18N.getMessage("successful")).jsonObject("medicines", list)
                .put("current_page", page).put("limit", limit).put("has_next", hasNext)
                .put("total", total).put("total_page", totalPage).build();
    }

    public JResponseEntity listSupplying(int page, int limit, Date startDate, Date endDate, int medicineId, boolean all) {
        log.info("list supplying stock");
        Map<String, Object> params = new LazyMap("medicineId", medicineId);
        if (all) {
            List<SupplyingEntity> list = medicineRepo.listSupplying(params);
            return JResponseEntity.ok(I18N.getMessage("successful")).jsonObject("supplying", list).build();
        }
        int offset = (page - 1) * limit;
        int total = medicineRepo.countSupplying(medicineId);
        int totalPage = (int) Math.ceil((float) total / limit);
        boolean hasNext = page < totalPage;
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("offset", offset);
        params.put("limit", limit);
        List<SupplyingEntity> list = medicineRepo.listSupplying(params);
        return JResponseEntity.ok(I18N.getMessage("successful")).jsonObject("supplying", list)
                .put("current_page", page).put("limit", limit).put("has_next", hasNext)
                .put("total", total).put("total_page", totalPage).build();
    }

    public JResponseEntity listRecyclableMedicine() {
        log.info("list recyclable medicine");
        LazyMap params = new LazyMap("status", Status.RECYCLABLE);
        params.put("userId", SecurityHelper.getUserId());
        params.put("by", "deleted_date");
        params.put("order", "desc");
        List<MedicineEntity> list = medicineRepo.listMedicine(params);
        List<MedicineEntity> response = new ArrayList<>();
        List<MedicineEntity> inactive = new ArrayList<>();
        for (MedicineEntity entity : list) {
            if (entity.isRecyclable())
                response.add(entity);
            else
                inactive.add(entity);
        }
        inactiveMedicine(inactive);
        return JResponseEntity.ok(I18N.getMessage("successful")).jsonObject("medicines", response).build();
    }

    public JResponseEntity filterMedicine(RequestBodyWrapper<List<Filter>> request) {
        log.info("filter medicine");
        Map<String, Object> params = new LazyMap();
        request.getRequest().forEach(filter -> {
            if ("order".equalsIgnoreCase(filter.getOperator())) {
                params.put("by", filter.getField());
                params.put("order", filter.getValue());
            } else
                params.put(filter.getField(), filter.getValue());
        });
        params.put("userId", SecurityHelper.getUserId());
        List<MedicineEntity> list = medicineRepo.filterMedicine(params);
        return JResponseEntity.ok(I18N.getMessage("successful")).jsonObject("medicines", list).build();
    }

    public JResponseEntity searchMedicine(Search search) {
        log.info("search medicine");
        List<MedicineEntity> list = medicineRepo.searchMedicine(search);
        String message = I18N.getMessage("successful");
        if (list.isEmpty())
            message = I18N.getMessage("medicine.search.not.found", search.getText());
        return JResponseEntity.ok(message).jsonObject("medicines", list).build();
    }

    public JResponseEntity listMedicineAlert() {
        log.info("list medicine alert by user id:{}", SecurityHelper.getUserId());
        List<MedicineAlertEntity> list = new ArrayList<>(medicineRepo.listMedicineAlert(SecurityHelper.getUserId()));
        return JResponseEntity.ok(I18N.getMessage("successful")).jsonObject("medicines", list).build();
    }

    private void inactiveMedicine(List<MedicineEntity> list) {
        log.info("delete recyclable medicine");
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityUtil.getAuthentication();
        CompletableFuture.runAsync(() -> {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            list.forEach(entity -> {
                try {
                    log.info("id:{}", entity.getId());
                    log.info("name:{}", entity.getName());
                    MedicineUpdate medicine = new MedicineUpdate(entity.getId(), Status.INACTIVE);
                    AuditUpdate auditContext = new AuditUpdate(Table.PRODUCT, entity.getId().toString());
                    int result = medicineRepo.updateMedicine(entity, medicine, auditContext);
                    if (result > 0)
                        auditService.doAudit(auditContext);
                } catch (Exception e) {
                    log.warn(e.getMessage(), e);
                }
            });
        });
    }

      /*  @Transactional(rollbackFor = Exception.class)
    public JResponseEntity uploadImage(int medicineId, MultipartFile[] files) throws IOException {
        log.info("delete medicine");
        ValidationHelper.isMedicineExist(medicineId);
        List<String> fail = new ArrayList<>();
        for (MultipartFile file : files) {
            ImageEntity image = new ImageEntity(file.getOriginalFilename(), file.getContentType(), file.getBytes());
            image.setCreatedBy(SecurityHelper.getUsername());
            int result = medicineRepo.saveImage(image);
            if (result != 1)
                fail.add(file.getOriginalFilename());
        }
        if (!fail.isEmpty()) {
            log.info("There are errors when upload image file");
            fail.forEach(msg -> log.info("file name:{}", msg));
            return ResponseBuilder.fail(I18N.getMessage("medicine.upload.image.fail"), "There is an error when upload image");
        }
        log.info("upload medicine image successful");
        return ResponseBuilder.success(I18N.getMessage("successful"));
    }*/
}
