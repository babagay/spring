package com.example.spring.db;

import com.example.spring.config.AppConfig;
import com.example.spring.service.EmployeeService;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import java.util.Collection;
import java.util.Optional;

/**
 * [примеры]
 * http://java-online.ru/hibernate-sequence.xhtml
 */
class EmployeeTest {

    static Session session;

    private static final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    //private final static Session session = context.getBean(Session.class);
    // альтернативный вариант получения сессии
    private final static Session currentSession = context.getBean(Session.class, "currentSession");
    private final static SessionFactory sessionFactory = context.getBean(SessionFactory.class);
    private final static EntityManagerFactory entityManagerFactory = context.getBean(EntityManagerFactory.class);
    private final static EmployeeRepository employeeRepository = context.getBean(EmployeeRepository.class);
    private final static EmployeeService employeeService = context.getBean(EmployeeService.class);

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


//    @Autowired
//    private EmployeeRepository employeeRepository;

//    @Autowired
//    public EmployeeTest(EmployeeRepository _employeeRepository) {
//        employeeRepository = _employeeRepository;
//    }

    public static void main(String[] args) throws Exception {


        try {
            //getUserByIdTest();
            //getEmployeeByNameTest();
            //getDetailsEntityTest();
            //addEmployee();
            //testDao();
            //testRepository();
            //testCreate();
            //testCreate2();
            //dropUser();
            //dropUser2();
            //dropUser3();
            //dropUser4();
            //dropDetails();
            //getUserWithHQL();
            //getUserCollection();
            // testUpdate(); // НЕ заработал
            //testGetDetails();
            //getUserThroughDetailsTest();
            // createUserWithDetailsTest();
            // createUserWithDetailsTest2();
            // createUserWithDetailsTest3();
            //createUserWithDetailsTest4();
            //biDirectLinkTest();
            //existTest();

            //addUserToDepartmentTest();
            //getDepartmentTest();
            deleteDepartmentTest();

        }
        finally {
            System.out.println("== FINALLY ==");
            closeSession();
            context.close();
            System.out.println("== Session Factory closed");
        }
    }

    private static Session getSession() {

        if(session != null)
            return session;

        try {
            // это странно, но сессия не берется -
            // каждый раз она открывается
            session = sessionFactory.getCurrentSession();
            System.out.println("session = getCurrentSession()");
        }
        catch (HibernateException e) {
            session = sessionFactory.openSession();
            System.out.println("session = openSession()");
        }
        return session;
    }

    private static void closeSession(){
        if (session != null){
            Transaction transaction = session.getTransaction();
            if (transaction != null) {
                System.out.println("== Transaction commit: " + transaction);
                if(transaction.isActive()) transaction.commit();
            }
            session.close();
        }

        sessionFactory.close();
    }

    // Взять сущность + связанную сущность из другой таблицы
    private static void getUserByIdTest() {
        Session session = getSession();
        session.beginTransaction();

        // [A] взять сущность через сессию, используя стандартные методы сессии
        // Employee user = session.load(Employee.class, 13L); // так детали вытягиваются сразу же. Не смотря на то, что FetchType = lazy у поля Employee.details
        // Employee user = session.get(Employee.class, 13L); // так details вытягиваются сразу же

        // [B] LazyInitializationException, т.к. сессия успевает закрыться раньше, чем происходит обращение к деталям
        // одно из решений - OpenSessionInViewFilter
        // другое - отметить класс как @Transactional. Это было и так
        // третье - fetch=FetchType.EAGER - не годится
        // четвертое - JOIN FETCH
        // пятое - использовать нативный SQL с джоинами
        // шестое - enable_lazy_load_no_trans = true - уже и так было
        // седьмое - выполнить re-attach сессии. Помогло. Делается через session.update()
        // восьмое - постучаться к полю, хранящему сущность из другой таблицы, внутри сессии (транзакции?). Предстоит опробовать.

        // Employee user = employeeService.byId(13L); // через entityManager. Возникает исключение LazyInitializationException. could not initialize proxy - no Session
        // Employee user = employeeRepository.retrieveByName("German"); // Через кастомный метод репозитория. LazyInitializationException
        // Через сгенерированный метод репозитория. LazyInitializationException - could not initialize proxy EmployeeDetails - no Session
        Employee user = employeeService.bySurname("Titov"); // LazyInitializationException - could not initialize proxy EmployeeDetails - no Session

        System.out.println("---------------");
        System.out.println(user.getName() + " " + user.getSurname());

        session.update(user); // обновление юзера - защита от LazyInitializationException, возникающего при вызове getDetails()

        EmployeeDetails det = null;
        det = user.getDetails();
        System.out.println("===============");
        System.out.println("=== " + det);

        // Это можно не делать
        // Вообще, может возникнуть ERROR: Connection leak detected: there are 1 unclosed connections upon shutting down pool
        // Вероятно, сессия закрывается в другомместе...
        // Да, есть сообщение:
        //      Closing JPA EntityManagerFactory for persistence unit 'default'
        //      HHH000031: Closing
        // К тому же, такая ошибка возникает и из-за других вещей типа @GeneratedValue(strategy = GenerationType.AUTO)
        // - в этом случае явное закрытие сесси не поможет
        // session.getTransaction().commit();
        // session.close();
    }

