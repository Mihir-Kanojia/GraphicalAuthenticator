package com.example.graphicalauthenticator.model;

import com.google.firebase.firestore.DocumentId;
import com.google.gson.annotations.SerializedName;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public
class User {

   @DocumentId
   private String id;

   @SerializedName("name")
   private String name;

   @SerializedName("patternUri")
   private String patternUri;

   @ServerTimestamp
   public Date lastDate;

   @ServerTimestamp
   public Date createDate;

   public User(){

   }

   public User(String name, String patternUri, Date lastDate) {
      this.name = name;
      this.patternUri = patternUri;
      this.lastDate = lastDate;
      this.createDate = new Date();

   }
}
