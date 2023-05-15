package com.pract.crud.service.impl;

import com.pract.crud.dto.UserDto;
import com.pract.crud.dto.UserSettingDto;
import com.pract.crud.entity.User;
import com.pract.crud.repository.UserRepository;
import com.pract.crud.repository.UserSettingRepository;
import com.pract.crud.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserSettingRepository userSettingRepository;

    @Override
    public List<UserDto> findAll(int offset, int limit) {
        /*double pageNumber = 0;
        if (offset > 0)
            pageNumber = (double) (offset / limit) + ( offset % limit );
        //PageRequest pReq = PageRequest.of((int) pageNumber, limit);*/
        return userRepository.findAll(offset, limit).stream()
                .map(UserDto::toDto)
                .toList();
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    @Transactional
    public UserDto save(UserDto user) {
        User savedUser = userRepository.saveAndFlush(UserDto.toEntity(user));

        UserSettingDto.generateDefaultUserSetting()
                .forEach(userSettingDto -> userSettingRepository.saveAndFlush(UserSettingDto.toEntity(userSettingDto, savedUser)));
        return UserDto.toDto(savedUser);
    }

    @Override
    public UserDto findById(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent() && user.get().getDeletedTime() == null && user.get().getIsActive()) {
            return UserDto.toDto(user.get());
        }
        return null;
    }

    @Override
    @Transactional
    public UserDto refresh(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent() && user.get().getDeletedTime() != null && !user.get().getIsActive()) {
            User edited = user.get();
            edited.setDeletedTime(null);
            edited.setIsActive(true);
            return UserDto.toDto(edited);
        }
        return null;
    }

    @Override
    @Transactional
    public UserDto updateSetting(long id, List<UserSettingDto> settings) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent() && user.get().getDeletedTime() == null && user.get().getIsActive()) {
            userSettingRepository.saveAll(settings.
                    stream()
                    .map(UserSettingDto::toEntity)
                    .toList());
            return UserDto.toDto(user.get());
        }
        return null;
    }

    @Override
    @Transactional
    public void delete(UserDto dto) {
        try {
            userRepository.delete(UserDto.toEntity(dto));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean isExist(String ssn) {
        return userRepository.existsUserBySsn(ssn);
    }

    @Override
    public boolean isExist(long id) {
        return userRepository.existsById(id);
    }
}
