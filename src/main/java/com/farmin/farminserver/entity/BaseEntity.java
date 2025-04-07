package com.farmin.farminserver.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Data
@NoArgsConstructor
public abstract class BaseEntity {
    // 식별자 제거. 공통 필드만 둠
    // 추후 createAt, updateAt 등 공통 컬럼 넣을 수 있음
}
