package com.farmin.farminserver.entity.barns.reserve;

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
@Table(name = "Reserve")
public class ReserveEntity extends BaseEntity {
    @Column(nullable = false, length = 50)
    private String FarmID;

    @Column(nullable = false, length = 100)
    private String ReserveID;
}
