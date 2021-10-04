package org.jarvis.ws.medicine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jarvis.ws.medicine.helper.SecurityHelper;

/**
 * Created: KimChheng
 * Date: 31-Oct-2020 Sat
 * Time: 10:36 AM
 */
public interface UserContext {

    @JsonIgnore
    default String getUsername() {
        return SecurityHelper.getUsername();
    }

    @JsonIgnore
    default Integer getUserId() {
        return SecurityHelper.getUserId();
    }
}
