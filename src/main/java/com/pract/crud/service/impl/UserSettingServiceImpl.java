package com.pract.crud.service.impl;

import com.pract.crud.dto.UserSettingDto;
import com.pract.crud.entity.UserSetting;
import com.pract.crud.repository.UserSettingRepository;
import com.pract.crud.service.UserSettingService;
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
}
