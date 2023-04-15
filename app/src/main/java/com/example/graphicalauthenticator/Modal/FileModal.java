package com.example.graphicalauthenticator.Modal;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class FileModal {

    public String id;

    public String name;
    public long size;
    public String type;
    public Date uploadDateTime;
    public String url;

    public FileModal(String name, long size, String type, String url) {
        this.name = name;
        this.size = size;
        this.type = type;
        this.uploadDateTime = new Date();
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getUploadDateTime() {
        return uploadDateTime;
    }

    public void setUploadDateTime(Date uploadDateTime) {
        this.uploadDateTime = uploadDateTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
