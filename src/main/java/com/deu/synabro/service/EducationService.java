package com.deu.synabro.service;

import com.deu.synabro.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EducationService {
    BoardRepository boardRepository;

    @Autowired
    public EducationService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }
}
