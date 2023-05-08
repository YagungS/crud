package com.pract.crud.service;

import com.pract.crud.dto.UserSettingDto;

import java.util.List;

public interface UserSettingService {
    public List<UserSettingDto> findByUserId(long id);

    public List<UserSettingDto> update(List<UserSettingDto> settings, long id);
}
