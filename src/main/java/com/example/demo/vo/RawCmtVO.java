package com.example.demo.vo;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class RawCmtVO {

    UUID uuid;

    UUID parent;

    Long timestamp;

    UserVO user;

    String comment;

    Long upvotes;

    public RawCmtVO(JSONObject json) {
        this.uuid = UUID.fromString(json.getString("id"));
        JSONObject userName = (JSONObject) json.get("user");
        this.user = new UserVO(userName.getString("name"));
        this.timestamp = json.getLong("timestamp");
        this.comment = json.getString("text");
        this.upvotes = json.getLong("upvotes");
        if (json.has("parent")) this.parent = UUID.fromString(json.getString("parent"));
    }
}
