package org.jarvis.ws.medicine.repository;

import org.apache.ibatis.annotations.Param;
import org.jarvis.orm.mybatis.annotation.IBatisRepository;
import org.jarvis.ws.medicine.model.entity.ImageEntity;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * Created: KimChheng
 * Date: 28-Nov-2020 Sat
 * Time: 10:21 AM
 */
@IBatisRepository
public interface ImageRepository {

    int addImage(ImageEntity image);

    ImageEntity getById(@Param("id") int id);

}
