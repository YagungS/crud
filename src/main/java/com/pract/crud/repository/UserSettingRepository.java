package com.pract.crud.repository;

import com.pract.crud.entity.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSettingRepository extends JpaRepository<UserSetting, Long> {
    List<UserSetting> findUserSettingByUser_Id(long id);
}
