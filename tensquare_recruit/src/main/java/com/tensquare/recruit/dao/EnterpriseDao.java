package com.tensquare.recruit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.recruit.pojo.Enterprise;

import java.util.List;

public interface EnterpriseDao extends JpaRepository<Enterprise, String>, JpaSpecificationExecutor<Enterprise> {
    public List<Enterprise> findByIshot(String ishot);//where ishot = ?
}
