package com.example.spring.db;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "details")
@Transactional
public class EmployeeDetails {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detailsSeq")
    // @SequenceGenerator(name = "detailsSeq", sequenceName = "SEQ_DETAILS", initialValue = 100, allocationSize = 1)
    private Long id;

    @Column(name = "city")
    private String city;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    // Для bi-directional связи
    @OneToOne(
            targetEntity = com.example.spring.db.Employee.class,
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            // название поля в классе Employee, хранящего связь с этой сущностью
            // По сути, этот атрибут говорит, что связь между сущностями уже сконфигурирована -
            // аннотациями поля Employee.details
            mappedBy = "details"
    )
    private Employee user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Employee getUser() {
        return user;
    }

    public void setUser(Employee user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "EmployeeDetails{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
