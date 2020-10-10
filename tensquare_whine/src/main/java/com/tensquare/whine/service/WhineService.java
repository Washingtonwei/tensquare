package com.tensquare.whine.service;

import com.tensquare.whine.dao.WhineDao;
import com.tensquare.whine.pojo.Whine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class WhineService {

    @Autowired
    private WhineDao whineDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Whine> findAll() {
        return whineDao.findAll();
    }

    public Whine findById(String id) {
        return whineDao.findById(id).get();
    }

    public void save(Whine whine) {
        //in whine, user has already provided some data
        //but the following, we need to provide when a new whine is created
        //to avoid NPE, we initialize them here
        whine.set_id(idWorker.nextId() + "");
        whine.setPublishtime(new Date());
        whine.setVisits(0);
        whine.setShare(0);
        whine.setThumbup(0);
        whine.setComment(0);//number of replies
        whine.setState("1");
        //If the new whine has a parent whine, then the number of comments
        // in its parent should be increment by 1
        if (whine.getParentid() != null && !"".equals(whine.getParentid())) {
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(whine.getParentid()));
            Update update = new Update();
            update.inc("comment", 1);
            mongoTemplate.updateFirst(query, update, "whine");
        }

        whineDao.save(whine);
    }

    public void update(Whine whine) {
        whineDao.save(whine);
    }

    public void deleteById(String id) {
        whineDao.deleteById(id);
    }

    public Page<Whine> findByParentid(String parentid, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return whineDao.findByParentId(parentid, pageable);
    }

    public void updateThumbup(String whineId) {
        //First approach: easy but not very efficient, since there are two accesses with DB
        //Whine whine = wineDao.findById(whineId).get();
        //whine.setThumbup((whine.getThumbup()==null ? 0 : whine.getThumbup()) + 1);
        //wineDao.save(whine);

        //Second approach: use db.whine.update({"_id":"1"},{$inc:{updateThumbup:NumberInt(1)}})
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(whineId));
        Update update = new Update();
        update.inc("thumbup", 1);
        mongoTemplate.updateFirst(query, update, "whine");
    }
}
