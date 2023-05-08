package com.pract.crud.service.impl;

import com.pract.crud.dto.UserSettingDto;
import com.pract.crud.entity.UserSetting;
import com.pract.crud.repository.UserSettingRepository;
import com.pract.crud.service.UserSettingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserSettingServiceImpl implements UserSettingService {

    private final UserSettingRepository userSettingRepository;

    @Override
    public List<UserSettingDto> findByUserId(long id) {
        List<UserSetting> result = userSettingRepository.findUserSettingByUser_Id(id);
        if (result != null && !result.isEmpty()) {
            return result.stream()
                    .map(UserSettingDto::toDto)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    @Transactional
    public List<UserSettingDto> update(List<UserSettingDto> settings, long id) {
        List<UserSetting> old_settings = userSettingRepository.findUserSettingByUser_Id(id);
        if (old_settings.isEmpty())
            return null;
        for (UserSetting old:old_settings) {
            for (UserSettingDto newS:settings) {
                if(old.getKey().equals(newS.getKey())){
                    old.setValue(newS.getValue());
                    userSettingRepository.save(old);
                }
            }
        }

        return old_settings.stream().map(UserSettingDto::toDto).toList();
    }
}
