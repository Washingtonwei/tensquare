package com.tensquare.recruit.controller;
import com.tensquare.recruit.pojo.Enterprise;
import com.tensquare.recruit.service.EnterpriseService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/enterprise")
public class EnterpriseController {

	@Autowired
	private EnterpriseService enterpriseService;

	@RequestMapping(value = "/search/hotlist", method = RequestMethod.GET)
	public Result hotlist(){
		List<Enterprise> list = enterpriseService.hotList("1");
		return new Result(true, StatusCode.OK, "Query Successful", list);
	}

	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"Query Successful",enterpriseService.findAll());
	}
	

	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"Query Successful",enterpriseService.findById(id));
	}


	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<Enterprise> pageList = enterpriseService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"Query Successful",  new PageResult<Enterprise>(pageList.getTotalElements(), pageList.getContent()) );
	}


    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"Query Successful",enterpriseService.findSearch(searchMap));
    }
	

	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Enterprise enterprise  ){
		enterpriseService.add(enterprise);
		return new Result(true,StatusCode.OK,"Save Successful");
	}
	

	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Enterprise enterprise, @PathVariable String id ){
		enterprise.setId(id);
		enterpriseService.update(enterprise);		
		return new Result(true,StatusCode.OK,"Update Successful");
	}
	

	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		enterpriseService.deleteById(id);
		return new Result(true,StatusCode.OK,"Delete Successful");
	}
	
}
