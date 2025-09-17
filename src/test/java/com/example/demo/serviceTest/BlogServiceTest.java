package com.example.demo.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.example.demo.repository.Blog;
import com.example.demo.repository.BlogRepository;
import com.example.demo.service.BlogService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BlogServiceTest {

    @Mock
    private BlogRepository blogRepository;

    @InjectMocks
    private BlogService blogService;

    private Blog blog;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        blog = new Blog();
        blog.setId(1L);
        blog.setAuthorName("Eminem");
        blog.setTitle("Rap God");
        blog.setContent("Lyrics and analysis");
    }

    @Test
    @DisplayName("Get all blogs")
    public void testGetBlogs() {
        when(blogRepository.findAll()).thenReturn(List.of(blog));
        List<Blog> blogs = blogService.getBlogs();
        assertThat(blogs).hasSize(1);
        assertThat(blogs.get(0).getTitle()).isEqualTo("Rap God");
    }

    @Test
    @DisplayName("Add a new blog")
    public void testAddBlog() {
        when(blogRepository.save(blog)).thenReturn(blog);
        Blog saved = blogService.addBlogs(blog);
        assertThat(saved).isNotNull();
        assertThat(saved.getAuthorName()).isEqualTo("Eminem");
    }

    @Test
    @DisplayName("Search blog by ID")
    public void testSearchById() {
        when(blogRepository.findById(1L)).thenReturn(Optional.of(blog));
        Optional<Blog> found = blogService.searchById(1L);
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Rap God");
    }

    @Test
    @DisplayName("Search blog by author name")
    public void testSearchByAuthorName() {
        when(blogRepository.findByAuthorName("Eminem")).thenReturn(Optional.of(List.of(blog)));
        Optional<List<Blog>> found = blogService.searchByName("Eminem");
        assertThat(found).isPresent();
        assertThat(found.get().get(0).getTitle()).isEqualTo("Rap God");
    }

    @Test
    @DisplayName("Search blog by title")
    public void testSearchByTitle() {
        when(blogRepository.findByTitle("Rap God")).thenReturn(Optional.of(blog));
        Optional<Blog> found = blogService.searchByTitle("Rap God");
        assertThat(found).isPresent();
        assertThat(found.get().getAuthorName()).isEqualTo("Eminem");
    }

    @Test
    @DisplayName("Delete blog by ID")
    public void testDeleteBlogById() {
        blogService.deleteBlog(1L);
        verify(blogRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Delete blog by title")
    public void testDeleteBlogByTitle() {
        blogService.deleteByTitle("Rap God");
        verify(blogRepository, times(1)).deleteByTitle("Rap God");
    }

    @Test
    @DisplayName("Update blog content by title")
    public void testUpdateContentByTitle() {
        blogService.updateContentByTitle("Rap God", "Updated content");
        verify(blogRepository, times(1)).updateContentByTitle("Rap God", "Updated content");
    }
}
