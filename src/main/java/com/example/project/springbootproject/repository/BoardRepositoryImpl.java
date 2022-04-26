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

    public Page<Board> pagingMyBoardList(String username, Pageable pageable) {
        List<Board> myBoardList = jpaQueryFactory
            .selectFrom(qBoard)
            .where(qBoard.username.eq(username))
            .orderBy(qBoard.boardId.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = jpaQueryFactory.select(qBoard.count())
            .from(qBoard)
            .where(qBoard.username.eq(username))
            .orderBy(qBoard.boardId.desc())
            .fetchOne();

        return new PageImpl<>(myBoardList, pageable, total);
    }

    public Page<Board> pagingBoardList(Pageable pageable) {
        List<Board> boardList = jpaQueryFactory.selectFrom(qBoard)
                .where(qBoard.delete.eq(0))
                .orderBy(qBoard.boardId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory.select(qBoard.count())
                .from(qBoard)
                .where(qBoard.delete.eq(0))
                .orderBy(qBoard.boardId.desc())
                .fetchOne();

        return new PageImpl<>(boardList, pageable, total);
    }
}
