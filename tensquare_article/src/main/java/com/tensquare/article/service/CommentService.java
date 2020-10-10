package com.tensquare.article.service;

import com.tensquare.article.dao.CommentDao;
import com.tensquare.article.pojo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import util.IdWorker;

@Service
public class CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private IdWorker idWorker;

    public void save(Comment comment) {
        comment.set_id(idWorker.nextId() + "");
        commentDao.save(comment);
    }

    public void deleteById(String commentId) {
        commentDao.deleteById(commentId);
    }

    public Page<Comment> findByArticleid(String parentid, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return commentDao.findByArticleId(parentid, pageable);
    }
}
