package com.example.demo.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.repository.Blog;
import com.example.demo.service.BlogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/blogs")
public class BlogController {
    
    @Autowired
    BlogService blogService;

    @Operation(summary = "Fetch all blogs")
    @GetMapping("/getAllBlogs")
    public List<Blog> getAllBlogs() {
        return blogService.getBlogs();
    }

    @Operation(summary = "Get blog by title")
    @GetMapping("/getByTitle/{title}")
    public Optional<Blog> getByTitle(
        @Parameter(description = "Title of the blog") @PathVariable("title") String title) {

        String decodedTitle = URLDecoder.decode(title, StandardCharsets.UTF_8);
        return blogService.searchByTitle(decodedTitle);
    }

    @Operation(summary = "Get blogs by author name")
    @GetMapping("/getByAuthor/{authorName}")
    public Optional<List<Blog>> getByAuthorName(
        @Parameter(description = "Author name") @PathVariable("authorName") String authorName) {
        return blogService.searchByName(authorName);
    }

    @Operation(summary = "Get blog by ID")
    @GetMapping("/getById/{id}")
    public Optional<Blog> getById(
        @Parameter(description = "Blog ID") @PathVariable("id") Long id) {
        return blogService.searchById(id);
    }

    @Operation(summary = "Add a new blog")
    @PostMapping("/addBlogs")
    public ResponseEntity<Blog> addNewBlogs(
        @Parameter(description = "Blog object to be added") @RequestBody Blog blog) {
        Blog savedBlog = blogService.addBlogs(blog);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBlog);
    }

    @Operation(summary = "Delete blog by ID")
    @DeleteMapping("/deleteBlogById/{id}")
    public void deleteBlog(
        @Parameter(description = "Blog ID to delete") @PathVariable("id") Long id) {
        blogService.deleteBlog(id);
    }

    @Operation(summary = "Delete blog by title")
    @DeleteMapping("/deleteBlogByTitle/{title}")
    public void deleteBlogByTitle(
        @Parameter(description = "Blog title to delete") @PathVariable("title") String title) {
        blogService.deleteByTitle(title);
    }

    @Operation(summary = "Update blog content by title", description = "Updates the content of a blog identified by its title")
    @PutMapping("/updateBlog/{title}")
    public ResponseEntity<String> updateContentByTitle(
        @Parameter(description = "Blog title to update", example = "Rap God") @PathVariable("title") String title,
        @Parameter(description = "New content for the blog", example = "Updated lyrics and analysis") @RequestBody String newContent) {
        String decodedTitle = java.net.URLDecoder.decode(title, java.nio.charset.StandardCharsets.UTF_8);
        blogService.updateContentByTitle(decodedTitle, newContent);
        return ResponseEntity.ok("Blog content updated successfully for title: " + decodedTitle);
    }



}
