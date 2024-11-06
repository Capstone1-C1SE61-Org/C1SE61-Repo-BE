package com.example.systemp3l.repository;

import com.example.systemp3l.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IPostRepository extends JpaRepository<Post, Integer> {
}
