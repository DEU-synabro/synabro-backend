package com.deu.synabro.docs.domain.repository;

import com.deu.synabro.docs.domain.entity.DocsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocsRepository extends JpaRepository<DocsEntity,Long> {
}
