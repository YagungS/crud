package com.pract.crud.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.pract.crud.entity.User;
import com.pract.crud.entity.UserSetting;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"id","userId"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class UserSettingDto {
    private long id;

    @NotNull
    private String key;

    @NotNull
    private String value;

    private long userId;

    public static UserSettingDto toDto(UserSetting userSetting) {
        if (userSetting != null) {
            return new UserSettingDto(userSetting.getId(), userSetting.getKey(), userSetting.getValue(),
                    userSetting.getUser()!=null?userSetting.getUser().getId():0);
        }

        return null;
    }

    public static UserSetting toEntity(UserSettingDto dto){
        if (dto != null) {
            return new UserSetting(dto.id, dto.key, dto.value, new User(dto.getUserId()));
        }

        return null;
    }

    public static UserSetting toEntity(UserSettingDto dto, User user){
        if (dto != null) {
            return new UserSetting(dto.id, dto.key, dto.value, user);
        }

        return null;
    }

    public static List<UserSettingDto> generateDefaultUserSetting() {

        return Stream.of(new String[][] {
                { "biometric_login", "false" },
                { "push_notification", "false" },
                { "sms_notification", "false" },
                { "show_onboarding", "false" },
                { "widget_order", "1,2,3,4,5" },
        }).map(data -> new UserSettingDto(0,data[0], data[1],0))
                .collect(Collectors.toList());
    }
}
