package com.pract.crud.service.impl;

import com.pract.crud.dto.UserDto;
import com.pract.crud.dto.UserSettingDto;
import com.pract.crud.entity.User;
import com.pract.crud.repository.UserRepository;
import com.pract.crud.repository.UserSettingRepository;
import com.pract.crud.service.UserService;
import com.pract.crud.util.OffsetBasedPageable;
import com.pract.crud.util.Util;
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
    public String Test() {
        return "null";
    }

    @Override
    public List<UserDto> findAll(int offset, int limit) {
        double pageNumber = 0;
        if (offset > 0)
            pageNumber = Math.floor(offset / limit) + ( offset % limit );
        PageRequest pReq = PageRequest.of(Integer.valueOf((int) pageNumber), limit);
        //OffsetBasedPageable pageable = new OffsetBasedPageable(limit, offset);
        return userRepository.findAll(pReq).getContent().stream()
                .map(UserDto::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    @Transactional
    public UserDto save(UserDto user) {
        try{
            User savedUser = userRepository.saveAndFlush(UserDto.toEntity(user));

            UserSettingDto.generateDefaultUserSetting()
                    .forEach(userSettingDto -> {
                        userSettingRepository.saveAndFlush(UserSettingDto.toEntity(userSettingDto, savedUser));
                    });
            return UserDto.toDto(savedUser);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public UserDto findById(long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent() && user.get().getDeletedTime() == null && user.get().getIsActive()){
            return UserDto.toDto(user.get());
        }
        return null;
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto) {
        Optional<User> user = userRepository.findById(userDto.getId());
        if(user.isPresent() && user.get().getDeletedTime() == null && user.get().getIsActive()){
            User edited = user.get();
            edited.setSsn(userDto.getSsn());
            edited.setFirstName(userDto.getFirstName());
            edited.setMiddleName(userDto.getMiddleName());
            edited.setFamilyName(userDto.getFamilyName());
            edited.setBirthDate(Util.strToDate(userDto.getBirthDate(),"yyyy-mm-dd"));
            userRepository.save(edited);
            return UserDto.toDto(edited);
        }
        return null;
    }

    @Override
    @Transactional
    public UserDto refresh(long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent() && user.get().getDeletedTime() != null && !user.get().getIsActive()){
            User edited = user.get();
            edited.setDeletedTime(null);
            userRepository.save(edited);
            return UserDto.toDto(edited);
        }
        return null;
    }

    @Override
    @Transactional
    public UserDto updateSetting(long id, List<UserSettingDto> settings) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent() && user.get().getDeletedTime() == null && user.get().getIsActive()){
            userSettingRepository.saveAll(settings.
                    stream()
                    .map(UserSettingDto::toEntity)
                    .toList());
            return UserDto.toDto(user.get());
        }
        return null;
    }

    @Override
    public void delete(long id) {
        try{
            userRepository.deleteById(id);
        }
        catch (Exception ex){
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
