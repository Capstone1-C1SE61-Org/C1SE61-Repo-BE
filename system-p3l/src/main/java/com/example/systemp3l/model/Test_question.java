package com.example.systemp3l.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
public class Test_question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer question_ID;
    private String correct_answer;
    private String question_content;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "test_id")
    private Test test;
}
