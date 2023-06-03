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
