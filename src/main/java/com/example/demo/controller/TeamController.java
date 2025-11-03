// src/main/java/com/example/demo/controller/TeamController.java
package com.example.demo.controller;

import com.example.demo.model.Team;
import com.example.demo.model.TeamJoinRequest;
import com.example.demo.repository.TeamRepository;
import com.example.demo.repository.TeamJoinRequestRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

// src/main/java/com/example/demo/controller/TeamController.java
@RestController
@RequestMapping("/teams")
@CrossOrigin(origins = "*")
public class TeamController {

    private final TeamRepository teamRepo;
    private final TeamJoinRequestRepository requestRepo;

    public TeamController(TeamRepository teamRepo, TeamJoinRequestRepository requestRepo) {
        this.teamRepo = teamRepo;
        this.requestRepo = requestRepo;
    }

    // Create a new team
    @PostMapping
    public Team createTeam(@RequestBody Team team) {
        return teamRepo.save(team);
    }

    // Get all teams
    @GetMapping
    public List<Team> getTeams() {
        return teamRepo.findAll();
    }

    // Submit a join request
    @PostMapping("/{teamId}/request")
    public TeamJoinRequest requestJoin(@PathVariable Long teamId, @RequestBody JoinRequestDTO dto) {
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        TeamJoinRequest req = new TeamJoinRequest();
        req.setUsername(dto.getUsername());
        req.setTeam(team);
        req.setApproved(false);
        return requestRepo.save(req);
    }

    // Admin: view pending join requests
    @GetMapping("/requests/pending")
    public List<TeamJoinRequest> getPendingRequests() {
        return requestRepo.findByApprovedFalse();
    }

    // Admin: approve a request
    @PatchMapping("/requests/{requestId}/approve")
    public TeamJoinRequest approveRequest(@PathVariable Long requestId) {
        TeamJoinRequest req = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        req.setApproved(true);
        return requestRepo.save(req);
    }

    // Admin: deny (delete) a request
    @PatchMapping("/requests/{requestId}/deny")
    public void denyRequest(@PathVariable Long requestId) {
        TeamJoinRequest req = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        requestRepo.delete(req);
    }

    // All members (or “current members” depending on your semantics)
    @GetMapping("/{teamId}/members")
    public List<String> getTeamMembers(@PathVariable Long teamId) {
        return requestRepo.findByTeamIdAndApprovedTrue(teamId)
                .stream().map(TeamJoinRequest::getUsername).toList();
    }

    // Approved members (distinct path to avoid ambiguity)
    @GetMapping("/{teamId}/members/approved")
    public List<String> getApprovedMembers(@PathVariable Long teamId) {
        return requestRepo.findByTeamIdAndApprovedTrue(teamId)
                .stream().map(TeamJoinRequest::getUsername).toList();
    }
}
