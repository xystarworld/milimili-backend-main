package com.nebulaxy.milimilibackendmain.mapper;

import com.nebulaxy.milimilibackendmain.entity.Comment;

import java.util.List;

public interface CommentMapper {

    void insertComment(Comment comment);

    List<Comment> selectCommentAll(Integer id);

    List<Comment> selectCommentByVid(Integer vid);

    void insertReply(Comment comment);

    void like(Integer id);

    void unlike(Integer id);
}
