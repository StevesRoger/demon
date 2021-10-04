package org.jarvis.ws.medicine.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.jarvis.ws.medicine.model.UserContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

/**
 * Created: kim chheng
 * Date: 19-Jan-2019 Sat
 * Time: 4:40 PM
 */
public class ImageEntity extends BaseEntity implements UserContext {

    private static final long serialVersionUID = -1673744745000510267L;

    private String name;
    private String mineType;
    @JsonIgnore
    private byte[] bytes;
    private String path;
    private String url;
    private String createdBy;
    private Date createdDate;

    public ImageEntity() {
    }

    public ImageEntity(MultipartFile file) throws IOException {
        this(file.getOriginalFilename(), file.getContentType(), file.getBytes());
    }

    public ImageEntity(String name, String mineType, byte[] bytes) {
        this(name, mineType, bytes, null);
    }

    public ImageEntity(String name, String mineType, byte[] bytes, String path) {
        this.name = name;
        this.mineType = mineType;
        this.bytes = bytes;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMineType() {
        return mineType;
    }

    public void setMineType(String mineType) {
        this.mineType = mineType;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static ImageEntity imageFromClassPath(String path) throws IOException {
        ImageEntity image = new ImageEntity();
        image.setBytes(IOUtils.toByteArray(Objects.requireNonNull(image.getClass().getClassLoader().getResourceAsStream(path))));
        image.setMineType("image/" + FilenameUtils.getExtension(path));
        image.setName(FilenameUtils.getName(path));
        return image;
    }

}
