package com.example.spring.aop;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class University {

    private final List<Student> students = new ArrayList<>();

    public University() {
        addStudent();
    }

    public void addStudent(){
        students.add(new Student("Lex Ivanov", 1, 7.5));
        students.add(new Student("Nikoly", 2, 7.0));
        students.add(new Student("Peter", 3, 5.0));
    }

    public List<Student> getStudents(){
        System.out.println("JOB: University.getStudents() before adding item ..." + students);

        students.add(new Student("Hatico", 4, 5.5));

        return students;
    }

    // метод возбуждает исключение
    public String getStudentNameWithException() throws StudentException {
        String name = null;
        System.out.println("JOB: University.getStudentNameWithException()");
        students.clear();
        try {
            name = students.get(0).getName();
        } catch (Exception e){
            StudentException ex = new StudentException();
            ex.setMess("student not found");
            throw ex;
        }

        return name;
    }

    public Student getStudent(){
        Student st = students.get(1);
        System.out.println("JOB: University.getStudent(). Name: " + st.getName() + ". Grade: " + st.getAvgGrade());
        return st;
    }
}
