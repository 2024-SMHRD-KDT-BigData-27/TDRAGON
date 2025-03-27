package com.smart.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.smart.entity.Chunks;
import com.smart.repository.ChunksRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChunksService {

    private final ChunksRepository chunksRepository;

    public List<Chunks> getChunksByIdsAndUser(List<Long> chunkIdxList, Long userIdx) {
        return chunksRepository.findByChunkIdxInAndUserIdx(chunkIdxList, userIdx);
    }
}