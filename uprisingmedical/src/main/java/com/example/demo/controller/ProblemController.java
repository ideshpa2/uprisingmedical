package com.example.demo.controller;

import com.example.demo.model.Problem;
import com.example.demo.repository.ProblemRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/problems")
@CrossOrigin(origins = "*")
public class ProblemController {

    private final ProblemRepository repository;

    public ProblemController(ProblemRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Problem addProblem(@RequestBody Problem problem) {
        return repository.save(problem);
    }

    @GetMapping("/pending")
    public List<Problem> getPendingProblems() {
        return repository.findAll().stream()
                .filter(p -> !p.isApproved())
                .toList();
    }

        @PatchMapping("/{id}/approve")
    public Problem approveProblem(@PathVariable Long id) {
        Problem problem = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem not found"));
        problem.setApproved(true);
        return repository.save(problem);
    }

    // Get *all* problems (for admin use)
    @GetMapping("/all")
    public List<Problem> getAllProblems() {
        return repository.findAll();
    }

    // Get only *approved* problems (for challenges page)
    @GetMapping
    public List<Problem> getApprovedProblems() {
        return repository.findByApprovedTrue();  // Assuming you added an `approved` field
    }


}
