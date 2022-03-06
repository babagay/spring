package com.example.spring.db;

import com.example.spring.config.WebConfig;
import org.hibernate.Session;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Optional;

public class ChildrenTest {

    private static final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebConfig.class);
    private final static Session currentSession = context.getBean(Session.class, "currentSession");
    private final static ChildRepository childRepository = context.getBean(ChildRepository.class);
    private final static SectionRepository sectionRepository = context.getBean(SectionRepository.class);

    public static void main(String[] args) {
//        test();
//        test1();
//        test2();
//        test3();
//        test4();
//        test5();
//        test6();
        test7();
    }

    private static void test() {

        Child kid = new Child("John Doe", 15);
        currentSession.beginTransaction();
        currentSession.save(kid);
        currentSession.getTransaction().commit();
        currentSession.close();
    }
    
    /**
     * Создать секцию
     * Добавить ребят
     */
    private static void test1() {
        Section section = new Section("Volleyball");

        Child kid1 = new Child("Darina", 15);
        Child kid2 = new Child("Grisha", 15);

        section.addChild(kid1);
        section.addChild(kid2);

        currentSession.beginTransaction();
        
        // currentSession.persist(kid1); 
        // [!] надо добавить сконструированную сущность в persistence context, т.е.
        // перевести её из состояния transient в persistent
        //  и сущность становится персистентной
        // Это нужно, если в сущности указан тип связи, отличный от CascadeType.ALL -
        // В этом случае, как раз, операция начинает каскадироваться по типу ALL
        // Если же сущность в состоянии detached, будет исключение (сразу либо на момент коммита сессии)
        // [!] для использования этого метода д.б. включён CascadeType.PERSIST
        // [!] того же самого можно достичь, вызвав этот метод один раз на секции - persist(section)
        //  вместо persist(kid1) persist(kid2) и save(section)
        // currentSession.persist(kid2);

        // currentSession.save(section);        
        currentSession.persist(section);

        currentSession.getTransaction().commit();
        currentSession.close();

        System.out.println("DONE");
    }

    /**
     * Создать секцию
     * Добавить в нее существующего ребенка
     */
    private static void test2() {
        Section section = new Section("Dance");

        Child Eli = new Child("Vladislav", 4);
        section.addChild(Eli);

        currentSession.beginTransaction();

        Child Arina = currentSession.get(Child.class, 5);
        section.addChild(Arina);

        currentSession.save(section);

        currentSession.getTransaction().commit();
        currentSession.close();

        System.out.println("DONE");
    }

    /**
     * Взять секции, которые посещает ребенок
     */
    private static void test3() {
        Child Arina = currentSession.get(Child.class, 5);
        System.out.println(Arina.getSections());
        System.out.println("DONE ===========");
    }

    /**
     * Добавить Football
     * Взять существующую секцию
     * Взять детей
     * Присоединить детей к старой и новой секциям 
     */
    private static void test4() {
        currentSession.beginTransaction();
        Section sectionF = new Section("Football");
                
        Section section = currentSession.get(Section.class, 5);
        Child kid = currentSession.get(Child.class, 8);
        Child kid2 = currentSession.get(Child.class, 6);
        
        section.addChild(kid);
        section.addChild(kid2);
        
        sectionF.addChild(kid);
        
        //currentSession.update(section); // НЕ работает добавление связи
        //currentSession.save(kid2); // НЕ работает
        //currentSession.persist(section); // НЕ работает. detached entity passed to persist
        //currentSession.merge(section); // detached entity passed to persist
        currentSession.saveOrUpdate(section); // [!] сработало
        
        // sectionRepository.saveAndFlush(section); // [!] так сработало, потом НЕ сработало: Violation of PRIMARY KEY constraint 'PK__child_se__BEDEFA73C46B5F5F'. Cannot insert duplicate key in object 'db_accessadmin.child_section'. The duplicate key value is (7, 5).
        // sectionRepository.save(sectionF); // detached entity passed to persist
        currentSession.saveOrUpdate(sectionF); // [!] сработало

        currentSession.getTransaction().commit();
        currentSession.close();
        System.out.println("DONE");
    }

    /**
     * Взять всех детей, посещающих секцию
     * Протестировтаь сохранение стандартными средствами репозиотрия
     */
    private static void test5() {
        Section section1 = currentSession.get(Section.class, 4);
        System.out.println("_acro_ " + section1.getName() + " " + section1.getChildren());
        Section section2 = currentSession.get(Section.class, 5);
        System.out.println("_dance_ " + section2.getName() + " "  + section2.getChildren());
//        Section section3 = currentSession.get(Section.class, 3);
//        System.out.println("_foot_ " + section3.getName() + " " + section3.getChildren());

        Optional<Child> child = childRepository.findById(5);
        child.ifPresent(c -> {
            System.out.println(c.getName());
             c.setName("Ariana Panova");
             childRepository.save(c); // OK
        });
      
        System.out.println("DONE");
    }

    /**
     * Удалить секцию
     * Адекватно работает только при отсутсвии CascadeType.ALL
     */
    private static void test6() {
        Section section = currentSession.get(Section.class, 11); // Football
        System.out.println(section.getChildren()); // есть люди, посещающие футбол

        currentSession.beginTransaction();
        
        currentSession.delete(section);  // удалило ВСЁ из всех таблиц

        currentSession.getTransaction().commit();
        currentSession.close();
        
        //sectionRepository.delete(section); // так удалило ВСЁ из всех таблиц. Это из-за каскада CascadeType.ALL
        System.out.println("DONE");
        
        // В хибернате CascadeType.ALL работает иначе, чем в базе - оно попросту вытирает ВСЕ связанные сущности,
        // с какого бы конца ты ни начал удалять
        // В то же время, после замены CascadeType.ALL на 
        //            CascadeType.PERSIST,
        //            CascadeType.MERGE,
        //            CascadeType.REFRESH,
        //            CascadeType.DETACH
        // перестал работать код добавления связанных записей из test1()
        // с ошибкой IllegalStateException: Session/EntityManager is closed
        // хотя,по сути, мы просто убрали CascadeType.REMOVE из списка
        // Решение - сделать сохраняемые сущности персистентными через вызов persist()
    }

    /**
     * Удлаение юзера
     * При отсутсвии связи  CascadeType.ALL юзер удаляется, а секция остается, в которую он ходил
     * Удаляется сначала связь из т child_section, потом сущность из т children
     */
    private static void test7() {
        currentSession.beginTransaction();
        Child kid = currentSession.get(Child.class, 11);
        currentSession.delete(kid);
        currentSession.getTransaction().commit();
        currentSession.close();
        System.out.println("DONE");
    }
}
