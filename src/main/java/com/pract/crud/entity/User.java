package com.pract.crud.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import java.io.Serial;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tbl_user")
@SQLDelete(sql = "UPDATE tbl_user SET deleted_time = NOW(), is_active = false WHERE id = ?")
public class User extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(nullable = false, unique = true, length = 16)
    private String ssn;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "middle_name", length = 100)
    private String middleName;

    @Column(name = "family_name", nullable = false, length = 100)
    private String familyName;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private Boolean isActive = true;

    public User(long id) {
        User user = new User();
        user.setId(id);
    }

}