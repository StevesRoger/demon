package com.jarvis.app.auth.model;

import com.jarvis.frmk.core.ICore;
import com.jarvis.frmk.core.model.base.AnyObject;

import java.util.UUID;

/**
 * Created: KimChheng
 * Date: 13-Oct-2019 Sun
 * Time: 10:29 PM
 */

public class MailMessage extends AnyObject {

    private static final long serialVersionUID = 1413910997583573833L;

    private String to;
    private String from;
    private String subject;
    private String content;

    public MailMessage() {
    }

    public MailMessage(String to, String subject, String content) {
        this(to, null, subject, content);
    }

    public MailMessage(String to, String from, String subject, String content) {
        this.to = to;
        this.from = from;
        this.subject = subject;
        this.content = content;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public String toString() {
        return "MailMessage{" +
                "to='" + to + '\'' +
                ", from='" + from + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}
