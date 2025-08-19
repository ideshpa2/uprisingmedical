package com.example.demo.repository;

import com.example.demo.model.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    List<Problem> findByApprovedTrue();   // For Challenges page
    List<Problem> findByApprovedFalse();  // For Admin pending list
}
