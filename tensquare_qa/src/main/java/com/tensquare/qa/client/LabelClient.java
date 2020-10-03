package com.tensquare.qa.client;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//If LabelClient has any problem, Hystrix will use LabelClientImpl to throw exceptions
@FeignClient(value = "tensquare-base", fallback = LabelClientImpl.class)
public interface LabelClient {
    @RequestMapping(value = "/label/{labelId}", method = RequestMethod.GET)
    public Result findById(@PathVariable String labelId);
}
