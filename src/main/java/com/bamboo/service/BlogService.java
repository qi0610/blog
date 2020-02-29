package com.bamboo.service;

import com.bamboo.pojo.Blog;
import com.bamboo.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {

    Blog queryBlog(Long id);

    Blog queryAndConvert(Long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlog(Long tagId, Pageable pageable);

    Page<Blog> listBlog(String query, Pageable pageable);

    Page<Blog> listPublishedBlog(Pageable pageable);

    List<Blog> listRecommendBlogTop(Integer size);

    List<Blog> listPublishedBlogTop(Integer size);

    List<Blog> listBlogTop(Integer size);

    List<String> archiveYear();

    List<String> archiveMonth(String year);

    List<Blog> archiveBlogByMonth(String year,String month);

    Map<String,List<Blog>> archiveBlog();

    Long countBlog();

    Blog saveBlog(Blog blog);

    Blog modifyBlog(Long id, Blog blog);

    void removeBlog(Long id);
}
