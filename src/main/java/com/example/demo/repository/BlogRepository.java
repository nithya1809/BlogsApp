package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    // Find blog by exact title
    @Query("SELECT b FROM Blog b WHERE b.title = :title")
    Optional<Blog> findByTitle(@Param("title") String title);

    // Find blogs by author name
    @Query("SELECT b FROM Blog b WHERE b.authorName = :authorName")
    Optional<List<Blog>> findByAuthorName(@Param("authorName") String authorName);
    
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Blog b SET b.content = :content WHERE b.title = :title")
    void updateContentByTitle(@Param("title") String title, @Param("content") String content);


    void deleteByTitle(String title);

}
