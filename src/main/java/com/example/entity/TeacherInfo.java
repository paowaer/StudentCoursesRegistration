package com.example.entity;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "teacher_info")
public class TeacherInfo extends Account {
    @Column(name = "title")
    private String title;

    @Column(name = "majorId")
    private Long majorId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getMajorId() {
        return majorId;
    }

    public void setMajorId(Long majorId) {
        this.majorId = majorId;
    }
}
