package io.dev.socialmediaplatform.postmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.querydsl.core.BooleanBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.dev.socialmediaplatform.exception.PostNotFoundException;
import io.dev.socialmediaplatform.postmanagement.dto.PostDto;
import io.dev.socialmediaplatform.postmanagement.entity.Post;
import io.dev.socialmediaplatform.postmanagement.repository.PostRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    @Autowired
    private final PostRepository postRepository;

    public Page<Post> retrievePosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @CachePut(value = "post", key = "#savedPost.id")
    public PostDto createPost(PostDto postDto) {
        log.info("Entering createPost()");
        Post post = new Post();
        post.setContent(postDto.getContent());
        post.setUserId(postDto.getUserId());
        Post savedPost = postRepository.save(post);
        PostDto responsePostDto = buildPostDto(savedPost);
        log.info("Leaving createPost()");
        return responsePostDto;
    }

    @Cacheable(value = "post", key = "#postId")
    public PostDto retrievePostById(long postId) {
        log.info("Entering retrievePostById()");
        if (postId <= 0) {
            throw new PostNotFoundException("Provide valid post id", HttpStatus.BAD_REQUEST);
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found.", HttpStatus.NOT_FOUND));
        log.info("Leaving retrievePostById()");
        PostDto postDto = buildPostDto(post);
        log.info("Leaving retrievePostById()");
        return postDto;

    }

    @CachePut(value = "post", key = "#postId")
    public PostDto updatePost(long postId, PostDto updatedPostDto) {
        log.info("Entering updatePost()");
        if (postId <= 0) {
            throw new PostNotFoundException("Provide valid post id", HttpStatus.BAD_REQUEST);
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found.", HttpStatus.NOT_FOUND));
        post.setContent(updatedPostDto.getContent());
        Post savedPost = postRepository.save(post);
        PostDto postDto = buildPostDto(savedPost);
        log.info("Leaving updatePost()");
        return postDto;
    }

    @CacheEvict(value = "post", key = "#postId")
    public void deletePost(long postId) {
        log.info("Entering deletePost()");
        if (postId <= 0) {
            throw new PostNotFoundException("Provide valid post id", HttpStatus.BAD_REQUEST);
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found.", HttpStatus.NOT_FOUND));
        postRepository.deleteById(post.getId());
        log.info("Leaving deletePost()");
    }

    private PostDto buildPostDto(Post post) {
        log.info("Entering buildPostDto()");
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setContent(post.getContent());
        postDto.setUserId(post.getUserId());
        log.info("Leaving buildPostDto()");
        return postDto;
    }

    private BooleanBuilder buildSearchOrFilterPredicate(MultiValueMap<String, String> searchOrFilterParameters) {
        log.info("Entering buildSearchOrFilterPredicate()");
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        log.info("Leaving buildSearchOrFilterPredicate()");
        return booleanBuilder;
    }
}
