package com.bamboo.service;

import com.bamboo.exception.NotFoundException;
import com.bamboo.dao.BlogRepository;
import com.bamboo.pojo.Blog;
import com.bamboo.pojo.Type;
import com.bamboo.utils.MarkdownUtils;
import com.bamboo.utils.MyBeanUtils;
import com.bamboo.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * 功能描述:<br>
 * 〈〉
 *
 * @author bamboo
 * @create 2019/12/28
 * @since 1.0.0
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Blog queryBlog(Long id) {
        return blogRepository.findById(id).get();
    }

    @Transactional
    @Override
    public Blog queryAndConvert(Long id) {
        Blog blog = blogRepository.findById(id).get();
        if (blog == null) {
            throw new NotFoundException("该博客不存在");
        }
        blogRepository.updateViews(id);
        Blog b = new Blog();
        BeanUtils.copyProperties(blog, b);
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        b.setViews(b.getViews() + 1);
        return b;
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicates.add(cb.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
                }
                if (blog.getTypeId() != null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if (blog.isRecommend()) {
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("tags");
                return cb.equal(join.get("id"), tagId);
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query, pageable);
    }

    @Override
    public Page<Blog> listPublishedBlog(Pageable pageable) {
        return blogRepository.findByPublished(pageable);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogRepository.findTopByRecommend(pageable);
    }

    @Override
    public List<Blog> listPublishedBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogRepository.findTopByPublished(pageable);
    }

    @Override
    public List<Blog> listBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogRepository.findTop(pageable);
    }

    @Override
    public List<String> archiveYear() {
        return blogRepository.findGroupYear();
    }

    @Override
    public List<String> archiveMonth(String year) {
        return blogRepository.findGroupMonth(year);
    }

    @Override
    public List<Blog> archiveBlogByMonth(String year,String month) {
        List<Blog> list = blogRepository.findByMonth(month, year);
        return list;
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years) {
            Map<String, List<Blog>> monthMap = new HashMap<>();
            List<String> months = blogRepository.findGroupMonth(year);
            for (String month : months) {
                List<Blog> blogs = blogRepository.findByMonth(month, year);
                monthMap.put(month, blogs);
            }
            map.put(year, blogRepository.findByYear(year));
        }
        return map;
    }



    @Override
    public Long countBlog() {
        return blogRepository.countByPublished();
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null) {
            blog.setCreateTime(new Date());
            blog.setViews(0);
        }
        blog.setUpdateTime(new Date());
        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog modifyBlog(Long id, Blog blog) {
        Blog b = blogRepository.findById(id).get();
        if (b == null) {
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog, b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    @Transactional
    @Override
    public void removeBlog(Long id) {
        blogRepository.deleteById(id);
    }

}
