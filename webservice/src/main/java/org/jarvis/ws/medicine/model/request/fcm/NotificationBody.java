package org.jarvis.ws.medicine.model.request.fcm;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jarvis.core.model.JsonExcludeNullField;
import org.jarvis.core.model.JsonSnakeCase;
import org.jarvis.core.model.base.AnyObject;

/**
 * Created: KimChheng
 * Date: 24-Oct-2020 Sat
 * Time: 4:04 PM
 */
public class NotificationBody extends AnyObject implements JsonExcludeNullField, JsonSnakeCase {

    private static final long serialVersionUID = 6130138406850572994L;

    private String title;
    private String body;
    private String sound = "default";
    private String badge;
    private String clickAction = "FLUTTER_NOTIFICATION_CLICK";
    private String subTitle;
    private String icon;
    private String tag;
    private String color;
    private String androidChannelId;
    private boolean contentAvailable = true;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getClickAction() {
        return clickAction;
    }

    public void setClickAction(String clickAction) {
        this.clickAction = clickAction;
    }

    @JsonProperty("subtitle")
    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAndroidChannelId() {
        return androidChannelId;
    }

    public void setAndroidChannelId(String androidChannelId) {
        this.androidChannelId = androidChannelId;
    }

    public boolean isContentAvailable() {
        return contentAvailable;
    }

    public void setContentAvailable(boolean contentAvailable) {
        this.contentAvailable = contentAvailable;
    }
}