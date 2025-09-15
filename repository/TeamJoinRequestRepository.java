// src/main/java/com/example/demo/repository/TeamJoinRequestRepository.java
package com.example.demo.repository;

import com.example.demo.model.TeamJoinRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TeamJoinRequestRepository extends JpaRepository<TeamJoinRequest, Long> {
    List<TeamJoinRequest> findByApprovedFalse();
    List<TeamJoinRequest> findByTeamIdAndApprovedTrue(Long teamId); // 👈 add this
}




