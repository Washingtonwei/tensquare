package com.tensquare.user.controller;
import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private JwtUtil jwtUtil;

	/**
	 * This update method is not invoked by frontend, but by tensquare_friend module!
	 * update userid's followcount and friendid's fanscount
	 * x is a number that will be added to fanscount and friendid, normally it is 1 or -1
	 */
	@RequestMapping(value = "/{userid}/{friendid}/{x}", method = RequestMethod.PUT)
	public void updateFanscountAndFollowcount(@PathVariable String userid, @PathVariable String friendid, @PathVariable int x){
		userService.updateFanscountAndFollowcount(x, userid, friendid);
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Result login(@RequestBody User user){
		user = userService.login(user.getMobile(), user.getPassword());
		if(user == null){
			return new Result(false, StatusCode.LOGINERROR, "Login failed");
		}
		String token = jwtUtil.createJWT(user.getId(), user.getMobile(), "user");
		Map<String, Object> map = new HashMap<>();
		map.put("token", token);
		map.put("roles", "user");
		map.put("name", user.getNickname());
		map.put("avatar", user.getAvatar());
		return new Result(true, StatusCode.OK, "User Login Successful", map);
	}

	@RequestMapping(value = "/register/{code}", method = RequestMethod.POST)
	public Result register(@PathVariable String code, @RequestBody User user){
		//Trying to retrieve verification code from redis
		String checkCodeInRedis = (String) redisTemplate.opsForValue().get("verificationCode_" + user.getMobile());

		if(checkCodeInRedis.isEmpty()){
			return new Result(false, StatusCode.ERROR, "Please obtain a verification code first!");
		}
		if(!checkCodeInRedis.equals(code)){
			return new Result(false, StatusCode.ERROR, "Verification code is incorrect!");
		}
		userService.add(user);
		return new Result(true, StatusCode.OK, "Register Successful");
	}

	@RequestMapping(value = "/sendsms/{mobile}", method = RequestMethod.POST)
	public Result sendSms(@PathVariable String mobile){
		userService.sendSms(mobile);
		return new Result(true, StatusCode.OK, "Message Sent.");
	}

	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",userService.findAll());
	}

	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",userService.findById(id));
	}

	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<User> pageList = userService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<User>(pageList.getTotalElements(), pageList.getContent()) );
	}

    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",userService.findSearch(searchMap));
    }

	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody User user  ){
		userService.add(user);
		return new Result(true,StatusCode.OK,"增加成功");
	}

	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody User user, @PathVariable String id ){
		user.setId(id);
		userService.update(user);		
		return new Result(true,StatusCode.OK,"Update Successful");
	}

	/**
	 * Only user with admin role can use this method
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		userService.deleteById(id);
		return new Result(true,StatusCode.OK,"Delete Successful");
	}
	
}
