package com.example.systemp3l.service.impl;

import com.example.systemp3l.Exception.ResourceNotFoundException;
import com.example.systemp3l.model.Post;
import com.example.systemp3l.repository.IPostRepository;
import com.example.systemp3l.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl {

    @Autowired
    private IPostRepository postRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Integer id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Post updatePost(Integer id, Post postDetails) {
        Post post = getPostById(id);
        post.setPostTitle(postDetails.getPostTitle());
        post.setContent(postDetails.getContent());
        return postRepository.save(post);
    }

    public void deletePost(int id) {
        Post post = getPostById(id);
        postRepository.delete(post);
    }
}
