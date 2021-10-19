package com.jarvis.app.auth.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jarvis.app.auth.model.entity.UserDevice;
import com.jarvis.frmk.core.model.base.SerializeCloneable;
import com.jarvis.frmk.core.util.ObjectUtil;
import com.jarvis.frmk.hibernate.entity.ref.AuthType;
import com.jarvis.frmk.security.oauth2.IOAuth2;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static com.jarvis.frmk.security.oauth2.IOAuth2.RESOURCE_OWNER;

/**
 * Created: KimChheng
 * Date: 13-Oct-2019 Sun
 * Time: 10:29 PM
 */
@ApiModel
public class Login implements SerializeCloneable {

    private static final long serialVersionUID = -38237840687081676L;

    @NotEmpty
    @ApiModelProperty(example = "tony")
    private String username;
    @NotEmpty
    private String password;
    @ApiModelProperty(example = "ACCOUNT", dataType = "java.lang.String")
    private AuthType type = AuthType.ACCOUNT;
    @ApiModelProperty(example = "{\"id\":\"string\",\"platform\":\"ios\",\"token\":\"eh42st0WQvCSWO\",\"model\":\"iphone13\",\"app_version\":\"1.1.3\"}")
    private UserDevice device;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AuthType getType() {
        return type;
    }

    public void setType(AuthType type) {
        this.type = type;
    }

    public UserDevice getDevice() {
        return device;
    }

    public void setDevice(UserDevice device) {
        this.device = device;
    }

    @JsonIgnore
    public Map<String, String> passwordGrant(String clientId, String secret) {
        Map<String, String> map = new HashMap<>();
        map.put(IOAuth2.CLIENT_ID, clientId);
        map.put(IOAuth2.CLIENT_SECRET, secret);
        map.put(IOAuth2.PASSWORD, password);
        map.put(IOAuth2.USERNAME, username);
        map.put(IOAuth2.GRANT_TYPE, RESOURCE_OWNER);
        map.put("device", Base64.getEncoder().encodeToString(ObjectUtil.toByteArray(device)));
        map.put("type", type.toString());
        return map;
    }
}
