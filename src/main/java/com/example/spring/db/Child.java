package com.example.spring.db;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Transactional
@Table(name = "children")
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private String name;

    @Column
    private Integer age;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE, 
            CascadeType.REFRESH, 
            CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "child_section",
            joinColumns = @JoinColumn(name = "child_id"), // связь джоин-т с нашей таблицей
            inverseJoinColumns = @JoinColumn(name = "section_id") // связь джоин-т с т sections
    ) // та же запись идет в сущность Section
    private List<Section> sections;

    public Child(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public Child() {
    }

    public void addSection(Section section) {
        if (sections == null) {
            sections = new ArrayList<>();
        }

        sections.add(section);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    @Override
    public String toString() {
        return "Child{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
