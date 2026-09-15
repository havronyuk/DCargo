package dccargo.dcargoservice.repository.dcargo;

import dccargo.dcargoservice.model.dcargo.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByTransactionIdOrderBySeqAsc(String transactionId);

    List<AuditLog> findByEntityTypeAndEntityIdOrderByCreatedAtAsc(String entityType, Long entityId);

    List<AuditLog> findByParentEntityTypeAndParentEntityIdOrderByCreatedAtAsc(String parentEntityType, Long parentEntityId);

    List<AuditLog> findByActorIdOrderByCreatedAtDesc(Long actorId);
}