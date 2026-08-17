package dccargo.dcargoservice.repository.dcargo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dccargo.dcargoservice.enums.TechnicalInspectionStatus;
import dccargo.dcargoservice.model.dcargo.UserDocument;

@Repository
public interface UserDocumentRepository extends JpaRepository<UserDocument, Long> {

	boolean existsByDocumentNumber(String documentNumber);

	List<UserDocument> findAllByUserIdOrderByInspectionDateDesc(Long userId);

	List<UserDocument> findAllByUserIdAndStatusOrderByValidUntilDesc(
			Long userId,
			TechnicalInspectionStatus status
	);

}
