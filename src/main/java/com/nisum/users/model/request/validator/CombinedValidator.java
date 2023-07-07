package com.nisum.users.model.request.validator;


import com.nisum.users.model.request.SignupRequest;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CombinedValidator implements ConstraintValidator<CombinedConstraint, SignupRequest> {

    private String[] fields;

    private final String MESSAGE_MAIL = "for mail, e.g.: mail@dominio.com";

    private final String MESSAGE_PASSWORD = "for password, this must contain contain upper and lower case letters, numbers and at least one special character.";

    @Value("${app.mailPattern.regexp}")
    String mailPattern;

    @Value("${app.passwordPattern.regexp}")
    String passwordPattern;

    @Override
    public void initialize(final CombinedConstraint combinedConstraint) {
        fields = combinedConstraint.fields();
    }

    @Override
    public boolean isValid(final SignupRequest signupRequest, final ConstraintValidatorContext context) {
        final BeanWrapperImpl beanWrapper = new BeanWrapperImpl(signupRequest);
        Boolean validated = false;


        for (final String f : fields) {
            final String fieldName = beanWrapper.getPropertyDescriptor(f).getName();
            final Object fieldValue = beanWrapper.getPropertyValue(f);
            final String value = (String) fieldValue;

            if (fieldName.equals("email")) {
                validated = value.matches(mailPattern);
                ((ConstraintValidatorContextImpl) context).addMessageParameter("value", MESSAGE_MAIL);
            } else if (fieldName.equals("password")) {
                validated = value.matches(passwordPattern);
                ((ConstraintValidatorContextImpl) context).addMessageParameter("value", MESSAGE_PASSWORD);
            }

            if (!validated) {
                return false;
            }
        }
        return validated;
    }
}
