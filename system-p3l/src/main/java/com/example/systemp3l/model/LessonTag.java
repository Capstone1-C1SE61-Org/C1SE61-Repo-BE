package com.example.systemp3l.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
public class LessonTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lessonTagId;
    private String lessonTagName;
}
