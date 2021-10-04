package com.jarvis.app.auth.converter;

import com.jarvis.app.auth.model.entity.ref.OtpType;
import com.jarvis.frmk.hibernate.entity.converter.EntityRefConverter;

import javax.persistence.Converter;

/**
 * Created: KimChheng
 * Date: 28-Sep-2021 Tue
 * Time: 10:34 PM
 */
@Converter
public class OtpTypeConverter extends EntityRefConverter<OtpType, String> {
    @Override
    public OtpType convertToEntityAttribute(String value) {
        return value == null ? null : new OtpType(value, null);
    }
}
