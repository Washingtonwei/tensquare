package com.tensquare.recruit.service;

import com.tensquare.recruit.dao.RecruitDao;
import com.tensquare.recruit.pojo.Recruit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RecruitService {

    @Autowired
    private RecruitDao recruitDao;

    @Autowired
    private IdWorker idWorker;

    public List<Recruit> recommend() {
        return recruitDao.findTop6ByStateOrderByCreatetimeDesc("2");
    }

    public List<Recruit> newlist() {
        return recruitDao.findTop6ByStateNotOrderByCreatetimeDesc("0");
    }

    public List<Recruit> findAll() {
        return recruitDao.findAll();
    }

    public Page<Recruit> findSearch(Map whereMap, int page, int size) {
        Specification<Recruit> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return recruitDao.findAll(specification, pageRequest);
    }

    public List<Recruit> findSearch(Map whereMap) {
        Specification<Recruit> specification = createSpecification(whereMap);
        return recruitDao.findAll(specification);
    }

    public Recruit findById(String id) {
        return recruitDao.findById(id).get();
    }

    public void add(Recruit recruit) {
        recruit.setId(idWorker.nextId() + "");
        recruitDao.save(recruit);
    }

    public void update(Recruit recruit) {
        recruitDao.save(recruit);
    }

    public void deleteById(String id) {
        recruitDao.deleteById(id);
    }

    private Specification<Recruit> createSpecification(Map searchMap) {

        return new Specification<Recruit>() {

            @Override
            public Predicate toPredicate(Root<Recruit> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                if (searchMap.get("jobname") != null && !"".equals(searchMap.get("jobname"))) {
                    predicateList.add(cb.like(root.get("jobname").as(String.class), "%" + (String) searchMap.get("jobname") + "%"));
                }
                if (searchMap.get("salary") != null && !"".equals(searchMap.get("salary"))) {
                    predicateList.add(cb.like(root.get("salary").as(String.class), "%" + (String) searchMap.get("salary") + "%"));
                }
                if (searchMap.get("condition") != null && !"".equals(searchMap.get("condition"))) {
                    predicateList.add(cb.like(root.get("condition").as(String.class), "%" + (String) searchMap.get("condition") + "%"));
                }
                if (searchMap.get("education") != null && !"".equals(searchMap.get("education"))) {
                    predicateList.add(cb.like(root.get("education").as(String.class), "%" + (String) searchMap.get("education") + "%"));
                }
                if (searchMap.get("type") != null && !"".equals(searchMap.get("type"))) {
                    predicateList.add(cb.like(root.get("type").as(String.class), "%" + (String) searchMap.get("type") + "%"));
                }
                if (searchMap.get("address") != null && !"".equals(searchMap.get("address"))) {
                    predicateList.add(cb.like(root.get("address").as(String.class), "%" + (String) searchMap.get("address") + "%"));
                }
                if (searchMap.get("eid") != null && !"".equals(searchMap.get("eid"))) {
                    predicateList.add(cb.like(root.get("eid").as(String.class), "%" + (String) searchMap.get("eid") + "%"));
                }
                if (searchMap.get("state") != null && !"".equals(searchMap.get("state"))) {
                    predicateList.add(cb.like(root.get("state").as(String.class), "%" + (String) searchMap.get("state") + "%"));
                }
                if (searchMap.get("url") != null && !"".equals(searchMap.get("url"))) {
                    predicateList.add(cb.like(root.get("url").as(String.class), "%" + (String) searchMap.get("url") + "%"));
                }
                if (searchMap.get("label") != null && !"".equals(searchMap.get("label"))) {
                    predicateList.add(cb.like(root.get("label").as(String.class), "%" + (String) searchMap.get("label") + "%"));
                }
                if (searchMap.get("content1") != null && !"".equals(searchMap.get("content1"))) {
                    predicateList.add(cb.like(root.get("content1").as(String.class), "%" + (String) searchMap.get("content1") + "%"));
                }
                if (searchMap.get("content2") != null && !"".equals(searchMap.get("content2"))) {
                    predicateList.add(cb.like(root.get("content2").as(String.class), "%" + (String) searchMap.get("content2") + "%"));
                }
                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }
}
