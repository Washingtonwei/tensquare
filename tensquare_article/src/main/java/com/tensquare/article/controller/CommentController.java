package com.tensquare.article.controller;

import com.tensquare.article.pojo.Comment;
import com.tensquare.article.service.CommentService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Comment comment){
        commentService.save(comment);
        return new Result(true, StatusCode.OK, "Save Success");
    }

    @RequestMapping(value = "/{articleid}/{page}/{size}", method = RequestMethod.GET)
    public Result findByParentid(@PathVariable String articleid, @PathVariable int page, @PathVariable int size){
        Page<Comment> pageData = commentService.findByArticleid(articleid, page, size);
        return new Result(true, StatusCode.OK, "Query Successful", new PageResult<Comment>(pageData.getTotalElements(), pageData.getContent()));
    }

    @RequestMapping(value = "/{commentid}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String commentid){
        commentService.deleteById(commentid);
        return new Result(true, StatusCode.OK, "Delete Successful");
    }
}
