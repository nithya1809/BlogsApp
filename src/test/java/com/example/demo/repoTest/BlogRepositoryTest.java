package com.example.demo.repoTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.repository.Blog;
import com.example.demo.repository.BlogRepository;

@DataJpaTest
//@ActiveProfiles("prod")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BlogRepositoryTest {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Save blog")
    public void testSaveBlog() {
        Blog blog = new Blog();
        blog.setAuthorName("Eminem");
        blog.setTitle("Rap God");
        blog.setContent("Lyrics and analysis");

        Blog saved = blogRepository.save(blog);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("Find blog by ID")
    public void testFindById() {
        Blog blog = new Blog();
        blog.setAuthorName("Eminem");
        blog.setTitle("Lose Yourself");
        blog.setContent("Motivational lyrics");

        Blog saved = entityManager.persistAndFlush(blog);
        Optional<Blog> found = blogRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Lose Yourself");
    }

    @Test
    @DisplayName("Custom query - filter by author name")
    public void testFilterByName() {
        Blog blog = new Blog();
        blog.setAuthorName("Eminem");
        blog.setTitle("Not Afraid");
        blog.setContent("Empowering lyrics");

        entityManager.persistAndFlush(blog);

        Optional<List<Blog>> result = blogRepository.findByAuthorName("Eminem");
        assertThat(result).isPresent();
        assertThat(result.get()).hasSizeGreaterThanOrEqualTo(1);
        assertThat(result.get().get(0).getTitle()).isEqualTo("Not Afraid");
    }

    @Test
    @DisplayName("Custom query - filter by title")
    public void testFilterByTitle() {
        Blog blog = new Blog();
        blog.setAuthorName("Eminem");
        blog.setTitle("Mockingbird");
        blog.setContent("Emotional lyrics");

        entityManager.persistAndFlush(blog);

        Optional<Blog> result = blogRepository.findByTitle("Mockingbird");
        assertThat(result).isPresent();
        assertThat(result.get().getAuthorName()).isEqualTo("Eminem");
    }

    @Test
    @DisplayName("Delete blog by title")
    @Transactional
    public void testDeleteByTitle() {
        Blog blog = new Blog();
        blog.setAuthorName("Eminem");
        blog.setTitle("Cleanin' Out My Closet");
        blog.setContent("Confessional lyrics");

        entityManager.persistAndFlush(blog);

        blogRepository.deleteByTitle("Cleanin' Out My Closet");

        Optional<Blog> result = blogRepository.findByTitle("Cleanin' Out My Closet");
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Update blog content by title")
    @Transactional
    public void testUpdateContentByTitle() {
        Blog blog = new Blog();
        blog.setAuthorName("Eminem");
        blog.setTitle("Stan");
        blog.setContent("Original content");

        entityManager.persistAndFlush(blog);

        blogRepository.updateContentByTitle("Stan", "Updated content");

        Optional<Blog> updated = blogRepository.findByTitle("Stan");
        assertThat(updated).isPresent();
        assertThat(updated.get().getContent()).isEqualTo("Updated content");
    }
}
