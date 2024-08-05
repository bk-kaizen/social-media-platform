package io.dev.socialmediaplatform.postmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import io.dev.socialmediaplatform.postmanagement.dto.PostDto;
import io.dev.socialmediaplatform.postmanagement.entity.Post;
import io.dev.socialmediaplatform.postmanagement.service.PostService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@RestController
@RequestMapping("/api/posts")
@Tag(name = "Post Management", description = "APIs for managing posts")
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * Create a new post.
     * @param  postDto the post to be created
     * @return      the created post
     */

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
        log.info("Creating a new post with content: {}", postDto.getContent());
        PostDto savedPost = postService.createPost(postDto);
        log.info("Post created with ID: {}", savedPost.getId());
        return ResponseEntity.ok(savedPost);
    }

    @GetMapping
    public ResponseEntity<Page<Post>> retrievePosts(
            @Parameter(hidden = true, in = ParameterIn.QUERY, style = ParameterStyle.FORM)
            @SortDefault(sort = "title", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) MultiValueMap<String, String> searchParameters) {
        log.info("Retrieving all posts with pagination - page: {}, size: ");
        Page<Post> posts = postService.retrievePosts(pageable);
        log.info("Retrieved {} posts", posts);
        return ResponseEntity.ok(posts);
    }

    /**
     * Retrieve a specific post by ID.
     * @param  postId the ID of the post to retrieve
     * @return        the retrieved post
     */
    // @GetMapping("/{postId}")
    // public ResponseEntity<PostDto> retrievePost(@PathVariable long postId) {
    // log.info("Retrieving post with ID: {}", postId);
    // return postService.retrievePostById(postId).map(post -> {
    // return ResponseEntity.ok(post);
    // }).orElseGet(() -> {
    // return ResponseEntity.notFound().build();
    // });
    // }

    /**
     * Update a specific post by ID.
     * @param  postId      the ID of the post to update
     * @param  postDto the updated post details
     * @return             the updated post
     */
//    @PutMapping("/{postId}")
//    public ResponseEntity<PostDto> updatePost(@PathVariable long postId, @RequestBody PostDto postDto) {
//        log.info("Updating post with ID: {}", postId);
//        return postService.updatePost(postId, postDto).map(post -> {
//            log.info("Post updated: {}", post);
//            return ResponseEntity.ok(post);
//        }).orElseGet(() -> {
//            log.warn("Post with ID {} not found", postId);
//            return ResponseEntity.notFound().build();
//        });
//    }

    /**
     * Delete a specific post by ID.
     * @param  postId the ID of the post to delete
     * @return        no content if deletion was successful
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable long postId) {
        log.info("Deleting post with ID: {}", postId);
            log.info("Post deleted with ID: {}", postId);
            return ResponseEntity.noContent().build();


    }
}
