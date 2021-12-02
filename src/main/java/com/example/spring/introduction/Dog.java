package com.example.spring.introduction;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
// @Primary - вариант решения, когда есть несколько имплементаций типа Pet. Указываем приоритетную -
// она будет использована, если какой-то бин требует автоматически встроить себе тип Pet
public class Dog implements Pet {
    public Dog() {
        System.out.println("Dog created");
    }

    @Override
    public void say() {
        System.out.println("Gav!");
    }
}
