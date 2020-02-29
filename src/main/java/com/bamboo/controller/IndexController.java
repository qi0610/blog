package com.bamboo.controller;

import com.bamboo.service.BlogService;
import com.bamboo.service.TagService;
import com.bamboo.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 功能描述:<br>
 * 〈〉
 *
 * @author bamboo
 * @create 2019/12/24
 * @since 1.0.0
 */
@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String index(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model) {
        model.addAttribute("page", blogService.listPublishedBlog(pageable));
        model.addAttribute("types", typeService.listTypeTop(6));
        model.addAttribute("tags", tagService.listTagTop(10));
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(8));
        return "index";
    }

    @RequestMapping("/search")
    public String search(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String query, Model model) {
        model.addAttribute("page", blogService.listBlog("%"+query+"%", pageable));
        model.addAttribute("query", query);
        return "search";
    }

    @GetMapping("/blogs/{id}")
    public String blog(@PathVariable Long id, Model model) {
        model.addAttribute("blog", blogService.queryAndConvert(id));
        return "blog";
    }

    @GetMapping("/footer/newBlogs")
    public String newBlogs(Model model) {
        model.addAttribute("newBlogs", blogService.listPublishedBlogTop(3));
        return "_fragments :: newBlogList";
    }

    @GetMapping("/admin/footer/newBlogs")
    public String adminNewBlogs(Model model) {
        model.addAttribute("newBlogs", blogService.listBlogTop(3));
        return "_fragments :: newBlogList";
    }

}
