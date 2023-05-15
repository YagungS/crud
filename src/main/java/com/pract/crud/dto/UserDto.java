package com.pract.crud.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pract.crud.entity.User;
import com.pract.crud.util.Util;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

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
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("birth_date")
    private Date birthDate;

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

    @JsonIgnoreProperties
    @Value("${date.output.format}")
    private static String dateOutputFormat;

    public static User toEntity(UserDto dto) {
        if (dto != null) {
            User user = new User();
            user.setId(dto.getId());
            user.setSsn(dto.getSsn());
            user.setFirstName(dto.getFirstName());
            user.setMiddleName(dto.getMiddleName());
            user.setFamilyName(dto.getFamilyName());
            user.setBirthDate(dto.getBirthDate());
            user.setIsActive(dto.getIsActive() == null || dto.getIsActive());
            return user;
        }
        return null;
    }

    public static UserDto toDto(User user) {
        if (user != null) {
            return new UserDto(user.getId(),
                    user.getSsn(),
                    user.getFirstName(),
                    user.getMiddleName(),
                    user.getFamilyName(),
                    user.getBirthDate(),
                    user.getCreatedTime() == null ? null : Util.dateToStr(user.getCreatedTime(), dateOutputFormat),
                    user.getUpdatedTime() == null ? null :
                            Util.dateToStr(user.getUpdatedTime(), dateOutputFormat),
                    user.getCreatedBy(),
                    user.getUpdatedBy(),
                    user.getIsActive(),
                    user.getDeletedTime() == null ? null : Util.dateToStr(user.getDeletedTime(), dateOutputFormat));
        }
        return null;
    }
}