package com.nebulaxy.milimilibackendmain.service;

import com.nebulaxy.milimilibackendmain.entity.Comment;
import com.nebulaxy.milimilibackendmain.exception.CustomerException;
import com.nebulaxy.milimilibackendmain.mapper.CommentMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Resource
    CommentMapper commentMapper;

    public void addComment(Comment comment) {
        commentMapper.insertComment(comment);
    }

    public List<Comment> getCommentByVid(Integer vid) {
        return commentMapper.selectCommentByVid(vid);
    }

    public void addReply(Comment comment) {
        commentMapper.insertReply(comment);
    }

    public void like(Integer id) {
        commentMapper.like(id);
    }

    public void unlike(Integer id) {
        commentMapper.unlike(id);
    }
}
