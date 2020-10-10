package com.tensquare.whine.controller;

import com.tensquare.whine.pojo.Whine;
import com.tensquare.whine.service.WhineService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/whine")
public class WhineController {

    @Autowired
    private WhineService whineService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "Query Successful", whineService.findAll());
    }

    @RequestMapping(value = "/{whineId}", method = RequestMethod.GET)
    public Result findById(@PathVariable String whineId) {
        return new Result(true, StatusCode.OK, "Query Successful", whineService.findById(whineId));
    }

    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Whine whine) {
        whineService.save(whine);
        return new Result(true, StatusCode.OK, "Save Successful");
    }

    @RequestMapping(value = "/{whineId}", method = RequestMethod.PUT)
    public Result update(@PathVariable String whineId, @RequestBody Whine whine) {
        whine.set_id(whineId);
        whineService.update(whine);
        return new Result(true, StatusCode.OK, "Update Successful");
    }

    @RequestMapping(value = "/{whineId}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String whineId) {
        whineService.deleteById(whineId);
        return new Result(true, StatusCode.OK, "Delete Successful");
    }

    @RequestMapping(value = "/comment/{parentid}/{page}/{size}", method = RequestMethod.GET)
    public Result findByParentid(@PathVariable String parentid, @PathVariable int page, @PathVariable int size) {
        Page<Whine> pageData = whineService.findByParentid(parentid, page, size);
        return new Result(true, StatusCode.OK, "Query Successful", new PageResult<Whine>(pageData.getTotalElements(), pageData.getContent()));
    }

    @RequestMapping(value = "/thumbup/{whineId}", method = RequestMethod.PUT)
    public Result updateThumbup(@PathVariable String whineId) {
        //Based on the business rule, one user can ONLY like a whine once
        //Since we haven't implemented authentication yet, here we hardcode a userid 111
        String userid = "111";
        //Check if the current user has thumb-up'ed this whine already
        if (redisTemplate.opsForValue().get("thumbup_" + userid + whineId) != null) {
            return new Result(false, StatusCode.REPERROR, "Cannot thumb up again!");
        }
        whineService.updateThumbup(whineId);
        redisTemplate.opsForValue().set("thumbup_" + userid + whineId, 1);
        return new Result(true, StatusCode.OK, "Thumb up successful");
    }
}
