package org.jarvis.ws.medicine.repository;

import org.apache.ibatis.annotations.Param;
import org.jarvis.orm.mybatis.annotation.IBatisRepository;

/**
 * Created: KimChheng
 * Date: 16-Jun-2020 Tue
 * Time: 21:25
 */
@IBatisRepository
public interface UserRepository {

    int addUserMedicine(@Param("medicineId") int medicineId, @Param("userId") int userId);
}
