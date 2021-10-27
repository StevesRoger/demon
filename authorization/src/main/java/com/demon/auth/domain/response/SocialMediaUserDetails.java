package com.demon.auth.domain.response;

import com.jarvis.frmk.core.model.base.SerializeCloneable;
import com.jarvis.frmk.hibernate.entity.ref.AuthType;

/**
 * Created: KimChheng
 * Date: 21-Mar-2021 Sun
 * Time: 3:45 PM
 */
public interface SocialMediaUserDetails extends SerializeCloneable {

    String getFullName();

    String getFirstName();

    String getLastName();

    String getEmail();

    AuthType getType();

    String getPassword();

    UserDevice getDevice();
}
