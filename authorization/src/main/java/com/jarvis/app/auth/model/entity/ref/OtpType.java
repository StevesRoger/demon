package com.jarvis.app.auth.model.entity.ref;

import com.jarvis.frmk.hibernate.entity.ref.EntityRef;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created: KimChheng
 * Date: 27-Sep-2021 Mon
 * Time: 10:05 PM
 */
@Entity
@Table(name = "otp_type")
public class OtpType extends EntityRef<String> {

    public static final OtpType ACTIVATE = new OtpType("ACTIVATE", "activation otp type");
    public static final OtpType FORGET_PASSWORD = new OtpType("FORGET_PASSWORD", "forget password otp type");
    
    public OtpType(String id, String description) {
        super(id, description);
    }

    static {
        EntityRef.putDmlScript("sql/otp-type-dml.sql", OtpType.class);
    }
}
