package com.tensquare.article.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import util.IdWorker;

import com.tensquare.article.dao.ColumnDao;
import com.tensquare.article.pojo.Column;

@Service
public class ColumnService {

    @Autowired
    private ColumnDao columnDao;

    @Autowired
    private IdWorker idWorker;

    public List<Column> findAll() {
        return columnDao.findAll();
    }

    public Page<Column> findSearch(Map whereMap, int page, int size) {
        Specification<Column> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return columnDao.findAll(specification, pageRequest);
    }

    public List<Column> findSearch(Map whereMap) {
        Specification<Column> specification = createSpecification(whereMap);
        return columnDao.findAll(specification);
    }

    public Column findById(String id) {
        return columnDao.findById(id).get();
    }

    public void add(Column column) {
        column.setId(idWorker.nextId() + "");
        columnDao.save(column);
    }

    public void update(Column column) {
        columnDao.save(column);
    }

    public void deleteById(String id) {
        columnDao.deleteById(id);
    }

    private Specification<Column> createSpecification(Map searchMap) {

        return new Specification<Column>() {

            @Override
            public Predicate toPredicate(Root<Column> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                if (searchMap.get("name") != null && !"".equals(searchMap.get("name"))) {
                    predicateList.add(cb.like(root.get("name").as(String.class), "%" + (String) searchMap.get("name") + "%"));
                }
                if (searchMap.get("summary") != null && !"".equals(searchMap.get("summary"))) {
                    predicateList.add(cb.like(root.get("summary").as(String.class), "%" + (String) searchMap.get("summary") + "%"));
                }
                if (searchMap.get("userid") != null && !"".equals(searchMap.get("userid"))) {
                    predicateList.add(cb.like(root.get("userid").as(String.class), "%" + (String) searchMap.get("userid") + "%"));
                }
                if (searchMap.get("state") != null && !"".equals(searchMap.get("state"))) {
                    predicateList.add(cb.like(root.get("state").as(String.class), "%" + (String) searchMap.get("state") + "%"));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }
}
