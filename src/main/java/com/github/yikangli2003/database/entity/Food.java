package com.github.yikangli2003.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @ManyToOne
    private User uploader;

    @Column(nullable = false)
    private boolean isPublic;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String servingType;

    @Column(nullable = false)
    private double servingSize;

    @Column(nullable = false)
    private double servingSizeUnit;

    @Column(nullable = false)
    private LocalDateTime localUploadTime;

    public Food(
            User uploader,
            boolean isPublic,
            String name,
            String servingType,
            double servingSize,
            double servingSizeUnit,
            LocalDateTime localUploadTime
    ) {
        this.uploader = uploader;
        this.isPublic = isPublic;
        this.name = name;
        this.servingType = servingType;
        this.servingSize = servingSize;
        this.servingSizeUnit = servingSizeUnit;
        this.localUploadTime = localUploadTime;
    }

    public String getId() {
        return id;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void openToPublic() {
        isPublic = true;
    }

    public User getUploader() {
        return uploader;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServingType() {
        return servingType;
    }

    public void setServingType(String servingType) {
        this.servingType = servingType;
    }

    public double getServingSize() {
        return servingSize;
    }

    public void setServingSize(double servingSize) {
        this.servingSize = servingSize;
    }

    public double getServingSizeUnit() {
        return servingSizeUnit;
    }

    public void setServingSizeUnit(double servingSizeUnit) {
        this.servingSizeUnit = servingSizeUnit;
    }

    public LocalDateTime getLocalUploadTime() {
        return localUploadTime;
    }
}
