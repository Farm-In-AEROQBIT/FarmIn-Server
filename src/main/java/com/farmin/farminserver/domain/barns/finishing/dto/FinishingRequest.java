package com.farmin.farminserver.domain.barns.finishing.dto;

import lombok.Data;

@Data
public class FinishingRequest {
    private String snfarmID;
    private int farmId;
    private String barnType;

    // Getters and Setters
    public String getSnFarmId() {
        return snfarmID;
    }

    public void setSnFarmId(String snFarmId) {
        this.snfarmID = snFarmId;
    }

    public int getFarmId() {
        return farmId;
    }

    public void setFarmId(int farmId) {
        this.farmId = farmId;
    }

    public String getBarnType() {
        return barnType;
    }

    public void setBarnType(String barnType) {
        this.barnType = barnType;
    }

}
