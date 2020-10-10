package com.tensquare.article.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tensquare.article.pojo.Channel;
import com.tensquare.article.service.ChannelService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;

@RestController
@CrossOrigin
@RequestMapping("/channel")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "Query Success", channelService.findAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "Query Success", channelService.findById(id));
    }

    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<Channel> pageList = channelService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "Query Success", new PageResult<Channel>(pageList.getTotalElements(), pageList.getContent()));
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "Query Success", channelService.findSearch(searchMap));
    }

    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Channel channel) {
        channelService.add(channel);
        return new Result(true, StatusCode.OK, "Create Success");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Channel channel, @PathVariable String id) {
        channel.setId(id);
        channelService.update(channel);
        return new Result(true, StatusCode.OK, "Update Success");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        channelService.deleteById(id);
        return new Result(true, StatusCode.OK, "Delete Success");
    }
}
