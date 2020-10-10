package com.tensquare.qa.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tensquare.qa.pojo.Reply;
import com.tensquare.qa.service.ReplyService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;

@RestController
@CrossOrigin
@RequestMapping("/reply")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "Query Successful", replyService.findAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "Query Successful", replyService.findById(id));
    }

    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<Reply> pageList = replyService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "Query Successful", new PageResult<Reply>(pageList.getTotalElements(), pageList.getContent()));
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "Query Successful", replyService.findSearch(searchMap));
    }

    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Reply reply) {
        replyService.add(reply);
        return new Result(true, StatusCode.OK, "Create Successful");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Reply reply, @PathVariable String id) {
        reply.setId(id);
        replyService.update(reply);
        return new Result(true, StatusCode.OK, "Update Successful");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        replyService.deleteById(id);
        return new Result(true, StatusCode.OK, "Delete Successful");
    }
}
