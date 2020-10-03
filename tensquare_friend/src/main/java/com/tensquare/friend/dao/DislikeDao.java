package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.Dislike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DislikeDao extends JpaRepository<Dislike, String> {
    public Dislike findByUseridAndPersonid(String userid, String personid);
}
