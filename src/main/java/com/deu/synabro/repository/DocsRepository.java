package com.deu.synabro.repository;

import com.deu.synabro.entity.Docs;
import com.deu.synabro.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DocsRepository extends JpaRepository<Docs,Long> {
    Docs findByWorkId_Idx(UUID uuid);
}