    private static void getEmployeeByNameTest() throws Exception {

        //EmployeeTest test  = context.getBean("EmployeeTest", EmployeeTest.class);

        //Employee user = employeeRepository.retrieveByName("Zigar");

        //test.logger.info("Employee id 2 -> {}", user);

        //SessionFactory factory = HibernateUtils.getSessionFactory();

        // todo
        // если это заблочить, идет одна попытка создания фактори
        // Если взять бин из контекста (с закоменченным кодом ниже),
        // он берется, но с ошибкой Could not obtain transaction-synchronized Session for current thread
        // sessionFactory.openSession().load(Employee.class, 2L);
        // Настройки берутся из C:\projects\spring\src\main\resources\application.properties
        // и хибер конфигается спрингом втихую
        // ...получается,
        // это лишнее:
        // Session session = DataConfig.getCurrentSession();
        // Employee d = session.load(Employee.class, 1);

        // 1
        // [!] здесь будет Employee, завернутый в HibernateProxy
        Employee user = getSession().load(Employee.class, 2L);

        if (!user.getName().equals("Peter")) {
            throw new Exception("ERROR 1");
        }

        // 2
        // [!] здесь - чистый Employee
        Employee user2 = employeeRepository.retrieveByName("Zigar");

        if (!user2.getName().equals("Zigar")) {
            throw new Exception("ERROR 2");
        }

        // 3
        Employee user3 = employeeRepository.retrieveById(3L);

        if (!user3.getSurname().equals("Marakesh")) {
            throw new Exception("ERROR 3");
        }

    }

    /**
     * Сработало,
     * но после того, как добавил
     * '@GeneratedValue(strategy = GenerationType.IDENTITY)'
     * Иначе, была ошибка Invalid object name 'hibernate_sequence'
     * И, даже создание сиквенции вручную с указанием GenerationType.SEQUENCE не помогло
     */
    public static void addEmployee() throws Exception {
        Employee user = new Employee();
        user.setName("Sofia");
        user.setSurname("Blank");


        Session session = getSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();

        Employee userAdded = employeeRepository.retrieveByName("Sofia");

        if (userAdded != null && !userAdded.getSurname().equals("Blank")) {
            throw new Exception("ERROR");
        }

        Long id = user.getId();
        if (id == null || id <= 0 || !id.equals(userAdded.getId())) {
            throw new Exception("ERROR id");
        }
    }

    public static void testDao() throws Exception {
        Employee user = employeeService.byId(3);

        if (!user.getName().equals("Anna")) {
            throw new Exception("ERROR testDao()");
        }
    }

    public static void testRepository() throws Exception {
        Employee user = employeeService.bySurname("Cravitzer");

        if (!user.getName().equals("Peter")) throw new Exception("ERROR testRepository()");
    }

    public static void testCreate() {
        Employee user = new Employee();
        user.setName("Oynir");
        user.setSurname("Runduk");
        user.setDepartment(new Department());
        employeeService.save(user);
        System.out.println(user); // user содержит вновь присвоенный id
    }

    public static void testCreate2() throws Exception {
        Employee user = new Employee();
        user.setName("Jilly");
        user.setSurname("Idol");
        user.setDepartment(new Department());


        employeeService.create(user);

        if (user.getId() == null) {
            throw new Exception("ERROR");
        }
    }

