package com.example.demo.controller;

import com.example.demo.model.Team;
import com.example.demo.model.TeamJoinRequest;
import com.example.demo.repository.TeamRepository;
import com.example.demo.repository.TeamJoinRequestRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // ✅ Create a new team
    @PostMapping
    public Team createTeam(@RequestBody Team team) {
        return teamRepo.save(team);
    }

    // ✅ Get all teams
    @GetMapping
    public List<Team> getTeams() {
        return teamRepo.findAll();
    }

    // ✅ Submit a join request for a team (provide teamId in path and username in body)
    @PostMapping("/{teamId}/request")
    public TeamJoinRequest requestJoin(@PathVariable Long teamId, @RequestBody JoinRequestDTO dto) {
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        TeamJoinRequest request = new TeamJoinRequest();
        request.setUsername(dto.getUsername());
        request.setTeam(team);
        request.setApproved(false);
        return requestRepo.save(request);
    }

    // ✅ Admin: View all pending join requests
    @GetMapping("/pending")
    public List<TeamJoinRequest> getPendingRequests() {
        return requestRepo.findByApprovedFalse();
    }

    // ✅ Admin: Approve a pending request
    @PatchMapping("/{requestId}/approve")
    public TeamJoinRequest approveRequest(@PathVariable Long requestId) {
        TeamJoinRequest req = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        req.setApproved(true);
        return requestRepo.save(req);
    }
}
