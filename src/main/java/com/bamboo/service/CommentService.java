package com.bamboo.service;

import com.bamboo.pojo.Comment;

import java.util.List;

public interface CommentService {
    Comment queryCommentById(Long commentId);

    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);

    void removeComment(Comment comment);
}
