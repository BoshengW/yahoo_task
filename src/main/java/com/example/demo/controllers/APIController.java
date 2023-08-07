package com.example.demo.controllers;

import com.example.demo.services.CommentService;
import com.example.demo.vo.PostCmtNode;
import com.example.demo.vo.RawCmtVO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("yahootask")
public class APIController {

    @Autowired
    CommentService commentService;

    @RequestMapping(path="check", method= RequestMethod.GET)
    public String healthCheck() {
        return "health check pass";
    }

    @RequestMapping(path="comments", method= RequestMethod.GET)
    public String getRawDataResult() throws Exception {
        List<PostCmtNode> tree = commentService.getTree();
        JSONObject response = new JSONObject();
        for (PostCmtNode root: tree) {
            response.put(root.getId().toString(), convertTreeToJson(root));
        }

        return response.toString();
    }

    @RequestMapping(path="commentsByDepth", method= RequestMethod.GET)
    public String getRawDataResult(@RequestParam int depth) throws Exception {
        List<PostCmtNode> tree = commentService.getTree();
        JSONObject response = new JSONObject();
        for (PostCmtNode root: tree) {
            response.put(root.getId().toString(), getByDepth(root, depth));
        }

        return response.toString();
    }

    public static JSONObject convertTreeToJson(PostCmtNode node) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", node);

        if (node.getChilds() != null && !node.getChilds().isEmpty()) {
            JSONArray jsonArray = new JSONArray();
            for (PostCmtNode child : node.getChilds()) {
                jsonArray.put(convertTreeToJson(child));
            }
            jsonObject.put("children", jsonArray);
        }

        return jsonObject;
    }

//    public static JSONObject getByRootNDepth(List<PostCmtNode> tree, int depth, UUID root) {
////        for (PostCmtNode node: )
//    }

    public static JSONObject getByDepth(PostCmtNode node, int depth) {
        if (node.getChilds().isEmpty()) {
            JSONObject res = new JSONObject();
            res.put(node.getId().toString(), node);
            return res;
        }

        if (depth==0) {
            JSONObject res = new JSONObject();
            res.put(node.getId().toString(), node);
            return res;
        }

        JSONObject csrt = new JSONObject();
        for(PostCmtNode child: node.getChilds()) {
            csrt.put(child.getId().toString(), getByDepth(child, depth-1));
        }

        return csrt;
    }


}
