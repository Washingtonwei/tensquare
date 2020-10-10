package com.tensquare.user.service;

import com.tensquare.user.dao.UserDao;
import com.tensquare.user.pojo.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private HttpServletRequest request;

    public List<User> findAll() {
        return userDao.findAll();
    }

    public Page<User> findSearch(Map whereMap, int page, int size) {
        Specification<User> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return userDao.findAll(specification, pageRequest);
    }

    public List<User> findSearch(Map whereMap) {
        Specification<User> specification = createSpecification(whereMap);
        return userDao.findAll(specification);
    }

    public User findById(String id) {
        return userDao.findById(id).get();
    }

    public void add(User user) {
        user.setId(idWorker.nextId() + "");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setFollowcount(0);
        user.setFanscount(0);
        user.setOnline(0L);
        user.setRegdate(new Date());
        user.setUpdatedate(new Date());
        user.setLastdate(new Date());
        userDao.save(user);
    }

    public void update(User user) {
        userDao.save(user);
    }

    /**
     * Only admin can delete a user
     *
     * @param id
     */
    public void deleteById(String id) {
        String token = (String) request.getAttribute("admin_claims");
        if (token == null || "".equals(token))
            throw new RuntimeException("Authorization failed");
        userDao.deleteById(id);
    }

    private Specification<User> createSpecification(Map searchMap) {

        return new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                if (searchMap.get("mobile") != null && !"".equals(searchMap.get("mobile"))) {
                    predicateList.add(cb.like(root.get("mobile").as(String.class), "%" + (String) searchMap.get("mobile") + "%"));
                }
                if (searchMap.get("password") != null && !"".equals(searchMap.get("password"))) {
                    predicateList.add(cb.like(root.get("password").as(String.class), "%" + (String) searchMap.get("password") + "%"));
                }
                if (searchMap.get("nickname") != null && !"".equals(searchMap.get("nickname"))) {
                    predicateList.add(cb.like(root.get("nickname").as(String.class), "%" + (String) searchMap.get("nickname") + "%"));
                }
                if (searchMap.get("sex") != null && !"".equals(searchMap.get("sex"))) {
                    predicateList.add(cb.like(root.get("sex").as(String.class), "%" + (String) searchMap.get("sex") + "%"));
                }
                if (searchMap.get("avatar") != null && !"".equals(searchMap.get("avatar"))) {
                    predicateList.add(cb.like(root.get("avatar").as(String.class), "%" + (String) searchMap.get("avatar") + "%"));
                }
                if (searchMap.get("email") != null && !"".equals(searchMap.get("email"))) {
                    predicateList.add(cb.like(root.get("email").as(String.class), "%" + (String) searchMap.get("email") + "%"));
                }
                if (searchMap.get("interest") != null && !"".equals(searchMap.get("interest"))) {
                    predicateList.add(cb.like(root.get("interest").as(String.class), "%" + (String) searchMap.get("interest") + "%"));
                }
                if (searchMap.get("personality") != null && !"".equals(searchMap.get("personality"))) {
                    predicateList.add(cb.like(root.get("personality").as(String.class), "%" + (String) searchMap.get("personality") + "%"));
                }
                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }

    // Generate text verification code, save to Redis and send to RabbitMQ
    public void sendSms(String mobile) {
        //generate 6 random numbers
        String random = RandomStringUtils.randomNumeric(6);
        //put a copy in redis, expire after 1 hour
        redisTemplate.opsForValue().set("verificationCode_" + mobile, random, 1, TimeUnit.HOURS);
        System.out.println("Verification code is " + random);
        //send to new user, but we are not going to wait for it. So put it in MQ
        Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("verificationCode", random);
        rabbitTemplate.convertAndSend("sms", map);
    }

    public User login(String mobile, String password) {
        //find user by mobile phone number
        User user = userDao.findByMobile(mobile);
        if (user != null && bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public void updateFanscountAndFollowcount(int x, String userid, String friendid) {
        userDao.updateFanscount(x, friendid);
        userDao.updateFollowcount(x, userid);
    }
}
