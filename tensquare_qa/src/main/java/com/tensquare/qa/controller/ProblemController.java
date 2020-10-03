package com.tensquare.qa.controller;
import com.tensquare.qa.client.LabelClient;
import com.tensquare.qa.client.LabelClientImpl;
import com.tensquare.qa.pojo.Problem;
import com.tensquare.qa.service.ProblemService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/problem")
public class ProblemController {

	@Autowired
	private ProblemService problemService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private LabelClient labelClient;

	@RequestMapping(value = "/label/{labelId}", method = RequestMethod.GET)
	public Result findByLabelId(@PathVariable String labelId){
		Result result = labelClient.findById(labelId);
		return result;
	}

	@RequestMapping(value = "/newlist/{labelid}/{page}/{size}", method = RequestMethod.GET)
	public Result newlist(@PathVariable String labelid, @PathVariable int page, @PathVariable int size){
		Page<Problem> pageData = problemService.newlist(labelid, page, size);
		return new Result(true, StatusCode.OK, "Query Successful", new PageResult<Problem>(pageData.getTotalElements(), pageData.getContent()));
	}

	@RequestMapping(value = "/hotlist/{labelid}/{page}/{size}", method = RequestMethod.GET)
	public Result hotlist(@PathVariable String labelid, @PathVariable int page, @PathVariable int size){
		Page<Problem> pageData = problemService.hotlist(labelid, page, size);
		return new Result(true, StatusCode.OK, "Query Successful", new PageResult<Problem>(pageData.getTotalElements(), pageData.getContent()));
	}

	@RequestMapping(value = "/waitlist/{labelid}/{page}/{size}", method = RequestMethod.GET)
	public Result waitlist(@PathVariable String labelid, @PathVariable int page, @PathVariable int size){
		Page<Problem> pageData = problemService.waitlist(labelid, page, size);
		return new Result(true, StatusCode.OK, "Query Successful", new PageResult<Problem>(pageData.getTotalElements(), pageData.getContent()));
	}

	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"Query Successful",problemService.findAll());
	}

	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"Query Successful",problemService.findById(id));
	}

	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<Problem> pageList = problemService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"Query Successful",  new PageResult<Problem>(pageList.getTotalElements(), pageList.getContent()) );
	}

    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"Query Successful",problemService.findSearch(searchMap));
    }

	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Problem problem  ){
		String token = (String) request.getAttribute("user_claims");
		if(token == null || "".equals(token))
			return new Result(false, StatusCode.ACCESSERROR, "Authorization failed!");
		problemService.add(problem);
		return new Result(true,StatusCode.OK,"Save Successful");
	}

	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Problem problem, @PathVariable String id ){
		problem.setId(id);
		problemService.update(problem);		
		return new Result(true,StatusCode.OK,"Update Successful");
	}

	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		problemService.deleteById(id);
		return new Result(true,StatusCode.OK,"Delete Successful");
	}
	
}
