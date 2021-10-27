package org.jarvis.ws.medicine.service;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.jarvis.core.model.http.response.JResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created: KimChheng
 * Date: 30-Oct-2020 Fri
 * Time: 8:17 PM
 */
@Service
public class EnumService {

    @Autowired
    private SqlSessionFactory sessionFactory;

    public JResponseEntity list(String table) {
        SqlSession session = null;
        try {
            session = sessionFactory.openSession();
            List<Object> object = session.selectList("listEnum", "ph_e_" + table);
            return JResponseEntity.ok("success").data(object);
        } catch (Exception e) {
            return JResponseEntity.fail("failed to fetch enum " + table).error(e.getMessage()).exception(e);
        } finally {
            if (session != null)
                session.close();
        }
    }
}
