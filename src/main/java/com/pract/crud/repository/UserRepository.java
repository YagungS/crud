package com.pract.crud.repository;

import com.pract.crud.entity.User;
import org.hibernate.annotations.Where;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    @Where(clause = "deleted_time IS NULL")
    public List<User> findAll();

    boolean existsUserBySsn(String ssn);
}
