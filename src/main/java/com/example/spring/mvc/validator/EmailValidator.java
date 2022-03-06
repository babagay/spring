package com.example.spring.mvc.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Кастомный валидатор, который процессит [строковые] данные, захваченные аннотацией EmailCustom
 */
public class EmailValidator implements ConstraintValidator<EmailCustom, String> {

    private String emailValidationPattern;

    @Override
    public void initialize(EmailCustom validatorAnnotation) {
        emailValidationPattern = validatorAnnotation.value();
    }

    @Override
    public boolean isValid(String checkedValue, ConstraintValidatorContext context) {
        try {
            return checkedValue.matches(emailValidationPattern);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
