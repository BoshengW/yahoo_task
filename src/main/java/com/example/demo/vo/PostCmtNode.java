package com.example.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class PostCmtNode {
    UUID id;
    UserVO user;
    String comment;
    Long timestamp;

    List<PostCmtNode> childs;

    public PostCmtNode(UUID id, UserVO user) {
        // used for create root
        this.id = id;
        this.user = user;
    }

    public PostCmtNode(RawCmtVO rawVO) {
        this.id = rawVO.getUuid();
        this.user = rawVO.getUser();
        this.comment = rawVO.getComment();
        this.timestamp = rawVO.getTimestamp();
        this.childs = new ArrayList<>();
    }
}
