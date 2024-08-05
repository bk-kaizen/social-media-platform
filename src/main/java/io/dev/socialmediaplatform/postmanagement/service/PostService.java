package io.dev.socialmediaplatform.postmanagement.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

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

import io.dev.socialmediaplatform.exception.PostException;
import io.dev.socialmediaplatform.postmanagement.dto.PostDto;
import io.dev.socialmediaplatform.postmanagement.entity.Post;
import io.dev.socialmediaplatform.postmanagement.entity.QPost;
import io.dev.socialmediaplatform.postmanagement.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    @Autowired
    private final PostRepository postRepository;

    /**
     * Retrieve a paginated list of posts based on search and filter parameters.
     * @param  searchOrFilterParameters the parameters to filter or search posts
     * @param  pageable                 the pagination information
     * @return                          a paginated list of PostDto
     */
    public Page<PostDto> retrievePosts(MultiValueMap<String, String> searchOrFilterParameters, Pageable pageable) {
        log.info("Entering getPosts()");
        validateParameters(searchOrFilterParameters.keySet());
        validateSortCriteria(searchOrFilterParameters.getFirst("sort"));
        BooleanBuilder booleanBuilder = buildSearchOrFilterPredicate(searchOrFilterParameters);
        Page<Post> posts = postRepository.findAll(booleanBuilder, pageable);
        Page<PostDto> postDtoList = posts.map(this::convertToPostDto);
        log.info("Leaving getPosts()");
        return postDtoList;
    }

    /**
     * Create a new post.
     * @param  postDto the PostDto containing post information
     * @return         the created PostDto
     */
    @CachePut(value = "post", key = "#result.id")
    public PostDto createPost(PostDto postDto) {
        log.info("Entering createPost()");
        if (postDto.getContent() == null || postDto.getContent().isEmpty()) {
            throw new PostException("Post content cannot be null or empty", HttpStatus.BAD_REQUEST);
        }

        if (postDto.getUserId() == null) {
            throw new PostException("User ID cannot be null", HttpStatus.BAD_REQUEST);
        }

        Post post = new Post();
        post.setContent(postDto.getContent());
        post.setUserId(postDto.getUserId());
        Post savedPost = postRepository.save(post);
        PostDto responsePostDto = convertToPostDto(savedPost);
        log.info("Leaving createPost()");
        return responsePostDto;
    }

    /**
     * Retrieve a post by its ID.
     * @param  postId the ID of the post
     * @return        the PostDto of the retrieved post
     */
    @Cacheable(value = "post", key = "#postId")
    public PostDto retrievePostById(long postId) {
        log.info("Entering getPostById()");
        if (postId <= 0) {
            throw new PostException("Provide valid post id", HttpStatus.BAD_REQUEST);
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post not found.", HttpStatus.NOT_FOUND));
        log.info("Leaving getPostById()");
        return convertToPostDto(post);
    }

    /**
     * Update an existing post.
     * @param  postId  the ID of the post to be updated
     * @param  postDto the PostDto containing updated post information
     * @return         the updated PostDto
     */
    @CachePut(value = "post", key = "#postId")
    public PostDto updatePost(long postId, PostDto postDto) {
        log.info("Entering updatePost()");
        if (postId <= 0) {
            throw new PostException("Provide valid post id", HttpStatus.BAD_REQUEST);
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post not found.", HttpStatus.NOT_FOUND));
        post.setContent(postDto.getContent());
        Post savedPost = postRepository.save(post);
        log.info("Leaving updatePost()");
        return convertToPostDto(savedPost);
    }

    /**
     * Delete a post by its ID.
     * @param postId the ID of the post to be deleted
     */
    @CacheEvict(value = "post", key = "#postId")
    public void deletePost(long postId) {
        log.info("Entering deletePost()");
        if (postId <= 0) {
            throw new PostException("Provide valid post id", HttpStatus.BAD_REQUEST);
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post not found.", HttpStatus.NOT_FOUND));
        postRepository.deleteById(post.getId());
        log.info("Leaving deletePost()");
    }

    private PostDto convertToPostDto(Post post) {
        log.info("Entering convertToPostDto()");
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setContent(post.getContent());
        postDto.setUserId(post.getUserId());
        log.info("Leaving convertToPostDto()");
        return postDto;
    }

    void validateSortCriteria(String sortCriteria) {
        log.info("Entering validateSortCriteria()");
        if (sortCriteria != null && !sortCriteria.isBlank()) {
            String[] sortSplit = sortCriteria.split(",", 2);
            if (sortSplit.length != 2) {
                throw new PostException(
                        String.format("Invalid sort criteria '%s'. Should be something like 'name,ASC' or 'name,asc'",
                                sortCriteria),
                        HttpStatus.BAD_REQUEST);
            }

            String sortBy = sortSplit[0].trim();
            String sortOrder = sortSplit[1].trim().toLowerCase();
            if (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder)) {
                throw new PostException(String.format("Invalid sort-order [%s] for sort-by [%s]", sortOrder, sortBy),
                        HttpStatus.BAD_REQUEST);
            }

        }

        log.info("Leaving validateSortCriteria()");
    }

    void validateParameters(Set<String> actualParameters) {
        log.info("Entering validateParameters()");
        Set<String> validParameters = Set.of("page-no", "page-size", "sort", "id", "userId");
        List<String> invalidParameters =
                actualParameters.stream().filter(param -> !validParameters.contains(param)).toList();
        if (!invalidParameters.isEmpty()) {
            throw new PostException(String.format("Unknown parameter(s) %s found", invalidParameters),
                    HttpStatus.BAD_REQUEST);
        }

        log.info("Leaving validateParameters()");
    }

    BooleanBuilder buildSearchOrFilterPredicate(MultiValueMap<String, String> searchOrFilterParameters) {
        log.info("Entering buildSearchOrFilterPredicate()");
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QPost post = QPost.post;

        String filterById = searchOrFilterParameters.getFirst("id");
        if (filterById != null && !filterById.isBlank()) {
            booleanBuilder.and(post.id.eq(Long.valueOf(filterById)));
        }

        String filterByUserId = searchOrFilterParameters.getFirst("userId");
        if (filterByUserId != null && !filterByUserId.isBlank()) {
            booleanBuilder.and(post.userId.eq(UUID.fromString(filterByUserId)));
        }

        log.info("Leaving buildSearchOrFilterPredicate()");
        return booleanBuilder;
    }
}
