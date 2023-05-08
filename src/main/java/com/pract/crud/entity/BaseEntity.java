package com.pract.crud.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @CreationTimestamp
    @Column(name = "created_time", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdTime;

    @CreatedBy
    @Column(name = "created_by", nullable = false, length = 100, updatable = false, columnDefinition = "varchar(100) default 'SYSTEM'")
    private String createdBy = "SYSTEM";

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_time", insertable = false)
    private Date updatedTime;

    @LastModifiedBy
    @Column(name = "updated_by", length = 100, insertable = false, columnDefinition = "varchar(100) default 'SYSTEM'")
    private String updatedBy = "SYSTEM";

    @Column(name = "deleted_time", insertable = false)
    private Date deletedTime;
}
