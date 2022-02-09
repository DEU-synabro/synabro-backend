package com.deu.synabro.repository;

import com.deu.synabro.entity.Docs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocsRepository extends JpaRepository<Docs,Long> {
}
