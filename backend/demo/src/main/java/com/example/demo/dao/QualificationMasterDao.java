package com.example.demo.dao;

import java.util.List;
import java.util.Optional;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import com.example.demo.entity.QualificationMaster;

@Dao
@ConfigAutowireable
public interface QualificationMasterDao {

	@Select
	List<QualificationMaster> findAll();
	
	@Select
	Optional<QualificationMaster> findById(Integer id);
}
