package com.jarvis.app.auth.model.response;

import com.jarvis.app.auth.model.entity.ref.AuthType;
import com.jarvis.app.auth.model.request.RegisterDevice;
import com.jarvis.frmk.core.model.base.SerializeCloneable;

/**
 * Created: KimChheng
 * Date: 21-Mar-2021 Sun
 * Time: 3:45 PM
 */
public interface ThirdPartyUserAccount extends SerializeCloneable {

    String getFirstName();

    String getLastName();

    String getEmail();

    AuthType getAuthType();

    String getPassword();

    RegisterDevice getDevice();
}
