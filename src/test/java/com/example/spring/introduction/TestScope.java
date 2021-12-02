package com.example.spring.introduction;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestScope {

    private final static ClassPathXmlApplicationContext context;

    static {
        // [!] можно юзать несколько конфиг-файлов
        // напр, в обоих файлах конфигаются одни и те же бины
        // Тогда, за чем будет приоритет (за аннотациями или xml), не понятно,
        // но как-то эти конфиги друг друга дополняют
        // Напр,
        // если в xml прописать scope кэта как prototype, а сам класс кэт пометить singleton,
        //      тест ниже создает в консоли 2 объекта Cat
        // если в xml прописать scope кэта как singleton, а сам класс кэт пометить prototype,
        //      тест ниже создает в консоли 3 объекта Cat
        // если в обоих местах prototype, то обектов 3
        // если в обоих местах singleton, то 1
        //      Однако, в этом случае в системе по-прежнему 2 объекта Cat c именами Chika (дефолтное значение name) и Zazoo
        //      - один создается при первичном анализе конфигурации (вероятно, xml)
        //      а другой - при встраивании зависимости в объект Person
        // [!] при использовании xml + аннотации конфигов дефолтное Person.name берется из xml
        // [!] в xml дефолтное животное Cat
        //      аннотациями задано по умолчанию Dog
        //      Соответсвенно, при комбинации конфигураций в персону по умолчанию встраивается Cat
        //      При отключении xml-конфига животным в person будет Dog
        // [!] При отключенном xml-конфиге создается один объект Cat (при scope = singleton)
        //      Если подключить xml, котов будет 2 (singleton в обоих конфигах)
        //      Это трики-кейс!
        //      Один кот берется из контекста - Zazoo
        //      Второй - встраивается в Person - Chika
        //      После отключении xml остаётся Zazoo. Он зинжектан в Person и он же лежит в контексте
        // [!] при использовании комбинированной конфигурации
        //      и двух вариантах метода destroy() - аннотированного и прописанного в xml
        //      оба метода вызываются на одном объекте cat - созданном на основе xml

        // [!] если раскоментать строку с подключением единственного конфига и закоментать подключение applicationContext.xml,
        // будет ошибка компиляции, т.к. в коде теста Person.Pet будет приводиться к Cat,
        // в то время, как Person.Pet = Dog
        context = new ClassPathXmlApplicationContext("./applicationContext.xml" , "appContextWithAnnotations.xml");
        // context = new ClassPathXmlApplicationContext("./appContextWithAnnotations.xml");
    }

    public static void main(String[] args) {

        // Если изменить scope бина Person на prototype,
        // и здесь его НЕ запрашивать,
        // то бин Person не будет создаваться автоматом при старте приложения -
        // только по требованию
        // с другой стороны, если в условиях scope = prototype запросить Person 2 раза,
        // будет создано 2 объекта Person
         Person person = context.getBean(Person.class);
         Person person2 = context.getBean(Person.class);
        // System.out.println(person); // Person@782859e для scope = prototype
        // System.out.println(person2); // Person@23f5b5dc для scope = prototype
        // System.out.println(person == person2); // false для scope = prototype и true для scope = singleton

        // [!] singleton бин создается автоматом при старте контейнера и является общим для всех клиентов бина

        // person.setName("Vasya"); // меняем состояние в одном инстансе, а запрашиваем из другого
        // System.out.println(person2.getName()); // 'Vasya' для Person scope = singleton

        // [!] singleton scope подходет для stateless объектов, которые не должны хранить какое-то изменяемое состояние

        // -----------
        // [!] prototype бины создаются только после явного обращения за ними к контейнеру
        // и после каждого такого обращения создается новый инстанс
        // Это подходит для stateful объектов - которые должны хранить како-то изменяемое состояние

        Cat pet1 = context.getBean("theCat", Cat.class);
        Cat pet2 = context.getBean("theCat", Cat.class);

        pet1.setName("Catty");
        pet2.setName("Zazoo");

        System.out.println(pet1.getName());

        System.out.println(person.getPet().getClass());
        System.out.println(person.getName());

        String name = ((Cat) person.getPet()).getName();
        String name2 = ((Cat) person2.getPet()).getName();

        System.out.println(name);
        System.out.println(name2);

        context.close();
    }
}
