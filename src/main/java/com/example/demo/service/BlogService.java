package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.Blog;
import com.example.demo.repository.BlogRepository;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    public List<Blog> getBlogs() {
        return blogRepository.findAll();
    }

    public Blog addBlogs(Blog blog) {
        return blogRepository.save(blog);
    }

    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    public Optional<Blog> searchById(Long id) {
        return blogRepository.findById(id);
    }

    public Optional<List<Blog>> searchByName(String authorName) {
        return blogRepository.findByAuthorName(authorName);
    }

    public Optional<Blog> searchByTitle(String title) {
        return blogRepository.findByTitle(title);
    }

    public void deleteByTitle(String title) {
        blogRepository.deleteByTitle(title);
    }

    public void updateContentByTitle(String title, String newContent) {
        blogRepository.updateContentByTitle(title, newContent);
    }
}
