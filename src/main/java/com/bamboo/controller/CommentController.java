package com.bamboo.controller;

import com.bamboo.pojo.Comment;
import com.bamboo.pojo.User;
import com.bamboo.service.BlogService;
import com.bamboo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

/**
 * 功能描述:<br>
 * 〈〉
 *
 * @author bamboo
 * @create 2019/12/30
 * @since 1.0.0
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {
        model.addAttribute("comments", commentService.listCommentByBlogId(blogId));
        return "blog :: commentList";
    }

    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session) {
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.queryBlog(blogId));
        User user = (User) session.getAttribute("user");
        if (user != null) {
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
            // comment.setNickname(user.getNickname());
        } else {
            comment.setAvatar(avatar);
        }
        commentService.saveComment(comment);
        return "redirect:/comments/" + blogId;
    }

    @PostMapping("/admin/remove")
    public String remove(Comment comment) {
        Long blogId = comment.getBlog().getId();
        Long commentId = comment.getId();
        Comment comment1 = commentService.queryCommentById(commentId);
        commentService.removeComment(comment1);
        return "redirect:/comments/" + blogId;
    }

}
