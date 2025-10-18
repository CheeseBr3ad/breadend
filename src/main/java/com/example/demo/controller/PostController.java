package com.example.demo.controller;

import com.example.demo.entity.Post;
import com.example.demo.repository.PostRepository;
import com.example.demo.security.JwtUtils;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping("/nearby")
    public List<Map<String, Object>> getNearbyPosts(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam double distance // meters
    ) {
        return postRepository.findPostsNearby(lat, lon, distance);
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

   /* @PostMapping
    //@PreAuthorize("hasRole('USER')")
    public Post createPost(@RequestBody Post post, Authentication authentication) {
        // Set the user ID from JWT token
        String userId = JwtUtils.getUserId(authentication).orElse("anonymous");
        post.setUserId(userId);
        return postRepository.save(post);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post postDetails, Authentication authentication) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Post post = optionalPost.get();
        String userId = JwtUtils.getUserId(authentication).orElse("anonymous");
        
        // Check if user owns the post or is admin
        if (!post.getUserId().equals(userId) && !JwtUtils.isAdmin(authentication)) {
            return ResponseEntity.status(403).build();
        }

        post.setTitle(postDetails.getTitle());
        post.setContent(postDetails.getContent());
        post.setLatitude(postDetails.getLatitude());
        post.setLongitude(postDetails.getLongitude());

        Post updatedPost = postRepository.save(post);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, Authentication authentication) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Post post = optionalPost.get();
        String userId = JwtUtils.getUserId(authentication).orElse("anonymous");
        
        // Check if user owns the post or is admin
        if (!post.getUserId().equals(userId) && !JwtUtils.isAdmin(authentication)) {
            return ResponseEntity.status(403).build();
        }

        postRepository.delete(post);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-posts")
    @PreAuthorize("hasRole('USER')")
    public List<Post> getMyPosts(Authentication authentication) {
        String userId = JwtUtils.getUserId(authentication).orElse("anonymous");
        return postRepository.findByUserId(userId);
    }*/
}