    public static void testUpdate() {
        // 1
        Employee user = employeeService.byId(4);
        user.setSurname("ShiskinS");
        employeeService.update(user);

        // 2 не сработал
        Session session = getSession();
        session.beginTransaction();
        Employee us = employeeService.byId(6);
        us.setSurname("Blankesh");
        session.getTransaction().commit();

        Optional<Employee> u = employeeService.getById(4);
        u.ifPresent(System.out::println);
    }

    // [!] НЕ удалил связанную запись из details
    public static void dropUser() {
        employeeService.dropById(14L);
    }

    // [!] удалил связанную запись из details (через entity manager)
    public static void dropUser2() {
        Employee user = employeeService.bySurname("Croff");
        employeeService.dropEntity(user);
    }

    // удалит ли связанные данные из т details? НЕТ
    // Берем юзера НЕ через сессию, а через сервис и репозиторий
    // Возникает ошибка  Removing a detached instance com.example.spring.db.Employee#13
    public static void dropUser3() {
        Session session = getSession();
        session.beginTransaction();
        Employee user = employeeService.bySurname("Carimov");
        // session.update(user); // [!] при добавлении апдейта всё работает и данные удаляются из обоих таблиц
        session.delete(user);
        session.getTransaction().commit();
    }

    // удалит ли связанные данные из т details? ДА
    // Берем юзера через сессию
    // данные удалены из обоих т - Employee, Details
    public static void dropUser4() {
        Session session = getSession();
        session.beginTransaction();
        Employee user = session.get(Employee.class, 13L);
        session.delete(user);
        session.getTransaction().commit();
    }

    public static void getUserWithHQL() {
        employeeService.getById(12)
                .map(u -> {
                    System.out.println("== " + u.getName() + " got");
                    return u;
                })
                .ifPresent(System.out::println);
    }

    public static void getUserCollection() {
        Collection<Employee> users = employeeService.all();
        System.out.println(users);
    }

    /**
     * взять детали, которые хранятся в другой таблице
     * По умолчанию возникает LazyInitializationException: could not initialize proxy - no Session
     * Решение: https://www.baeldung.com/hibernate-initialize-proxy-exception
     */
    static void testGetDetails() {
        employeeService.getById(1)
                .map(u -> {
                    System.out.println(u.getName());
                    return u;
                })
                .map(Employee::getDetails)
                .ifPresent(d -> {
                    System.out.println(d.getCity());
                });

    }

    static void getUserThroughDetailsTest() {
        EmployeeDetails details = employeeService.detailsById(106L);
        System.out.println(details.getUser());
    }

    /**
     * SQLGrammarException: error performing isolated work
     */
    static void createUserWithDetailsTest() throws Exception {

        Employee user = new Employee();
        user.setName("Chingiz");
        user.setSurname("Itmatov");
        user.setDepartment(new Department());

        EmployeeDetails details = new EmployeeDetails();
        details.setCity("Almata");
        details.setEmail("chingiz");
        details.setPhoneNumber("77-88-993");
        user.setDetails(details);

        //employeeService.save(user);

        // не работало, когда использовалась секвенция в первичном ключе details
        // PersistenceException: org.hibernate.exception.SQLGrammarException: error performing isolated work
        // видимо, сиквенция не работает
        // EmployeeDetails details = new EmployeeDetails();
        // details.setEmail("foo@dd");
        // details.setPhoneNumber("333");
        // details.setCity("City");
        // employeeService.createDetails(details);

        Session session = getSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();

        if (user.getId() == null) {
            throw new Exception("ERROR");
        }
    }

    static void createUserWithDetailsTest2() throws Exception {

        Employee user = new Employee();
        user.setName("German");
        user.setSurname("Titov");
        user.setDepartment(new Department());

        EmployeeDetails details = new EmployeeDetails();
        details.setCity("Habarovsk");
        details.setEmail("foo");
        details.setPhoneNumber("77-88-993");
        user.setDetails(details);

        Employee usr = employeeService.create(user);

        System.out.println(usr);
    }

