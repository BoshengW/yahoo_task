package com.example.demo.services;

import com.example.demo.vo.PostCmtNode;
import com.example.demo.vo.RawCmtVO;
import com.example.demo.vo.UserVO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService {

    public static final String COMMENTURL = "https://gist.githubusercontent.com/jaredpetker/3541eb360a9836ad09eb94cffd946645/raw/05772483c421c4feba669425c9557a2c0a0f0416/comments.json";

    List<PostCmtNode> tree = new ArrayList<>();
    HashMap<UUID, RawCmtVO> lookup = new HashMap<>();
    HashMap<UUID, PostCmtNode> lookup2 = new HashMap<>();

    public JSONObject loadDataModel() throws Exception {
        URL url = new URL(COMMENTURL);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            sb.append(output);
        }
        conn.disconnect();

        JSONObject rawJson = new JSONObject(sb.toString());
        return rawJson;
    }

    public void processRawCmt2Graph(JSONObject json) {
        JSONArray list = (JSONArray) json.get("comments");
        for (int i=0; i<list.length(); i++) {
            JSONObject cmt = list.getJSONObject(i);
            RawCmtVO vo = new RawCmtVO(cmt);
            PostCmtNode pVO = new PostCmtNode(vo);
            lookup.put(vo.getUuid(), vo);
            lookup2.put(pVO.getId(), pVO);
        }

        for (UUID id: lookup.keySet()) {
            RawCmtVO item = lookup.get(id);
            PostCmtNode pItem = lookup2.get(id);
            if (item.getParent()==null) {
                this.tree.add(pItem);
            } else {
                RawCmtVO parent = lookup.get(item.getParent());
                PostCmtNode parent2 = lookup2.get(item.getParent());
                if (parent2 != null) {
                    parent2.getChilds().add(pItem);
                }
            }
        }
        System.out.println("done");
    }



    @PostConstruct
    public void init() throws Exception {
        processRawCmt2Graph(loadDataModel());
    }

    public List<PostCmtNode> getTree() {
        return this.tree;
    }

}
