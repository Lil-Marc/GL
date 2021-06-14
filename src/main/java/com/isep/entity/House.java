package com.isep.entity;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@AllArgsConstructor  //
@NoArgsConstructor   //
@ToString//tostring
@Accessors(chain = true)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
public class House implements Serializable{
    private int id;
    private String description;
    private JSONArray restriction;
    private JSONArray service;
    private String address;
    private String img;
    private String houseSize;
    private Boolean ifBooked;
    private String possessor;
    @JsonIgnore
    private int tenantId;

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JSONArray getRestriction() {
        return restriction;
    }

    public void setRestriction(JSONArray restriction) {
        this.restriction = restriction;
    }

    public JSONArray getService() {
        return service;
    }

    public void setService(JSONArray service) {
        this.service = service;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getHouseSize() {
        return houseSize;
    }

    public void setHouseSize(String houseSize) {
        this.houseSize = houseSize;
    }

    public Boolean getIfBooked() {
        return ifBooked;
    }

    public void setIfBooked(Boolean ifBooked) {
        this.ifBooked = ifBooked;
    }

    public String getPossessor() {
        return possessor;
    }

    public void setPossessor(String possessor) {
        this.possessor = possessor;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }
}
