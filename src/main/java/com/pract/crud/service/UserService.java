package com.pract.crud.service;

import com.pract.crud.dto.UserDto;
import com.pract.crud.dto.UserSettingDto;

import java.util.List;

public interface UserService {
    public String Test();

    public List<UserDto> findAll(int offset, int limit);

    public long count();

    public UserDto save(UserDto user);

    public UserDto findById(long id);

    public UserDto update(UserDto userDto);

    public UserDto refresh(long id);

    public UserDto updateSetting(long id, List<UserSettingDto> settings);

    public void delete(long id);

    public boolean isExist(String ssn);

    public boolean isExist(long id);
}
