package org.jarvis.ws.medicine.controller;

import org.jarvis.core.model.http.request.RequestBodyWrapper;
import org.jarvis.core.model.http.response.JResponseEntity;
import org.jarvis.core.util.DateUtil;
import org.jarvis.ws.medicine.model.request.*;
import org.jarvis.ws.medicine.service.MedicineService;
import org.jarvis.ws.medicine.service.OrderService;
import org.jarvis.ws.medicine.service.ReportService;
import org.jarvis.ws.medicine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created: kim chheng
 * Date: 29-Sep-2019 Sun
 * Time: 1:33 PM
 */
@RestController
@RequestMapping(value = "/medicine", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MedicineController {

    @Autowired
    private MedicineService service;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserService userService;

    //@PreAuthorize("#oauth2.hasScope('write')")
    @PostMapping("/create")
    public JResponseEntity addMedicine(@RequestBody MedicineCreate medicine) {
        return service.addMedicine(medicine);
    }

    //@PreAuthorize("#oauth2.hasScope('write')")
    @PutMapping("/update")
    public JResponseEntity updateMedicine(@RequestBody MedicineUpdate medicine) {
        return service.updateMedicine(medicine);
    }

    @PostMapping("/supplying")
    public JResponseEntity addStock(@RequestBody Supplying supplying) {
        return service.addStock(supplying);
    }

    @GetMapping("/{id}")
    public JResponseEntity getMedicine(@PathVariable int id) {
        return service.getMedicineById(id);
    }

    @GetMapping("/list")
    public JResponseEntity listMedicine(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit) {
        return service.listMedicine(page, limit);
    }

    @PostMapping("/filter")
    public JResponseEntity filterMedicine(@RequestBody RequestBodyWrapper<List<Filter>> request) {
        return service.filterMedicine(request);
    }

    @PostMapping("/search")
    public JResponseEntity searchMedicine(@RequestBody Search search) {
        return service.searchMedicine(search);
    }

    @GetMapping("/supplying")
    public JResponseEntity listSupplying(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int limit,
                                         @RequestParam(value = "medicine_id") int medicineId,
                                         @RequestParam(value = "all", required = false) boolean all,
                                         @RequestParam(required = false, value = "start_date")
                                         @DateTimeFormat(pattern = DateUtil.DATE_NO_TIME) Date startDate,
                                         @RequestParam(required = false, value = "end_date")
                                         @DateTimeFormat(pattern = DateUtil.DATE_NO_TIME) Date endDate) {
        return service.listSupplying(page, limit, startDate, endDate, medicineId, all);
    }

    @PostMapping("/order")
    public JResponseEntity createOrder(@RequestBody Order order) {
        return orderService.creteOrder(order);
    }

    @GetMapping("/order")
    public JResponseEntity listOrder(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int limit,
                                     @RequestParam(value = "all", required = false) boolean all,
                                     @RequestParam(required = false, value = "start_date")
                                     @DateTimeFormat(pattern = DateUtil.DATE_NO_TIME) Date startDate,
                                     @RequestParam(required = false, value = "end_date")
                                     @DateTimeFormat(pattern = DateUtil.DATE_NO_TIME) Date endDate) {
        return orderService.listOrder(page, limit, startDate, endDate);
    }

    @GetMapping("/report")
    public JResponseEntity listReport(@RequestParam(required = false, value = "start_date")
                                      @DateTimeFormat(pattern = DateUtil.DATE_NO_TIME) Date startDate,
                                      @RequestParam(required = false, value = "end_date")
                                      @DateTimeFormat(pattern = DateUtil.DATE_NO_TIME) Date endDate) {
        return reportService.listReport(startDate, endDate);
    }


    @PutMapping("/set-alert")
    public JResponseEntity updateRemainingAlert(@RequestParam("medicine_id") int medicineId, @RequestParam int remain) {
        return service.updateRemainingAlert(medicineId, remain);
    }

    @DeleteMapping("/delete/{id}")
    public JResponseEntity deleteMedicine(@PathVariable int id) {
        return service.inactiveMedicine(id);
    }

    @PutMapping("/recycle/{id}")
    public JResponseEntity recycleMedicine(@PathVariable int id) {
        return service.recycleMedicine(id);
    }

    @GetMapping("/list-recyclable")
    public JResponseEntity listRecyclableMedicine() {
        return service.listRecyclableMedicine();
    }

    @GetMapping("/list-alert")
    public JResponseEntity listMedicineAlert() {
        return service.listMedicineAlert();
    }

    @PostMapping("/shared")
    public JResponseEntity sharedMedicine(@RequestParam("guest_id") int guestUserId, @RequestParam("medicine_ids") List<Integer> list) {
        return userService.sharedMedicine(guestUserId, list);
    }

    @GetMapping("/shared/{id}")
    public JResponseEntity listSharedMedicine(@PathVariable("id") int ownerUserId) {
        return userService.listSharedMedicine(ownerUserId);
    }

      /*@PostMapping(value = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public JResponseEntity uploadImage(@RequestPart("id") String medicineId, @RequestPart("files") MultipartFile[] files) throws IOException {
        return service.uploadImage(Integer.parseInt(medicineId), files);
    }*/
}
