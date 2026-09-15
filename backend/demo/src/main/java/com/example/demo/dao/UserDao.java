package com.example.demo.dao;

import java.util.List;
import java.util.Optional;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import com.example.demo.dto.UserDetailDto;
import com.example.demo.entity.User;

@Dao
@ConfigAutowireable
public interface UserDao {
	
	@Select
	List<User> findAll(String sortColumn, String direction, SelectOptions options);

	@Select
	List<User> findAllOrderByIdAsc(SelectOptions options);

	@Select
	List<User> findAllOrderByIdDesc(SelectOptions options);

	@Select
	List<User> findAllOrderByNameAsc(SelectOptions options);

	@Select
	List<User> findAllOrderByNameDesc(SelectOptions options);
	
	@Select
	List<User> findByName(String name, String sortColumn, String direction, SelectOptions options);
	
	@Select
	List<User> findByNameOrderByIdAsc(
	        String name,
	        SelectOptions options);

	@Select
	List<User> findByNameOrderByIdDesc(
	        String name,
	        SelectOptions options);

	@Select
	List<User> findByNameOrderByNameAsc(
	        String name,
	        SelectOptions options);

	@Select
	List<User> findByNameOrderByNameDesc(
	        String name,
	        SelectOptions options);
	
	@Select
	Optional<User> findById(Integer id);
	
	@Select
	Optional<User> findByEmail(String email);
	
	@Select
	Optional<User> findByEmailAndNotId(String email, Integer id);
	
	@Select
	List<UserDetailDto> findByIdDetailDto(Integer id);
	
	@Insert
	int create(User user);
	
	@Update(sqlFile = true)
	int delete(User user);
	
	@Update
	int update(User user);
}
