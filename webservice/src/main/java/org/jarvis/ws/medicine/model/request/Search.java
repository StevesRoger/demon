package org.jarvis.ws.medicine.model.request;

import org.jarvis.core.model.base.AnyObject;
import org.jarvis.ws.medicine.model.UserContext;

import java.util.List;

/**
 * Created: KimChheng
 * Date: 12-Apr-2021 Mon
 * Time: 10:23 PM
 */
public class Search extends AnyObject implements UserContext {

    private static final long serialVersionUID = -7935028868511230854L;
    private String text;
    private String status;
    private List<Integer> ids;

    public String getText() {
        return text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}
