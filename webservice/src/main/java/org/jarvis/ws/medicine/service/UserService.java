package org.jarvis.ws.medicine.service;

import org.apache.commons.codec.binary.Base64;
import org.jarvis.core.I18N;
import org.jarvis.core.collection.LazyMap;
import org.jarvis.core.exception.FatalException;
import org.jarvis.core.model.http.response.JResponseEntity;
import org.jarvis.core.util.HttpUtil;
import org.jarvis.core.util.JsonUtil;
import org.jarvis.core.util.LogSuffix;
import org.jarvis.ws.medicine.helper.SecurityHelper;
import org.jarvis.ws.medicine.model.entity.SharedEntity;
import org.jarvis.ws.medicine.model.entity.UserEntity;
import org.jarvis.ws.medicine.model.entity.enums.Scope;
import org.jarvis.ws.medicine.repository.MedicineRepository;
import org.jarvis.ws.medicine.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created: KimChheng
 * Date: 16-Jun-2020 Tue
 * Time: 21:07
 */
@Service
public class UserService {

    private static final LogSuffix LOG = LogSuffix.of(LoggerFactory.getLogger(UserService.class));

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MedicineRepository medicineRepo;

    @Value("${gateway.auth.service.url}")
    private String authServiceUrl;

    @Value("${auth.sys.user}")
    private String sysUsername;

    @Value("${auth.sys.pwd}")
    private String sysPassword;

    private HttpHeaders headers;

    @PostConstruct
    public void init() throws UnsupportedEncodingException {
        String credential = Base64.encodeBase64String((sysUsername + ":" + sysPassword).getBytes(StandardCharsets.UTF_8));
        headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Basic " + credential);
    }

    @SuppressWarnings("unchecked")
    public UserEntity getUserById(Integer userId) {
        Map map = HttpUtil.get(authServiceUrl + "/system/user?id=" + userId, Map.class, headers);
        return JsonUtil.mapTo((Map<String, Object>) map.get("data"), UserEntity.class);
    }

    @SuppressWarnings("unchecked")
    public List<UserEntity> listUser() {
        Map map = HttpUtil.get(authServiceUrl + "/system/user", Map.class, headers);
        List<Map<String, Object>> data = (List<Map<String, Object>>) map.get("data");
        return data.stream().map((user) -> JsonUtil.mapTo(user, UserEntity.class)).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void addUserMedicine(int medicineId) {
        LOG.info("map medicine with user");
        LOG.info("user id:{}", SecurityHelper.getUserId());
        LOG.info("username:{}", SecurityHelper.getUsername());
        userRepository.addUserMedicine(medicineId, SecurityHelper.getUserId());
    }

    public JResponseEntity sharedMedicine(int guestUserId, List<Integer> medicineIds) {
        LOG.info("shared medicine");
        LOG.info("owner user id:{}", SecurityHelper.getUserId());
        LOG.info("guest user id:{}", guestUserId);
        medicineIds.forEach(id -> LOG.info("medicine id:{}", id));
        if (guestUserId == SecurityHelper.getUserId())
            throw new FatalException(I18N.getMessage("fail"), "Owner id and guest id cannot be the same");
        HttpUtil.get(authServiceUrl + "/system/user/" + guestUserId, Map.class, headers);
        List<Map<String, Object>> list = medicineRepo.listMedicineIdByUser(new LazyMap("userId", SecurityHelper.getUserId()).append("medicineId", medicineIds));
        List<SharedEntity> params = new ArrayList<>();
        for (Map<String, Object> map : list) {
            SharedEntity entity = new SharedEntity();
            entity.setOwnerUserId(SecurityHelper.getUserId());
            entity.setGuestUserId(guestUserId);
            entity.setMedicineId((Integer) map.get("product_id"));
            entity.setScope(Scope.READ);
            params.add(entity);
        }
        for (SharedEntity entity : params) {
            try {
                LOG.debug(entity.toString());
                medicineRepo.sharedMedicine(entity);
            } catch (Exception e) {
                LOG.error(e.getMessage());
            }
        }
        return JResponseEntity.ok(I18N.getMessage("successful"), 0);
    }

    public JResponseEntity listSharedMedicine(int ownerUserId) {
        LOG.info("list shared medicine");
        LOG.info("owner user id:{}", ownerUserId);
        LOG.info("guest user id:{}", SecurityHelper.getUserId());
        if (ownerUserId == SecurityHelper.getUserId())
            throw new FatalException(I18N.getMessage("fail"), "Owner and guest user id cannot be the same");
        List<SharedEntity> entity = medicineRepo.getSharedMedicine(ownerUserId, SecurityHelper.getUserId());
        if (entity == null)
            throw new FatalException(I18N.getMessage("fail"), "There is no shared medicine with owner user id " + ownerUserId +
                    "and guest user id " + SecurityHelper.getUserId());
        return JResponseEntity.ok(I18N.getMessage("successful"), entity);
    }
}
