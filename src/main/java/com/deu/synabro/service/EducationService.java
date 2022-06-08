package com.deu.synabro.service;

import com.deu.synabro.entity.Board;
import com.deu.synabro.entity.enums.BoardType;
import com.deu.synabro.http.response.BoardSimpleResponse;
import com.deu.synabro.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EducationService {
    BoardRepository boardRepository;

    @Autowired
    public EducationService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public List<BoardSimpleResponse> getBoards() {
        List<Board> boards = boardRepository.findBoardByBoardType(BoardType.education);
        return boards.stream().map(BoardSimpleResponse::new).collect(Collectors.toList());
    }
}
