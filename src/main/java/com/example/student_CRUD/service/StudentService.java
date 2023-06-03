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
