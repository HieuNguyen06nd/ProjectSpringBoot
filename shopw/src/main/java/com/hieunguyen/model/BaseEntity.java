package com.hieunguyen.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseEntity {
	
	@Column(name = "created_at")
	LocalDate createdAt; 
	
	@Column(name = "updated_at")
	LocalDate updatedAt;
	
	 @PrePersist
	    protected void onCreate() {
	        createdAt = LocalDate.now();
	        updatedAt = LocalDate.now();
	    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }

}
