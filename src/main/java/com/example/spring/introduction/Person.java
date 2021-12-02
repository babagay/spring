package com.example.spring.introduction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Component("thePerson") // дефолтный beanId будет: person. Лучше переопределять имя бина
public class Person {

    // [!] можно говорить спрингу, чтобы он внедрял бин через поле. Нежелательный тип внедрения
    // @Autowired
    // @Qualifier("theCat") // вариант решения проблемы, когда есть несколько имплементаций типа Pet. Указываем конкретную с помощью Qualifier
    //                         другой вариант - использовать @Primary в одной из имплементаций
    private Pet pet;

    @Value("${person.nameAlternative}") // задаём значение по умолчанию, Тянем его из application.properties
    private String name;

    @Value("2")
    private int age;

    // [!] второй вариант внедрения - через конструктор
    @Autowired // можно аннотировать весь конструктор. При этом можно оставить дефолтный конструктор (без параметров)
    public Person(
            // @Autowired // - можно аннотировать параметр. Но, тогда, надо убрать конструктор без параметров
            @Qualifier("dog")
                    Pet _pet
                 ) {
        System.out.println("Person created via parametrized constructor");
        pet = _pet;
    }

    public Person() {
        System.out.println("Person created via default constructor");
    }

    // Срабатывает при генерации объекта; после того, как отработает конструктор
    // В случае Person scope = prototype
    //      вызывается каждый раз при запросе объекта Person у контекста, т.е. для каждого вновь созданного бина
    public void init(){
        System.out.println("Person Init method works");
    }

    // Lifecycle hook
    // срабатывает после вызова context.close()
    //      в случае Person scope = singleton
    // модификатор доступа м.б. любым
    // название м.б. любым
    // сигнатура д.б. без параметров
    // return type лучше void
    // [!] если Person scope = singleton,
    //      вызывается 1 раз, не зависимо от количества раз, когда мы запрашиваем у контекста объект Person
    // В случае Person scope = prototype метод НЕ вызывается
    //      Это значит, что необходимо самому писать код постобработки, например, освобождения ресурсов
    public void destroy(){
        System.out.println("Person Destroy method works");
    }

    // [!] третий вариант внедрения - через сеттер
    // Можно раскомментать все 3 варианта, но будет использован вариант через сеттер
    // @Autowired
    // @Qualifier("theCat") // @Qualifier - чтоб указать [через beanId] конкретную имплементацию интерфейса Pet
    public void setThePet(Pet pet) {
        System.out.println("Pet was set. (" + pet.getClass() + ")");
        this.pet = pet;
    }

    public Pet getPet(){
        return pet;
    }

    public void callYourPet(){
        System.out.println("Hellow petty!");
        pet.say();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
