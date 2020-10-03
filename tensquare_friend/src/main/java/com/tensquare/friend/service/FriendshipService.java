package com.tensquare.friend.service;

import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.DislikeDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.Dislike;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class FriendshipService {
    @Autowired
    private FriendDao friendDao;

    @Autowired
    private DislikeDao dislikeDao;

    public int addFriend(String userid, String friendid) {
        //first, tell if (userid, friendid) is already in DB
        //In other words, if userid has already liked friendid, we do nothing
        Friend friend = friendDao.findByUseridAndFriendid(userid, friendid);
        if(friend != null){
            return 0;
        }
        //add (userid, friendid, 0) to DB
        friend = new Friend();
        friend.setUserid(userid);
        friend.setFriendid(friendid);
        friend.setIslike("0");
        friendDao.save(friend);

        //tell if (friendid, userid) is already in DB, if so, change from 0 to 1
        //In other words, does friendid already liked userid?
        if(friendDao.findByUseridAndFriendid(friendid, userid) != null){
            friendDao.updateIslike("1", userid, friendid);
            friendDao.updateIslike("1", friendid, userid);
        }
        return 1;
    }

    public int addDislike(String userid, String personid) {
        Dislike dislike = dislikeDao.findByUseridAndPersonid(userid, personid);
        //If userid has already disliked personid
        if(dislike != null){
            return 0;
        }
        dislike = new Dislike();
        dislike.setUserid(userid);
        dislike.setPersonid(personid);
        dislikeDao.save(dislike);
        return 1;
    }

    //delete a record from friend table, add a record to dislike table
    public void deleteFriend(String userid, String friendid) {
        //delete the record in tb_friend
        friendDao.deleteFriend(userid, friendid);
        //update friendid, userid record to 0
        friendDao.updateIslike(friendid, userid, "0");//if there is no friendid to userid in db, it's OK
        //add a record to tb_nofriend table
        Dislike noFriend = new Dislike();
        noFriend.setPersonid(friendid);// The variable name friendid is weird here, it was a friend, now it isn't
        noFriend.setUserid(userid);
        dislikeDao.save(noFriend);
    }
}
