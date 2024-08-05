package io.dev.socialmediaplatform.postmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.dev.socialmediaplatform.postmanagement.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
