package com.tensquare.whine.dao;

import com.tensquare.whine.pojo.Whine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WhineDao extends MongoRepository<Whine, String> {

    public Page<Whine> findByParentId(String parentid, Pageable pageable);
}
