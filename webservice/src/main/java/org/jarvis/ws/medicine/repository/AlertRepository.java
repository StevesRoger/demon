package org.jarvis.ws.medicine.repository;

import org.jarvis.orm.mybatis.annotation.IBatisRepository;
import org.jarvis.ws.medicine.model.entity.AlertEntity;
import org.jarvis.ws.medicine.model.entity.InboxEntity;
import org.jarvis.ws.medicine.model.fcm.Token;

import java.util.List;

@IBatisRepository
public interface AlertRepository {

    void createAlertLog(AlertEntity entity);

    void createInbox(InboxEntity entity);

    List<InboxEntity> list(int userId, String type);

    void saveToken(Token token);

    int getBadge(int userId);

    Integer clearBadge(int userId);

}
