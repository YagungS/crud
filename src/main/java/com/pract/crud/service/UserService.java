package com.pract.crud.service;

import com.pract.crud.dto.UserDto;
import com.pract.crud.dto.UserSettingDto;

import java.util.List;

public interface UserService {

    List<UserDto> findAll(int offset, int limit);

    long count();

    UserDto save(UserDto user);

    UserDto findById(long id);

    UserDto refresh(long id);

    UserDto updateSetting(long id, List<UserSettingDto> settings);

    void delete(UserDto dto);

    boolean isExist(String ssn);

    boolean isExist(long id);
}
