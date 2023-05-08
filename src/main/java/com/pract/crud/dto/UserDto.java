package com.pract.crud.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pract.crud.entity.User;
import com.pract.crud.util.Util;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private long id;

    @NotNull
    @Pattern(regexp = "^0[0-9]{15}$")
    @JsonProperty("ssn")
    private String ssn;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{3,100}$")
    @JsonProperty("first_name")
    private String firstName;

    @Pattern(regexp = "^[a-zA-Z0-9]{3,100}$")
    @JsonProperty("middle_name")
    private String middleName;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{3,100}$")
    @JsonProperty("family_name")
    private String familyName;

    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd")
    @JsonProperty("birth_date")
    private String birthDate;

    @JsonProperty("created_time")
    private String createdTime;


    @JsonProperty("updated_time")
    private String updatedTime;

    @Nullable
    @Size(min = 3, max = 100)
    @JsonProperty("created_by")
    private String createdBy;

    @Nullable
    @Size(min = 3, max = 100)
    @JsonProperty("updated_by")
    private String updatedBy;

    @Nullable
    @JsonProperty("is_active")
    private Boolean isActive = true;

    private String deletedTime;

    public UserDto(long id, String ssn, String first, String middle, String family, String birth) {
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setSsn(ssn);
        userDto.setFirstName(first);
        userDto.setMiddleName(middle);
        userDto.setFamilyName(family);
        userDto.setBirthDate(birth);
    }

    public static User toEntity(UserDto dto){
        if (dto !=null) {
            User user = new User();
            user.setId(dto.getId());
            user.setSsn(dto.getSsn());
            user.setFirstName(dto.getFirstName());
            user.setMiddleName(dto.getMiddleName());
            user.setFamilyName(dto.getFamilyName());
            user.setBirthDate(Util.strToDate(dto.getBirthDate(), "yyyy-mm-dd"));
            user.setIsActive(dto.getIsActive() == null?true:dto.getIsActive());
            return user;
        }
        return null;
    }

    public static UserDto toDto(User user){
        if (user !=null) {
            return new UserDto(user.getId(),
                user.getSsn(),
                user.getFirstName(),
                user.getMiddleName(),
                user.getFamilyName(),
                Util.dateToStr(user.getBirthDate(), "yyyy-mm-dd"),
                Util.dateToStr(user.getCreatedTime(),"yyyy-mm-dd'T'HH:m:ss'Z'"),
                user.getUpdatedTime() == null? null:
                Util.dateToStr(user.getUpdatedTime(),"yyyy-mm-dd'T'HH:m:ss'Z'"),
                user.getCreatedBy(),
                    user.getUpdatedBy(),
                user.getIsActive(),
                user.getDeletedTime() == null?null:Util.dateToStr(user.getDeletedTime(),"yyyy-mm-dd'T'HH:m:ss'Z'"));
        }
        return null;
    }
}