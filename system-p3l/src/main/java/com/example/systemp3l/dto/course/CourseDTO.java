package com.example.systemp3l.dto.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    private Integer courseId;
    private String courseName;
    private Integer coursePrice;
    private String image;
    private Boolean status;
    private Date createDate;
    private Date updateDate;
    private Integer instructorId;
}
