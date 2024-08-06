package io.dev.socialmediaplatform.postmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.dev.socialmediaplatform.postmanagement.dto.PostDto;
import io.dev.socialmediaplatform.postmanagement.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Post Management", description = "APIs for managing posts")
public class PostController {

    @Autowired
    private final PostService postService;

    /**
     * Create a new post.
     * @param  postDto the post to be created
     * @return         the created post
     */

    @PostMapping
    @Operation(summary = "Create a new post", description = "Creates a new post in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Post created successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(example = """
                        {
                            "id": 1,
                            "content": "This is a new post",
                            "userId": "5e40bc30-82e4-43dd-a50c-5eef0e9fe9d4"
                        }
                        """))),
        @ApiResponse(responseCode = "400", description = "Invalid post details",
                content = @Content(mediaType = "application/json", schema = @Schema(example = """
                        {
                            "statusCode": 400,
                            "message": "BAD_REQUEST",
                            "details": [
                                "Invalid post details provided"
                            ]
                        }
                        """))) })
    public PostDto createPost(@RequestBody PostDto postDto) {
        log.info("Creating a new post with content: {}", postDto.getContent());
        PostDto savedPost = postService.createPost(postDto);
        log.info("Post created with ID: {}", savedPost.getId());
        return savedPost;
    }

    /**
     * Retrieve a specific post by ID.
     * @return the retrieved post
     */
    @Operation(summary = "Retrieve all posts", description = "Retrieves all posts with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Posts retrieved successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(example = """
                        {
                            "content": [
                                {
                                    "id": 1,
                                    "content": "This is a post",
                                    "userId": "5e40bc30-82e4-43dd-a50c-5eef0e9fe9d4"
                                }
                            ],
                            "pageable": {
                                "sort": {
                                    "sorted": true,
                                    "unsorted": false,
                                    "empty": false
                                },
                                "pageNumber": 0,
                                "pageSize": 20,
                                "offset": 0,
                                "paged": true,
                                "unpaged": false
                            },
                            "last": true,
                            "totalPages": 1,
                            "totalElements": 1,
                            "size": 20,
                            "number": 0,
                            "sort": {
                                "sorted": true,
                                "unsorted": false,
                                "empty": false
                            },
                            "first": true,
                            "numberOfElements": 1,
                            "empty": false
                        }
                        """))),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters",
                content = @Content(mediaType = "application/json", schema = @Schema(example = """
                        {
                            "statusCode": 400,
                            "message": "BAD_REQUEST",
                            "details": [
                                "Invalid request parameters"
                            ]
                        }
                        """))) })
    @GetMapping()
    public Page<PostDto> retrievePosts(
            @Parameter(hidden = true, in = ParameterIn.QUERY, style = ParameterStyle.FORM)
            @SortDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) MultiValueMap<String, String> searchParameters) {
        log.info("Retrieving all posts with pagination - page: {}, size: {}", pageable.getPageNumber(),
                pageable.getPageSize());
        Page<PostDto> posts = postService.retrievePosts(searchParameters, pageable);
        log.info("Retrieved {} posts", posts.getTotalElements());
        return posts;
    }

    /**
     * Retrieve a specific post by ID.
     * @param  postId the ID of the post to retrieve
     * @return        the retrieved post
     */
    @Operation(summary = "Retrieve a specific post by ID", description = "Retrieves a specific post by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Post retrieved successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(example = """
                        {
                            "id": 1,
                            "content": "This is a post",
                            "userId": "5e40bc30-82e4-43dd-a50c-5eef0e9fe9d4"
                        }
                        """))),
        @ApiResponse(responseCode = "404", description = "Post not found",
                content = @Content(mediaType = "application/json", schema = @Schema(example = """
                        {
                            "statusCode": 404,
                            "message": "NOT_FOUND",
                            "details": [
                                "Post not found"
                            ]
                        }
                        """))), })
    @GetMapping("/{postId}")
    public PostDto retrievePostById(@PathVariable long postId) {
        log.info("Retrieving post with ID: {}", postId);
        PostDto postDto = postService.retrievePostById(postId);
        log.info("Retrieved {} post", postDto);
        return postDto;
    }

    /**
     * Update a specific post by ID.
     * @param  postId  the ID of the post to update
     * @param  postDto the updated post details
     * @return         the updated post
     */
    @PutMapping("/{postId}")
    @Operation(summary = "Update a specific post by ID", description = "Updates a specific post by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Post updated successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(example = """
                        {
                            "id": 1,
                            "content": "Updated post content",
                            "userId": "5e40bc30-82e4-43dd-a50c-5eef0e9fe9d4"
                        }
                        """))),
        @ApiResponse(responseCode = "400", description = "Invalid post details",
                content = @Content(mediaType = "application/json", schema = @Schema(example = """
                        {
                            "statusCode": 400,
                            "message": "BAD_REQUEST",
                            "details": [
                                "Invalid post details provided"
                            ]
                        }
                        """))),
        @ApiResponse(responseCode = "404", description = "Post not found",
                content = @Content(mediaType = "application/json", schema = @Schema(example = """
                        {
                            "statusCode": 404,
                            "message": "NOT_FOUND",
                            "details": [
                                "Post not found"
                            ]
                        }
                        """))) })
    public PostDto updatePost(@PathVariable long postId, @RequestBody PostDto postDto) {
        log.info("Updating post with ID: {}", postId);
        PostDto updatePost = postService.updatePost(postId, postDto);
        log.info("Leaving {} Update Post", updatePost);
        return updatePost;
    }

    /**
     * Delete a specific post by ID.
     * @param  postId the ID of the post to delete
     * @return        no content if deletion was successful
     */
    @DeleteMapping("/{postId}")
    @Operation(summary = "Delete a specific post by ID", description = "Deletes a specific post by its ID")
    @ApiResponses(
            value = { @ApiResponse(responseCode = "204", description = "Post deleted successfully", content = @Content),
                @ApiResponse(responseCode = "400", description = "Invalid post ID",
                        content = @Content(mediaType = "application/json", schema = @Schema(example = """
                                {
                                    "statusCode": 400,
                                    "message": "BAD_REQUEST",
                                    "details": [
                                        "Invalid post ID provided"
                                    ]
                                }
                                """))),
                @ApiResponse(responseCode = "404", description = "Post not found",
                        content = @Content(mediaType = "application/json", schema = @Schema(example = """
                                {
                                    "statusCode": 404,
                                    "message": "NOT_FOUND",
                                    "details": [
                                        "Post not found"
                                    ]
                                }
                                """))) })
    public void deletePost(@PathVariable long postId) {
        log.info("Deleting post with ID: {}", postId);
        postService.deletePost(postId);
        log.info("Post deleted with ID: {}", postId);

    }
}
