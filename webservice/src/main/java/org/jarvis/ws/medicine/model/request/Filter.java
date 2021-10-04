package org.jarvis.ws.medicine.model.request;

import org.jarvis.core.model.base.AnyJson;
import org.jarvis.core.model.base.SerializeCloneable;
import org.jarvis.ws.medicine.model.UserContext;

/**
 * Created: KimChheng
 * Date: 13-Feb-2021 Sat
 * Time: 9:05 AM
 */
public class Filter implements AnyJson, SerializeCloneable, UserContext {

    private static final long serialVersionUID = 7150318708150041332L;

    private String field;
    private Object value;
    private String operator;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
