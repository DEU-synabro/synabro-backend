package com.deu.synabro.repository;

import com.deu.synabro.entity.Docs;
import com.deu.synabro.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocsRepository extends JpaRepository<Docs, UUID> {
    List<Docs> findByBoard_Idx(UUID uuid);
    List<Docs> findByWork_Idx(UUID uuid);
    List<Docs> findByOffVolunteer_Idx(UUID uuid);
    List<Docs> findByCertification_Idx(UUID uuid);
}
