package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LabelService {
    @Autowired
    private LabelDao labelDao;
    @Autowired
    private IdWorker idWorker;

    public List<Label> findAll() {
        return labelDao.findAll();
    }

    public Label findById(String id) {
        return labelDao.findById(id).get();
    }

    public void save(Label label) {
        label.setId(idWorker.nextId() + "");
        labelDao.save(label);
    }

    public void update(Label label) {
        labelDao.save(label);
    }

    public void deleteById(String id) {
        labelDao.deleteById(id);
    }

    public List<Label> findSearch(Label label) {
        return labelDao.findAll(new Specification<Label>() {
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //1. create a list to store the predicates temporarily
                List<Predicate> list = new ArrayList<>();
                //2. create predicates
                if (label.getLabelname() != null && !"".equals(label.getLabelname())) {
                    Predicate p = cb.like(root.get("labelname").as(String.class), "%" + label.getLabelname() + "%"); //where labelname like "%...%"
                    list.add(p);
                }
                if (label.getState() != null && !"".equals(label.getState())) {
                    Predicate p = cb.equal(root.get("state").as(String.class), label.getState());
                    list.add(p);
                }
                //3. create an array of predicates
                Predicate[] array = new Predicate[list.size()];
                list.toArray(array);
                return cb.and(array);
            }
        });
    }

    public Page<Label> pageQuery(Label label, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size); //page 1 in front end corresponds to page 0 in Spring
        return labelDao.findAll(new Specification<Label>() {
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //1. create a list to store the predicates temporarily
                List<Predicate> list = new ArrayList<>();
                //2. create predicates
                if (label.getLabelname() != null && !"".equals(label.getLabelname())) {
                    Predicate p = cb.like(root.get("labelname").as(String.class), "%" + label.getLabelname() + "%"); //where labelname like "%...%"
                    list.add(p);
                }
                if (label.getState() != null && !"".equals(label.getState())) {
                    Predicate p = cb.equal(root.get("state").as(String.class), label.getState());
                    list.add(p);
                }
                //3. create an array of predicates
                Predicate[] array = new Predicate[list.size()];
                list.toArray(array);
                return cb.and(array);
            }
        }, pageable);
    }
}
