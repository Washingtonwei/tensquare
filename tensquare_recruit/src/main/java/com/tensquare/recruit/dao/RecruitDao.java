package com.tensquare.recruit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.recruit.pojo.Recruit;

import java.util.List;

public interface RecruitDao extends JpaRepository<Recruit, String>, JpaSpecificationExecutor<Recruit> {

    public List<Recruit> findTop6ByStateOrderByCreatetimeDesc(String state);//where state=? order by createime

    public List<Recruit> findTop6ByStateNotOrderByCreatetimeDesc(String state);//where state!=? order by createime
}
