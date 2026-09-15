package com.example.demo.dao;

import java.util.Optional;

import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import com.example.demo.entity.Profile;

@Dao
@ConfigAutowireable
public interface ProfileDao {

	@Select
	Optional<Profile> findByUserId(Integer userId);
	
	@Insert
	int create(Profile profile);
	
	@Update
	int update(Profile profile);
	
	@Delete
	int delete(Profile profile);
}
