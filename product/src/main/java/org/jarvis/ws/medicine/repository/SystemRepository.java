package org.jarvis.ws.medicine.repository;

import org.apache.ibatis.annotations.Param;
import org.jarvis.orm.mybatis.annotation.IBatisRepository;

/**
 * Created: KimChheng
 * Date: 21-Mar-2021 Sun
 * Time: 9:48 AM
 */
@IBatisRepository
public interface SystemRepository {

    String getConfig(@Param("key") String key);
}
