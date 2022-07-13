package com.deu.synabro.repository;

import com.deu.synabro.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VideoRepository extends JpaRepository<Video, UUID> {
    Video findByWork_Idx(UUID uuid);
}
