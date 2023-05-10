package com.pract.crud.repository;

import com.pract.crud.entity.User;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    @Modifying
    @Query(value = "select u.* from tbl_user u where u.deleted_time IS NULL AND u.is_active = true LIMIT 0,5", nativeQuery = true)
    List<User> findAll(@Param("offset") Integer offset,@Param("limit") Integer limit);

    boolean existsUserBySsn(String ssn);

}
