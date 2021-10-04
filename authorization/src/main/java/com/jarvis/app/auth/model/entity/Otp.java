package com.jarvis.app.auth.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.jarvis.app.auth.converter.OtpTypeConverter;
import com.jarvis.app.auth.model.entity.ref.OtpType;
import com.jarvis.frmk.hibernate.entity.audit.AuditIdentityEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "otp")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Otp extends AuditIdentityEntity<String> {

    private static final long serialVersionUID = 1561669477368673050L;

    private String code;
    private Integer validity;
    private Date expiryDate;
    private String destination;
    private Boolean verified;
    private OtpType type;
    private byte[] detail;

    public Otp() {
        this.id = UUID.randomUUID().toString();
        this.verified = false;
    }

    @Column(name = "code", nullable = false, updatable = false)
    public String getCode() {
        return code;
    }

    public void setCode(String otpCode) {
        this.code = otpCode;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiry_date", nullable = false)
    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryAfter) {
        this.expiryDate = expiryAfter;
    }

    @Column(name = "verified")
    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    @Column(name = "destination", nullable = false, updatable = false)
    public String getDestination() {
        return destination;
    }

    public void setDestination(String to) {
        this.destination = to;
    }

    @Column(name = "validity", nullable = false)
    public Integer getValidity() {
        return validity;
    }

    public void setValidity(Integer validity) {
        this.validity = validity;
    }

    @Convert(converter = OtpTypeConverter.class)
    @Column(name = "type")
    public OtpType getType() {
        return type;
    }

    public void setType(OtpType type) {
        this.type = type;
    }

    @Column(name = "detail")
    public byte[] getDetail() {
        return detail;
    }

    public void setDetail(byte[] detail) {
        this.detail = detail;
    }

    @Transient
    public boolean isExpired() {
        return !expiryDate.after(new Date());
    }
}
