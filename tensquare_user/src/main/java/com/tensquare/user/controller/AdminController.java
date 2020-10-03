package com.tensquare.user.controller;

import com.tensquare.user.pojo.Admin;
import com.tensquare.user.service.AdminService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private JwtUtil jwtUtil;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Result login(@RequestBody Admin admin){
		admin = adminService.login(admin);
		if(admin == null){
			return new Result(false, StatusCode.LOGINERROR, "Login Failed");
		}
		//Use JWT here
		//We didn't have a role field in User class or Admin class
		//After logging in, the function assigns role to the current user
		String token = jwtUtil.createJWT(admin.getId(), admin.getLoginname(), "admin");// we should get this from DB, here is not good practice
		Map<String, Object> map = new HashMap<>();
		map.put("token", token);
		map.put("roles", "admin");
		map.put("name", admin.getLoginname());
		return new Result(true, StatusCode.OK, "Admin Login Successful", map);
	}

	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"Query Successful",adminService.findAll());
	}

	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"Query Successful",adminService.findById(id));
	}

	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<Admin> pageList = adminService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"Query Successful",  new PageResult<Admin>(pageList.getTotalElements(), pageList.getContent()) );
	}

    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"Query Successful",adminService.findSearch(searchMap));
    }

	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Admin admin){
		adminService.add(admin);
		return new Result(true,StatusCode.OK,"Create Successful");
	}

	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Admin admin, @PathVariable String id ){
		admin.setId(id);
		adminService.update(admin);		
		return new Result(true,StatusCode.OK,"Update Successful");
	}

	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		adminService.deleteById(id);
		return new Result(true,StatusCode.OK,"Delete Successful");
	}
}