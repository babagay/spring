package com.example.spring.mvc.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Кастомная аннотация, задача которой - валидировать email
 * Расширяет плеяду аннотаций пакета javax.validation.constraints
 *      В т.ч. моделирует функционал javax.validation.constraints.Email
 * Тот факт, что это именно валидирующая аннотация обозначается тем, что интерфейс помечен 
 *      декораторомм @Constraint
 *      Подозреваю, что вся магия аннотаций вкидывается в этот интерфейс именно благодаря пометке @Constraint,
 *      иначе, всё пришлось бы делать вручную, т.е. это читерское создание кастомной [узкоспециализированной] аннотации
 */
@Target(value = {ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
public @interface EmailCustom {
    
    String value() default "\\w{2,12}@\\w{1,12}\\.\\w{2,4}"; // шаблон проверки по умолчанию
    String message() default "Email is not valid";
    
    // [!] стандартные методы, которые должна содержать подобная аннотация, чтобы все работало
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
