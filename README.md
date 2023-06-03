---
title: "CRUD Operation - Springboot"
disqus: JBeanny
---

By: Yim Sotharoth

CRUD Operation - Springboot
===

## Table of Contents

[TOC]

## Installation

:::info
Go to https://start.spring.io/ to create the springboot project

The dependencies in used for this project: 
```xml!
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

Noted: I use Postgresql as my database that's why I need the postgres driver.
:::



## Folder structure

> the folder structure of this project: 

![](https://hackmd.io/_uploads/rkYCVVuUh.png)

___

## Let's start writing the configuration in application.yaml

```yaml!
server:
  port: 80

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/<YOUR-DATABASE>
    username: <YOUR-DATABASE-USERNAME>
    password: <YOUR-DATABASE-PASSWORD>
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

## Let's start writing code

### Entity
> Inside `entity` I created a file called `Student` with the following code: 


```java!
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
```
___


### Repository
> Inside `repository` I created a file called `StudentRepository` with the following code: 


```java!
package com.example.student_CRUD.repository;

import com.example.student_CRUD.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
}

```
___

### Service
> Inside `service` I created a file called `StudentService` with the following code: 


```java!
package com.example.student_CRUD.service;

import com.example.student_CRUD.entity.Student;
import com.example.student_CRUD.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    public void saveStudent(Student student){
        studentRepository.save(student);
    }
    
    public List<Student> getStudents(){
        return studentRepository.findAll();
    }
    
    public Student getStudentById(Long studentId) throws Exception {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new Exception("Student not found with ID: " + studentId));
        
    }
    
    public Student updateStudent(Long studentId, Student student) throws Exception {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        
        if(optionalStudent.isPresent()){
            Student existingStudent = optionalStudent.get();

            existingStudent.setGender(student.getGender());
            existingStudent.setName(student.getName());
            existingStudent.setMajor(student.getMajor());
            existingStudent.setIq(student.getIq());
            existingStudent.setGrade(student.getGrade());
            
            // Save the updated student
            studentRepository.save(existingStudent);
            return existingStudent;
        }else{
            throw new Exception("Student not found with ID: " + studentId);
        }
    }
    
    public void deleteStudent(Long studentId){
        studentRepository.deleteById(studentId);
    }
}
```
___

### Controller
> Inside `controller` I created a file called `StudentController` with the following code: 


```java!
package com.example.student_CRUD.controller;

import com.example.student_CRUD.entity.Student;
import com.example.student_CRUD.service.StudentService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    @GetMapping()
    public ResponseEntity<Object> getStudents() {
        Map<String,Object> response = new HashMap<>();
        
        try{
            List<Student> students = studentService.getStudents();
            response.put("status","success");
            response.put("result",students.size());
            response.put("students",students);
            
            return ResponseEntity.status(200).body(response);
            
        }catch(Exception ex) {
            response.put("status","fail");
            response.put("message",ex.getMessage());
            
            return ResponseEntity.status(500).body(response);
        }
    }
    
    @PostMapping()
    public ResponseEntity<Object> uploadStudent(@RequestBody Student student) {
        Map<String,Object> response = new HashMap<>();
        
        try{
            System.out.println(student.getIq());
            studentService.saveStudent(student);
            response.put("status","success");
            response.put("message","created successfully");
            
            return ResponseEntity.status(201).body(response);
            
        }catch(Exception ex) {
            response.put("status","fail");
            response.put("message",ex.getMessage());
            
            return ResponseEntity.status(500).body(response);
        }
    }
    
    @GetMapping("/{student_id}")
    public ResponseEntity<Object> getStudentById(@PathVariable Long student_id) {
        Map<String,Object> response = new HashMap<>();
        
        try{
            Student student = studentService.getStudentById(student_id);
            response.put("status","success");
            response.put("student",student);
            
            return ResponseEntity.status(200).body(response);
            
        }catch(Exception ex) {
            response.put("status","fail");
            response.put("message",ex.getMessage());
            
            return ResponseEntity.status(500).body(response);
        }
    }
    
    @PutMapping("/{student_id}")
    public ResponseEntity<Object> updateStudent(@PathVariable Long student_id, @RequestBody Student student) {
        Map<String,Object> response = new HashMap<>();
    
        try{
            Student updatedStudent = studentService.updateStudent(student_id,student);
            response.put("status","success");
            response.put("message","updated successfully");
            response.put("student",student);
        
            return ResponseEntity.status(200).body(response);
        
        }catch(Exception ex) {
            response.put("status","fail");
            response.put("message",ex.getMessage());
        
            return ResponseEntity.status(500).body(response);
        }
    }
    
    @DeleteMapping("/{student_id}")
    public ResponseEntity<Object> deleteStudent(@PathVariable Long student_id){
        Map<String,Object> response = new HashMap<>();
    
        try{
            studentService.deleteStudent(student_id);
            response.put("status","success");
            response.put("message","deleted successfully");
        
            return ResponseEntity.status(200).body(response);
        
        }catch(Exception ex) {
            response.put("status","fail");
            response.put("message",ex.getMessage());
        
            return ResponseEntity.status(500).body(response);
        }
    }
    
    
}
```
___


> Project Repository : https://github.com/metaphorlism/student-crud-springboot

### Contact Us

- :mailbox: yimsotharoth999@gmail.com
- [GitHub](https://github.com/metaphorlism) 
- [Facebook Page](https://www.facebook.com/Metaphorlism)
- [Instagram: Metaphorlism](https://www.instagram.com/metaphorlism/)
