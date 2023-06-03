package com.example.student_CRUD.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "students")
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "student_name")
    private String name;
    @Column(name = "student_gender")
    private String gender;
    @Column(name = "student_major")
    private String major;
    @Column(name = "student_grade")
    private String grade;
    @Column(name = "iq")
    private Integer iq;
}
