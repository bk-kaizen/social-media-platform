package io.dev.socialmediaplatform.postmanagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.querydsl.core.BooleanBuilder;

import io.dev.socialmediaplatform.exception.PostException;
import io.dev.socialmediaplatform.postmanagement.dto.PostDto;
import io.dev.socialmediaplatform.postmanagement.entity.Post;
import io.dev.socialmediaplatform.postmanagement.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Captor
    private ArgumentCaptor<Post> postCaptor;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("Given Valid Parameters Provide Success Response")
    void testRetrievePosts() {
        Pageable pageable = PageRequest.of(0, 10);
        MultiValueMap<String, String> searchParams = new LinkedMultiValueMap<>();
        searchParams.add("sort", "content,asc");

        Post post = new Post();
        post.setId(1L);
        post.setContent("Test Content");
        post.setUserId(UUID.randomUUID());

        Page<Post> postPage = new PageImpl<>(List.of(post), pageable, 1);
        when(postRepository.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(postPage);

        Page<PostDto> result = postService.retrievePosts(searchParams, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Test Content", result.getContent().get(0).getContent());
    }

    @Test
    @DisplayName("Given Invalid Id Throws Exception")
    void testDeletePostNotFound() {
        long postId = 0;
        assertThrows(PostException.class, () -> postService.deletePost(postId));
    }

    @Test
    @DisplayName("Given Invalid Sort Details Throws Exception")
    void testValidateSortCriteriaInvalid() {
        String invalidSortCriteria = "invalidSortCriteria";

        assertThrows(PostException.class, () -> postService.validateSortCriteria(invalidSortCriteria));
    }

    @Test
    @DisplayName("Given Invalid Filter Parameter Throws Exception")
    void testValidateParametersInvalid() {
        Set<String> invalidParameters = Set.of("invalidParameter");

        assertThrows(PostException.class, () -> postService.validateParameters(invalidParameters));
    }

    @Test
    @DisplayName("Given Valid Post Dto data Provide Success Response")
    void testCreatePost() {
        PostDto postDto = new PostDto();
        postDto.setContent("Test Content");
        postDto.setUserId(UUID.randomUUID());

        Post post = new Post();
        post.setContent(postDto.getContent());
        post.setUserId(postDto.getUserId());

        when(postRepository.save(any(Post.class))).thenReturn(post);

        PostDto result = postService.createPost(postDto);

        assertEquals("Test Content", result.getContent());
        verify(postRepository).save(postCaptor.capture());
        assertEquals("Test Content", postCaptor.getValue().getContent());
    }

    @Test
    @DisplayName("Given Null Content Throws Exception")
    void testCreatePostWithNullContent() {
        PostDto postDto = new PostDto();
        postDto.setContent(null);
        postDto.setUserId(UUID.randomUUID());

        Exception exception = assertThrows(PostException.class, () -> {
            postService.createPost(postDto);
        });

        assertEquals("Post content cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Given Empty Content Throws Exception")

    void testCreatePostWithEmptyContent() {
        PostDto postDto = new PostDto();
        postDto.setContent("");
        postDto.setUserId(UUID.randomUUID());

        Exception exception = assertThrows(PostException.class, () -> {
            postService.createPost(postDto);
        });

        assertEquals("Post content cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Given Null User Id Throws Exception")
    void testCreatePostWithNullUserId() {
        PostDto postDto = new PostDto();
        postDto.setContent("Test Content");
        postDto.setUserId(null);

        Exception exception = assertThrows(PostException.class, () -> {
            postService.createPost(postDto);
        });

        assertEquals("User ID cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Given Valid Id Provide Success Response")
    void testRetrievePostById() {
        long postId = 1L;
        Post post = new Post();
        post.setId(postId);
        post.setContent("Test Content");
        post.setUserId(UUID.randomUUID());

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        PostDto result = postService.retrievePostById(postId);

        assertEquals("Test Content", result.getContent());
    }

    @Test
    @DisplayName("Given Invalid Id  Throws Exception")
    void testRetrievePostByIdNotFound() {
        long postId = 0;
        assertThrows(PostException.class, () -> postService.retrievePostById(postId));
    }

    @Test
    @DisplayName("Given Valid Details Provides Success Response")
    void testUpdatePost() {
        long postId = 1L;
        PostDto postDto = new PostDto();
        postDto.setContent("Updated Content");

        Post post = new Post();
        post.setId(postId);
        post.setContent("Test Content");
        post.setUserId(UUID.randomUUID());

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        PostDto result = postService.updatePost(postId, postDto);

        assertEquals("Updated Content", result.getContent());
        verify(postRepository).save(postCaptor.capture());
        assertEquals("Updated Content", postCaptor.getValue().getContent());
    }

    @Test
    @DisplayName("Provide Invalid Update Details Then Throws Exception")
    void testUpdatePostNotFound() {
        long postId = 0;
        PostDto postDto = new PostDto();
        postDto.setContent("Updated Content");
        assertThrows(PostException.class, () -> postService.updatePost(postId, postDto));
    }

    @Test
    @DisplayName("Provide Valid Id Provides Success Response")
    void testDeletePost() {
        long postId = 1L;
        Post post = new Post();
        post.setId(postId);
        post.setContent("Test Content");
        post.setUserId(UUID.randomUUID());

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        doNothing().when(postRepository).deleteById(postId);

        postService.deletePost(postId);

        verify(postRepository).deleteById(postId);
    }

}
