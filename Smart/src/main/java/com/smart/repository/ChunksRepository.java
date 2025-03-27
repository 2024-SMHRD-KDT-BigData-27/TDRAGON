package com.smart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.smart.entity.Chunks;

@Repository
public interface ChunksRepository extends JpaRepository<Chunks, Long> {

    @Query(value = "SELECT * FROM chunks2 WHERE chunk_idx IN (:chunkIdxList) AND user_idx = :userIdx", nativeQuery = true)
    List<Chunks> findByChunkIdxInAndUserIdx(List<Long> chunkIdxList, Long userIdx);
    
//    List<Chunks> findByChunkIdxInAndUserIdx(List<Long> chunkIdxList, Long userIdx);
}