    // добавляем юзера с деталями (связанная таблица)
    // тестировалось на uni-directional связи Employee и Details
    // Здесь мы сохраняем сущность Employee, т.е. хранителя связи
    static void createUserWithDetailsTest3() throws Exception {

        Employee user = new Employee();
        user.setName("Chevald");
        user.setSurname("Croff");

        EmployeeDetails details = new EmployeeDetails();
        details.setCity("Oslo");
        details.setEmail("bar");
        details.setPhoneNumber("77-88-998");
        user.setDetails(details);

        Employee usr = employeeService.save(user);

        System.out.println(usr);
    }

    // тестируем после того, как связь таблиц стала Bi-directional
    // Здесь мы так же создаем 2 объекта,
    // но вкидываем в них линки друг на друга
    // и сохраняем Details,
    // т.е. НЕ ту сущность, которая является хранителем связи, а наоборот, зависимую
    static void createUserWithDetailsTest4() throws Exception {

        Employee user = new Employee();
        user.setName("Chevald");
        user.setSurname("Croff");
        user.setDepartment(new Department());

        EmployeeDetails details = new EmployeeDetails();
        details.setCity("Oslo");
        details.setEmail("bar@tovaridh.pesnya");
        details.setPhoneNumber("77-88-99888");
        details.setUser(user);

        user.setDetails(details); // [!] юзеру устанавливаем детали, а в детали кидаем ссылку на юзера

        employeeService.save(details);
        // System.out.println(usr);
    }

    // [!] для взятия сущности используется заинжэктанный в даошку entity manager
    // Благодаря добавлению поля Employee user, помеченного @OneToOne, в EmployeeDetails классе
    // employee и details ОБА знают друг о друге
    // ТОГДА, можно через details влиять на employee
    // Как минимум, можно получить его через details
    // [!] почему-то Employee выгребается даже, если нет обращения getUser()
    static void biDirectLinkTest() {
        EmployeeDetails details = employeeService.detailsById(101L);
        System.out.println("== " + details + " " + details.getUser());
    }

    // Проверка существования записи с заданным набором полей
    // Использован автосгенеренный метод репозитория: exists(Example<S> example)
    static void existTest() {
        EmployeeDetails d = new EmployeeDetails();
        d.setEmail("chingiz");
        boolean r = employeeService.exist(d);

        System.out.println("== details exists: " + r);
    }

    // [!] Использован бин EntityManagerFactory, переопределенный DataConfig
    static void getDetailsEntityTest() {
        EmployeeDetails dt = entityManagerFactory.createEntityManager().find(EmployeeDetails.class, 103L);
        System.out.println("== " + dt);

        System.out.println("=============");
        System.out.println(currentSession.equals(getSession())); // FALSE т.е. это 2 разных объекта - 2 сессии
        System.out.println(currentSession.equals(currentSession)); //  TRUE

        // UnknownEntityTypeException: Unable to locate persister: com.example.spring.db.EmployeeDetails
        // [!] это было вылечено путем добавления сущностей в currentSession бин через вызов addAnnotatedClass(Employee.class)

        // EmployeeDetails d = currentSession.get(EmployeeDetails.class, 103L);
        // System.out.println("== " + d);
    }

    /**
     * [!] для взятия сущности использован бин Session
     *
     * Удаляем детали
     * Связанный юзер должен остаться
     * в случае, если со стороны EmployeeDetails.user выставлена связь cascade = {CascadeType.PERSIST, CascadeType.REFRESH}
     * (!) Почему-то hibernate завалил запись details, не смотря на то, что в Employee осталась ссылка на эту запись
     *     И после этого, при попытке взять юзера, ссылающегося на данную запись из details, возникло исключение
     *     No row with the given identifier exists: [EmployeeDetails#106]
     * Т.о. если уже мы контролируем целостность данных самостоятельно, надо сперва выносить 106 details_id в соответсвующей записи Employee
     * (!) Если выставить CascadeType.ALL,
     *      пользователь будет снесен вместе с деталями
     *      и при попытке взять его будет исключение No row with the given identifier exists: [EmployeeDetails#107]
     *      Т.е. юзер опять остался! Т.е. код НЕ СРАБОТАЛ. hiber разрывает целостность данных
     *      Оказалось, у т employees отсутсвовал CONSTRAINT
     *      После его восстановления все заработало - юзер был удален каскадно
     * Но тут другая проблема.
     * Даже, если поставить cascade = {CascadeType.PERSIST, CascadeType.REFRESH},
     * срабатывает ограничение на уровне базы
     * и юзер выносится все-равно
     * [!] инетересно как сделать так чтобы
     *      при удалении деталей удалялась запись из details
     *      и при этом Employee.details_id сбрасывался в NULL, но сам юзер оставался
     */
    static void dropDetails() {
        // step 1
        employeeService.dropDetails(101L);

        // step 2
        Employee user = currentSession.get(Employee.class, 8L);

        System.out.println("=================");
        System.out.println(user);

        currentSession.close();
    }

