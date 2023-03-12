package com.example.graphicalauthenticator.model;

import com.google.firebase.firestore.DocumentId;
import com.google.gson.annotations.SerializedName;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public
class User {

    @DocumentId
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("pathURI")
    private List<Integer> imageList;

    @ServerTimestamp
    public Date currDate;

    @ServerTimestamp
    public Date createDate;

    public User() {

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

    public List<Integer> getPathURI() {
        return imageList;
    }

    public void setPathURI(List<Integer> imageList) {
        this.imageList = imageList;
    }

    public Date getCurrDate() {
        return currDate;
    }

    public void setCurrDate(Date currDate) {
        this.currDate = currDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User(String name, List<Integer> imageList, Date currDate) {
        this.name = name;
        this.imageList = imageList;
        this.currDate = currDate;
        this.createDate = new Date();

    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("imageList", imageList);
        map.put("currDate", currDate);
        map.put("createDate", createDate);
        return map;
    }
}
