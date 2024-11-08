package com.farmin.farminserver.entity.farminfo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.farmin.farminserver.entity.BaseEntity;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FarmInfo")
public class FarmInfoEntity extends BaseEntity {

    @Column(unique = true, nullable = false, length = 50)
    private String FarmID;

    @Column(nullable = false, length = 50)
    private String FarmName;

    @Column(nullable = false, length = 100)
    private String UserID;

}
