package com.example.spring.db;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 uni-directional link with Employee
 здесь departments - это SRC сущность (source table)
 Здесь есть информация о Employee, т.о. мы моделируем связь со стороны Department,
 не смотря на то, что FK содержится именно в Employee!


 [!] при моделировании связи One-to-Many (в варианте Uni-directional) на стороне src сущности
 в аннотации @JoinColumn 
 атрибут name будет ссылаться на FK из target-таблицы  
 
 [модель uni-direct связи]
 один Department (SRC) ко многим Employee (TARGET)
 связь прописывается со стороны Department в виде One-to-Many
 Department содержит коллекцию Employee
 коллекция Employee помечена @JoinColumn
 JoinColumn.name содержит имя столбца, хранящего FK: employees.department_id
    (потому, что на уровне базы связь хранится у юзера)
    получается, для конфигурирования мы используем Department,
    но сообщаем хибернейту, какое поле target-таблицы отвечает за связь в базе данных
    Конструкция @JoinColumn(name = "department_id") сохраняется,
    но перемещается из сущности Employee в Departament в варианте uni-direectional 
*/
@Entity
@Table(name = "departments")
public class DepartmentUni {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(name = "max_salary")
    private int maxSalary;

    @Column(name = "min_salary")
    private int minSalary;

    // One относится к текущей таблице, т.е к Department,
    // т.к. в одном отделе много пользователей
    // [!] для связей м и @OneToOne и @ManyToOne по дефолту стоит EAGER 
    //      Инфа о связанных сущностях загружается сразу, в основном запросе
    //      для @MOneToMany и @ManyToMany - LAZY, т.к. это грозит подгрузкой коллекций
    //      Инфа о связанныйх сущностях подгружается черезе дополнительные запросы по мере обращения к полям
    //      Соответсвенно, т.к. 1 отдел вмещает много людей, здесь оправдано использование LAZY
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            targetEntity = EmployeeUni.class
            // mappedBy = "department"  // это поле в сущности Employee, которое хранит объект Department
            // [!] в случае uni-direct связи - не используется
    )
    @JoinColumn(name = "department_id") // столбец target-таблицы employees, который содержит FK
    // [!] В случае bi-direct связи эта аннотация использовалась в сущности Employee
    private Set<EmployeeUni> employees;

    public DepartmentUni() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(int maxSalary) {
        this.maxSalary = maxSalary;
    }

    public int getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(int minSalary) {
        this.minSalary = minSalary;
    }

    public Set<EmployeeUni> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<EmployeeUni> employees) {
        this.employees = employees;
    }

    public void addEmployee(EmployeeUni user){
        if (employees == null)
            employees = new HashSet<>();

        employees.add(user);
    }
}
