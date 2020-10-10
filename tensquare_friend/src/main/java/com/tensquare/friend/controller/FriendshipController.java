package com.tensquare.friend.controller;

import com.tensquare.friend.client.UserClient;
import com.tensquare.friend.service.FriendshipService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/friend")
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserClient userClient;

    /**
     * This function will create a friendship related relation and either
     * add a record to friend table or add a record to dislike table.
     * if type is 1, userid likes personid
     * if type is 0, userid dislike personid
     */
    @RequestMapping(value = "/{personid}/{type}", method = RequestMethod.PUT)
    public Result addRelation(@PathVariable String personid, @PathVariable String type) {
        //verify if current user has logged in or not, also get the current user's id
        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims == null) {
            return new Result(false, StatusCode.LOGINERROR, "Not authorized, login first!");
        }
        // if logined, get user id
        String userid = claims.getId();

        //tell if add a friend or add a dislike
        if (type != null) {
            if (type.equals("1")) {
                //add a record to friend table
                int flag = friendshipService.addFriend(userid, personid);
                if (flag == 0) {// userid has already liked this person
                    return new Result(false, StatusCode.ERROR, "You have already added this friend");
                } else if (flag == 1) {
                    //Besides updating friend table, we also need to update user table
                    userClient.updateFanscountAndFollowcount(userid, personid, 1);//update userid's followcount and friendid's fanscount
                    return new Result(true, StatusCode.OK, "You have successfully added this friend");
                }
            } else {//add a record to dislike table
                int flag = friendshipService.addDislike(userid, personid);
                if (flag == 0) {// userid has already disliked this person
                    return new Result(false, StatusCode.ERROR, "You have already unlike this friend");
                } else if (flag == 1) {
                    return new Result(true, StatusCode.OK, "You have successfully unliked this friend");
                }
            }
        }
        return new Result();
    }

    /**
     * Delete a record from friend table, and then add a record in dislike table
     */
    @RequestMapping(value = "/{friendid}", method = RequestMethod.DELETE)
    public Result deleteFriend(@PathVariable String friendid) {
        //verify if login or not, get the current user's id
        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims == null) {
            return new Result(false, StatusCode.LOGINERROR, "Not authorized");
        }
        // if logined, get user id
        String userid = claims.getId();
        friendshipService.deleteFriend(userid, friendid);
        userClient.updateFanscountAndFollowcount(userid, friendid, -1);
        return new Result(true, StatusCode.OK, "You have deleted a friend");
    }
}
