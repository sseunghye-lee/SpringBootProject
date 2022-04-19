package com.example.project.springbootproject.repository;

import com.example.project.springbootproject.domain.Board;
import com.example.project.springbootproject.domain.QBoard;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class BoardRepositoryImpl extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public final QBoard qBoard = QBoard.board;

    public BoardRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(JPAQueryFactory.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

//    @Transactional
//    public void deleteBoard(long boardId) {
//        long delete = jpaQueryFactory.update(qBoard)
//            .where(qBoard.boardId.eq(boardId))
//            .set(qBoard.delete, 1)
//            .execute();
//    }
//
//    @Transactional
//    public void updateBoard(long boardId, String title, String content) {
//        long update = jpaQueryFactory.update(qBoard)
//            .where(qBoard.boardId.eq(boardId))
//            .set(qBoard.title, title)
//            .set(qBoard.content, content)
//            .execute();
//    }

//    public List<Board> boardList() {
//        List<Board> boardList =
//            jpaQueryFactory
//            .selectFrom(qBoard)
//            .where(qBoard.delete.eq(0))
//            .orderBy(qBoard.boardId.desc())
//            .fetch();
//        return boardList;
//    }
//
//    public List<Board> myBoardList(String username) {
//        List<Board> myBoardList =
//            jpaQueryFactory
//                .selectFrom(qBoard)
//                .where(qBoard.username.eq(username))
//                .orderBy(qBoard.boardId.desc())
//                .fetch();
//        return myBoardList;
//    }

    public Page<Board> pagingMyBoardList(String username, Pageable pageable) {
        QueryResults<Board> results = jpaQueryFactory
            .selectFrom(qBoard)
            .where(qBoard.username.eq(username))
            .orderBy(qBoard.boardId.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchResults();

        List<Board> myBoardList = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(myBoardList, pageable, total);
    }

    public Page<Board> pagingBoardList(Pageable pageable) {
        QueryResults<Board> results = jpaQueryFactory
                .selectFrom(qBoard)
                .where(qBoard.delete.eq(0))
                .orderBy(qBoard.boardId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        List<Board> boardList = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(boardList, pageable, total);
    }
}
