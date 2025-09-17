package com.example.demo.controllerTest;

import com.example.demo.repository.Blog;
import com.example.demo.repository.BlogRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
//@ActiveProfiles("prod")
@WithMockUser(username = "testuser", roles = {"USER"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class BlogControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BlogRepository blogRepository;

    @Test
    @DisplayName("GET /blogs/getAllBlogs - Should return all blogs")
    void testGetAllBlogs() throws Exception {
        Blog b1 = new Blog();
        b1.setAuthorName("Alice Johnson");
        b1.setTitle("Spring Boot Basics");
        b1.setContent("Intro to Spring Boot");
        blogRepository.save(b1);

        Blog b2 = new Blog();
        b2.setAuthorName("Bob Smith");
        b2.setTitle("Advanced Java");
        b2.setContent("Java concurrency tips");
        blogRepository.save(b2);

        mockMvc.perform(get("/blogs/getAllBlogs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Spring Boot Basics"))
                .andExpect(jsonPath("$[0].authorName").value("Alice Johnson"))
                .andExpect(jsonPath("$[1].title").value("Advanced Java"))
                .andExpect(jsonPath("$[1].authorName").value("Bob Smith"));
    }

    @Test
    @DisplayName("GET /blogs/getById/{id} - Should return blog by ID")
    void testGetBlogById() throws Exception {
        Blog blog = new Blog();
        blog.setAuthorName("Carol White");
        blog.setTitle("Docker Essentials");
        blog.setContent("Containerization with Docker");
        Blog saved = blogRepository.save(blog);

        mockMvc.perform(get("/blogs/getById/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Docker Essentials"))
                .andExpect(jsonPath("$.authorName").value("Carol White"));
    }

    @Test
    @DisplayName("GET /blogs/getByTitle/{title} - Should return blog by title")
    void testGetByTitle() throws Exception {
        Blog blog = new Blog();
        blog.setAuthorName("David Green");
        blog.setTitle("Kubernetes Guide");
        blog.setContent("Managing clusters effectively");
        blogRepository.save(blog);

        String encodedTitle = URLEncoder.encode("Kubernetes Guide", StandardCharsets.UTF_8);

        mockMvc.perform(get("/blogs/getByTitle/" + encodedTitle))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Kubernetes Guide"))
                .andExpect(jsonPath("$.authorName").value("David Green"));
    }

    @Test
    @DisplayName("POST /blogs/addBlogs - Should create a new blog")
    void testCreateBlog() throws Exception {
        String json = """
                {
                  "title": "Microservices Architecture",
                  "authorName": "Emily Rose",
                  "content": "Building scalable apps"
                }
                """;

        mockMvc.perform(post("/blogs/addBlogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Microservices Architecture"))
                .andExpect(jsonPath("$.authorName").value("Emily Rose"));

        List<Blog> allBlogs = blogRepository.findAll();
        assertThat(allBlogs).hasSize(1);
        assertThat(allBlogs.get(0).getTitle()).isEqualTo("Microservices Architecture");
    }

    @Test
    @DisplayName("DELETE /blogs/deleteBlogById/{id} - Should delete blog by ID")
    void testDeleteBlogById() throws Exception {
        Blog blog = new Blog();
        blog.setAuthorName("Frank Turner");
        blog.setTitle("REST API Design");
        blog.setContent("Best practices for REST");
        Blog saved = blogRepository.save(blog);

        mockMvc.perform(delete("/blogs/deleteBlogById/" + saved.getId()))
                .andExpect(status().isOk());

        boolean exists = blogRepository.existsById(saved.getId());
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("PUT /blogs/updateBlog/{title} - Should update blog content")
    void testUpdateContentByTitle() throws Exception {
        Blog blog = new Blog();
        blog.setAuthorName("Grace Lee");
        blog.setTitle("GraphQL Intro");
        blog.setContent("Old content");
        blogRepository.save(blog);

        String newContent = "Updated content";

        String encodedTitle = URLEncoder.encode("GraphQL Intro", StandardCharsets.UTF_8);

        mockMvc.perform(put("/blogs/updateBlog/" + encodedTitle)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(newContent))
                .andExpect(status().isOk())
                .andExpect(content().string("Blog content updated successfully for title: GraphQL Intro"));

        Blog updated = blogRepository.findByTitle("GraphQL Intro").orElseThrow();
        assertThat(updated.getContent()).isEqualTo(newContent);
    }
}
