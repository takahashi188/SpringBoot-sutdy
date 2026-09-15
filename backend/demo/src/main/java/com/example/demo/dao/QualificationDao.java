package com.example.demo.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import com.example.demo.entity.Qualification;

@Dao
@ConfigAutowireable
public interface QualificationDao {

	@Select
	List<Qualification> findByUserId(Integer userId);
	
	@Insert
	int create(Qualification qualifications);
	
	@Update
	int update(Qualification qualifications);
	
	@Delete
	int delete(Qualification qualifications);
}
