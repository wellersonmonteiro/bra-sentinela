package com.projeto.complaintservice.repository;

import com.projeto.complaintservice.entity.ComplaintEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ComplaintRepository extends JpaRepository<ComplaintEntity, UUID> {

    ComplaintEntity findByProtocolNumber(String protocolNumber);

    Long countByStatusComplaint(String open);
}
