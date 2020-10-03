package com.tensquare.gathering.service;

import com.tensquare.gathering.dao.GatheringDao;
import com.tensquare.gathering.pojo.Gathering;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
public class GatheringService {

	@Autowired
	private GatheringDao gatheringDao;
	
	@Autowired
	private IdWorker idWorker;

	public List<Gathering> findAll() {
		return gatheringDao.findAll();
	}

	public Page<Gathering> findSearch(Map whereMap, int page, int size) {
		Specification<Gathering> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return gatheringDao.findAll(specification, pageRequest);
	}


	public List<Gathering> findSearch(Map whereMap) {
		Specification<Gathering> specification = createSpecification(whereMap);
		return gatheringDao.findAll(specification);
	}

	@Cacheable(value = "gathering", key = "#id")
	public Gathering findById(String id) {
		return gatheringDao.findById(id).get();
	}

	public void add(Gathering gathering) {
		gathering.setId( idWorker.nextId()+"" );
		gatheringDao.save(gathering);
	}

	@CacheEvict(value = "gathering", key = "#gathering.id")
	public void update(Gathering gathering) {
		gatheringDao.save(gathering);
	}

	@CacheEvict(value = "gathering", key = "#id")
	public void deleteById(String id) {
		gatheringDao.deleteById(id);
	}

	private Specification<Gathering> createSpecification(Map searchMap) {

		return new Specification<Gathering>() {

			@Override
			public Predicate toPredicate(Root<Gathering> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                if (searchMap.get("name")!=null && !"".equals(searchMap.get("name"))) {
                	predicateList.add(cb.like(root.get("name").as(String.class), "%"+(String)searchMap.get("name")+"%"));
                }
                if (searchMap.get("summary")!=null && !"".equals(searchMap.get("summary"))) {
                	predicateList.add(cb.like(root.get("summary").as(String.class), "%"+(String)searchMap.get("summary")+"%"));
                }
                if (searchMap.get("detail")!=null && !"".equals(searchMap.get("detail"))) {
                	predicateList.add(cb.like(root.get("detail").as(String.class), "%"+(String)searchMap.get("detail")+"%"));
                }
                if (searchMap.get("sponsor")!=null && !"".equals(searchMap.get("sponsor"))) {
                	predicateList.add(cb.like(root.get("sponsor").as(String.class), "%"+(String)searchMap.get("sponsor")+"%"));
                }
                if (searchMap.get("image")!=null && !"".equals(searchMap.get("image"))) {
                	predicateList.add(cb.like(root.get("image").as(String.class), "%"+(String)searchMap.get("image")+"%"));
                }
                if (searchMap.get("address")!=null && !"".equals(searchMap.get("address"))) {
                	predicateList.add(cb.like(root.get("address").as(String.class), "%"+(String)searchMap.get("address")+"%"));
                }
                if (searchMap.get("state")!=null && !"".equals(searchMap.get("state"))) {
                	predicateList.add(cb.like(root.get("state").as(String.class), "%"+(String)searchMap.get("state")+"%"));
                }
                if (searchMap.get("city")!=null && !"".equals(searchMap.get("city"))) {
                	predicateList.add(cb.like(root.get("city").as(String.class), "%"+(String)searchMap.get("city")+"%"));
                }
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));
			}
		};
	}
}
