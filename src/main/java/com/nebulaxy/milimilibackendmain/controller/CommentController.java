package com.nebulaxy.milimilibackendmain.controller;


import com.nebulaxy.milimilibackendmain.common.Result;
import com.nebulaxy.milimilibackendmain.entity.Comment;
import com.nebulaxy.milimilibackendmain.service.CommentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Resource
    CommentService commentService;

    /**
     * 新增评论
     */
    @PostMapping("/addComment")
    public Result addComment(@RequestBody Comment comment) {
        //@RequestBody 接收前端传来的 json 数据
        commentService.addComment(comment);
        return Result.success();
    }

    /**
     * 新增回复
     */
    @PostMapping("/addReply")
    public Result addReply(@RequestBody Comment comment) {
        //@RequestBody 接收前端传来的 json 数据
        commentService.addReply(comment);
        return Result.success();
    }

    /**
     * 查询评论
     */
    @GetMapping("/selectComment")
    public Result selectComment(@RequestParam Integer vid) {
        List<Comment> commentsList = commentService.getCommentByVid(vid);
        return Result.success(commentsList);
    }

    /**
     * 点赞
     */
    @PutMapping("/like/{id}")
    public Result like(@PathVariable Integer id) {
        commentService.like(id);
        return Result.success();
    }

    /**
     * 取消点赞
     */
    @PutMapping("/unlike/{id}")
    public Result unlike(@PathVariable Integer id) {
        commentService.unlike(id);
        return Result.success();
    }
}
