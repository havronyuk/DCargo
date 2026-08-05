package dccargo.dcargoservice.repository.dcargo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dccargo.dcargoservice.model.dcargo.FileCustom;

import java.util.List;

@Repository
public interface FileCustomRepository extends JpaRepository<FileCustom, Long> {

    @Query("SELECT f.idFiles FROM FileCustom f WHERE f.idObject = :idObject AND f.type = :type")
    List<Long> getIdFilesByIdObjectAndType(Long idObject, String type);

    FileCustom getByIdFiles(Long idFiles);


}
