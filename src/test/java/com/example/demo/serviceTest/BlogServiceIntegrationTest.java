package com.example.demo.serviceTest;
 
import static org.assertj.core.api.Assertions.assertThat;
 
import java.util.List;
import java.util.Optional;
 
import com.example.demo.repository.Blog;
import com.example.demo.service.BlogService;
 
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
 
@SpringBootTest
@Transactional
@Sql(scripts = "/blog-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BlogServiceIntegrationTest {
 
    @Autowired
    private BlogService blogService;
 
    @Test
    @DisplayName("Integration - Get all blogs")
    public void getAllBlogsTest() {
        List<Blog> blogs = blogService.getBlogs();
        assertThat(blogs).isNotEmpty();
        assertThat(blogs.get(0).getTitle()).isEqualTo("Rap God");
    }
 
    @Test
    @DisplayName("Integration - Add new blog")
    public void addBlogTest() {
        Blog blog = new Blog();
        blog.setAuthorName("Eminem");
        blog.setTitle("Lose Yourself");
        blog.setContent("Motivational lyrics");
 
        Blog saved = blogService.addBlogs(blog);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Lose Yourself");
    }
 
    @Test
    @DisplayName("Integration - Find blog by ID")
    public void searchByIdTest() {
        
        Blog blog = new Blog();
        blog.setAuthorName("Eminem");
        blog.setTitle("Not Afraid");
        blog.setContent("I’m not afraid to take a stand");
 
        Blog saved = blogService.addBlogs(blog);
        Optional<Blog> found = blogService.searchById(saved.getId());
 
        assertThat(found).isPresent();
        assertThat(found.get().getAuthorName()).isEqualTo("Eminem");
    }
 
 
    @Test
    @DisplayName("Integration - Find blog by title")
    public void searchByTitleTest() {
        Optional<Blog> found = blogService.searchByTitle("Rap God");
        assertThat(found).isPresent();
        assertThat(found.get().getContent()).contains("Lyrics");
    }
 
    @Test
    @DisplayName("Integration - Find blog by author name")
    public void searchByAuthorNameTest() {
        Optional<List<Blog>> found = blogService.searchByName("Eminem");
        assertThat(found).isPresent();
        assertThat(found.get()).hasSizeGreaterThanOrEqualTo(1);
    }
 
    @Test
    @DisplayName("Integration - Delete blog by ID")
    public void deleteBlogByIdTest() {
        blogService.deleteBlog(1L);
        Optional<Blog> result = blogService.searchById(1L);
        assertThat(result).isEmpty();
    }
 
    @Test
    @DisplayName("Integration - Delete blog by title")
    public void deleteBlogByTitleTest() {
        blogService.deleteByTitle("Rap God");
        Optional<Blog> result = blogService.searchByTitle("Rap God");
        assertThat(result).isEmpty();
    }
 
    @Test
    @DisplayName("Integration - Update blog content by title")
    public void updateContentByTitleTest() {
        blogService.updateContentByTitle("Rap God", "Updated lyrics and meaning");
        Optional<Blog> updated = blogService.searchByTitle("Rap God");
        assertThat(updated).isPresent();
        assertThat(updated.get().getContent()).isEqualTo("Updated lyrics and meaning");
    }
 
    @Test
    @DisplayName("Integration - Missing blog by ID")
    public void searchByMissingIdTest() {
        Optional<Blog> result = blogService.searchById(999L);
        assertThat(result).isEmpty();
    }
}