package com.example.demo.controllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.controller.BlogController;
import com.example.demo.repository.Blog;
import com.example.demo.service.BlogService;

@WebMvcTest(BlogController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BlogControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BlogService blogService;

    Blog blog;

    @BeforeEach
    public void setup() {
        blog = new Blog();
        blog.setId(1L);
        blog.setTitle("Rap God");
        blog.setContent("Lyrics and analysis");
        blog.setAuthorName("Eminem");
    }

    @Test
    @DisplayName("GET all blogs")
    public void getAllBlogsTest() throws Exception {
        when(blogService.getBlogs()).thenReturn(List.of(blog));

        mockMvc.perform(get("/blogs/getAllBlogs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Rap God"));
    }

    @Test
    @DisplayName("GET blog by title")
    public void getBlogByTitleTest() throws Exception {
        when(blogService.searchByTitle("Rap God")).thenReturn(Optional.of(blog));

        mockMvc.perform(get("/blogs/getByTitle/Rap%20God"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorName").value("Eminem"));
    }

    @Test
    @DisplayName("GET blog by ID")
    public void getBlogByIdTest() throws Exception {
        when(blogService.searchById(1L)).thenReturn(Optional.of(blog));

        mockMvc.perform(get("/blogs/getById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Rap God"));
    }

    @Test
    @DisplayName("POST new blog")
    public void addBlogTest() throws Exception {
        Blog newBlog = new Blog();
        newBlog.setId(2L);
        newBlog.setTitle("Lose Yourself");
        newBlog.setContent("Motivational lyrics");
        newBlog.setAuthorName("Eminem");

        when(blogService.addBlogs(any(Blog.class))).thenReturn(newBlog);

        String jsonObj = """
                {
                    "title": "Lose Yourself",
                    "content": "Motivational lyrics",
                    "author": "Eminem"
                }
                """;

        mockMvc.perform(post("/blogs/addBlogs")
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Lose Yourself"));
    }

    @Test
    @DisplayName("PUT update blog content by title")
    public void updateBlogContentTest() throws Exception {
        mockMvc.perform(put("/blogs/updateBlog/Rap%20God")
                .content("Updated content")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("Blog content updated successfully for title: Rap God"));
    }

    @Test
    @DisplayName("DELETE blog by ID")
    public void deleteBlogByIdTest() throws Exception {
        mockMvc.perform(delete("/blogs/deleteBlogById/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE blog by title")
    public void deleteBlogByTitleTest() throws Exception {
        mockMvc.perform(delete("/blogs/deleteBlogByTitle/Rap%20God"))
                .andExpect(status().isOk());
    }
}
