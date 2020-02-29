package com.bamboo.controller;

import com.bamboo.dao.BlogRepository;
import com.bamboo.pojo.Blog;
import com.bamboo.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 功能描述:<br>
 * 〈〉
 *
 * @author bamboo
 * @create 2019/12/31
 * @since 1.0.0
 */
@Controller
public class ArchiveShowController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private BlogRepository blogRepository;

    @GetMapping("/archives/{year}/{month}")
    public String archives(@PathVariable String year, @PathVariable String month, Model model) {
        Long count = blogService.countBlog();
        if (count == 0) {
            return "archives";
        }
        model.addAttribute("blogCount", count);
        List<String> years = blogService.archiveYear();
        model.addAttribute("years", years);
        List<Blog> blogs = null;
        List<String> months = null;
        if ("-1".equals(year)) {
            year = years.get(0);
            months = blogRepository.findGroupMonth(year);
            month = months.get(0);
            blogs = blogService.archiveBlogByMonth(year, month);
        } else {
            months = blogRepository.findGroupMonth(year);
            if ("-1".equals(month)) {
                month = months.get(0);
                blogs = blogService.archiveBlogByMonth(year, month);
            } else {
                blogs = blogService.archiveBlogByMonth(year, month);
            }
        }
        model.addAttribute("blogs", blogs);
        model.addAttribute("months", months);
        model.addAttribute("activeYear", year);
        model.addAttribute("activeMonth", month);
        /*Map<String, List<Blog>> map = blogService.archiveBlog();
        model.addAttribute("archiveMap", map);*/
        return "archives";
    }

    @GetMapping("/test")
    public void test() {
        Long count = blogRepository.countByPublished();
        System.out.println(count);
    }
}