    static void addUserToDepartmentTest(){
        Department department = new Department();
        department.setMaxSalary(800);
        department.setMinSalary(100);
        department.setName("New2");

        // Employee user = currentSession.get(Employee.class, 7);

        Employee user = new Employee();
        user.setName("Chevald2");
        user.setSurname("Croff2");
        // user.setDepartment(department); // [!] даже без этого работает вставка сперва в departments потом в Employee

        Employee user2 = new Employee();
        user2.setName("Elen2");
        user2.setSurname("Smirnoff2");
        // user2.setDepartment(department); // без этого работает вставка в обе таблицы

        department.addUserToDepartment(user); // без этого вставка юзеров НЕ происходит. Создается только отдел
        department.addUserToDepartment(user2);

        currentSession.save(department);
    }

    static void getDepartmentTest(){
        //getSession(); // инициализация сессии. Нужно, если используем session поле
        currentSession.beginTransaction(); // работает и без этого
        currentSession.setFlushMode(FlushModeType.AUTO);

        // A - берем юзеров, связанных с отделом
        // поле Department.employees помечено LAZY,
        // но здесь ошибка НЕ возникает
        Department dep = currentSession.get(Department.class, 5L);

        System.out.println("== [" + dep.getName() + "]");
        System.out.println("== Employees: " + dep.getEmployees());

        // Б - берем отдел, связанный с юзером
        // поле Employee.Department помечено LAZY
        Employee user = employeeService.byId(6L); // LazyInitializationException: could not initialize proxy - no Session
        // Employee user = currentSession.get(Employee.class, 6L); // [!] так LazyLoad exception НЕ возникает

        currentSession.update(user); // во избежание ошибки could not initialize proxy - no session spring data jpa
        // делается update(user), чтобы сохранить сессию
        // https://coderedirect.com/questions/177229/lazyinitializationexception-could-not-initialize-proxy-no-session
        System.out.println("== Dep: " + user.getDepartment().getName());

        currentSession.getTransaction().commit(); // работает и без этого

        // [!] как это решается в СЗ
        // Но там все работает через сессию (причем, через один и тот же объект)
        // А здесь можно делать запросы через разные объекты сессии, entityManager и репозиторий
        //      что юзает под капотом Repository, понять сложно
        //      TransactionManagerProvider.getTransactionManager().checkAndStartTrans
        //          под капотом: session.setFlushMode(FlushMode.AUTO)
        //
        //		task.execute(HibernateUtil.getSessionFactory().getCurrentSession())
        //
        //		TransactionManagerProvider.getTransactionManager().commitTransaction
        //          под капотом: session.getTransaction().commit()
    }

    // Тестирование удаления сущностей при Bi-directional свящи Many-to-One
    // Удалится ли департамент юзера и все юзеры, входящие в этот отдел?
    // Да
    // По цепочке удалился юзер, связанный отдел и все связанные с отделом пользователи
    // Т.к. стоит связь cascade ALL
    // [!] Если убрать cascade ALL, заменив ее на безопасный набор вариантов, на одном из концов связи (Employee),
    // тогда, при удалении юзера его отдел остаётся и юзеры этого отдела остаются
    // (мы заменили в сущности Employee - оконечности связи Many, хранятеля FK - каскадность поля типа Department
    // с CascadeType.ALL на {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
    // хотя, при этом каскадность у поля типа Set<Employee> employees у сущности Department стояла CascadeType.ALL, т.е со стороны One)
    static void deleteDepartmentTest(){
        currentSession.beginTransaction();
        currentSession.setFlushMode(FlushModeType.AUTO);

        Employee user = currentSession.get(Employee.class, 20L);
        currentSession.delete(user);

        currentSession.getTransaction().commit();
    }
}