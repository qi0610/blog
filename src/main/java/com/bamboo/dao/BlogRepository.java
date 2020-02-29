package com.bamboo.dao;

import com.bamboo.pojo.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    @Query("select count(b) from Blog b where b.published = true")
    Long countByPublished();

    @Query("select b from Blog b where b.recommend = true and b.published = true")
    List<Blog> findTopByRecommend(Pageable pageable);

    @Query("select b from Blog b where b.published = true")
    List<Blog> findTopByPublished(Pageable pageable);

    @Query("select b from Blog b")
    List<Blog> findTop(Pageable pageable);

    @Query("select b from Blog b where b.title like ?1 or b.content like ?1")
    Page<Blog> findByQuery(String query, Pageable pageable);

    @Query("select b from Blog b where b.published = true")
    Page<Blog> findByPublished(Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Blog b set b.views = b.views+1 where b.id = ?1")
    int updateViews(Long id);

    @Query("select function('date_format',b.updateTime,'%Y') as year from Blog b where b.published = true group by function('date_format',b.updateTime,'%Y') order by year desc")
    List<String> findGroupYear();

    @Query("select function('date_format',b.updateTime,'%M') as month from Blog b where function('date_format',b.updateTime,'%Y') = ?1 and b.published = true  group by function('date_format',b.updateTime,'%M') order by month desc")
    List<String> findGroupMonth(String year);

    @Query("select b from Blog b where function('date_format', b.updateTime, '%Y') = ?1 and b.published = true")
    List<Blog> findByYear(String year);

    @Query("select b from Blog b where function('date_format',b.updateTime,'%M') = ?1 and function('date_format', b.updateTime, '%Y') = ?2 and b.published = true order by b.updateTime desc")
    List<Blog> findByMonth(String month, String year);
}
