package org.jarvis.ws.medicine.helper;

import org.jarvis.core.I18N;
import org.jarvis.core.exception.FatalException;
import org.jarvis.core.util.ContextUtil;
import org.jarvis.ws.medicine.model.entity.MedicineEntity;
import org.jarvis.ws.medicine.model.entity.enums.Status;
import org.jarvis.ws.medicine.repository.MedicineRepository;

import java.net.URL;

/**
 * Created: KimChheng
 * Date: 14-Jun-2020 Sun
 * Time: 22:11
 */
public interface ValidationHelper {

    static void isValid(String url) {
        try {
            new URL(url).toURI();
        } catch (Exception e) {
            throw new FatalException(I18N.getMessage("media.invalid.link"), "Invalid link");
        }
    }

    static MedicineEntity isMedicineExist(Integer id) {
        return isMedicineExist(id, Status.ACTIVE);
    }

    static MedicineEntity isMedicineExist(Integer id, Status status) {
        if (id == null && id <= 0)
            throw new FatalException(I18N.getMessage("medicine.id.empty"));
        MedicineRepository repository = ContextUtil.getBean(MedicineRepository.class);
        MedicineEntity entity = repository.getMedicineByIdAndStatus(id, status, SecurityHelper.getUserId());
        if (entity == null)
            throw new FatalException(I18N.getMessage("medicine.id.not.found", "id", id));
        if (entity.getStock() == null)
            throw new FatalException(I18N.getMessage("stock.not.found"));
        return entity;
    }
}
