package com.github.jinahya.test.validation;

import lombok.extern.slf4j.Slf4j;

import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.MessageInterpolator;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Constants and utilities for Bean-Validation.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
public final class BeanValidationTests {

    // -----------------------------------------------------------------------------------------------------------------
//    public static final ValidatorFactory VALIDATION_FACTORY = Validation.buildDefaultValidatorFactory();

    // https://stackoverflow.com/a/54750045/330457
    private static Configuration messageInterpolator(final Configuration configuration) {
        try {
            final Class<?> c = Class.forName(
                    "org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator");
            configuration.messageInterpolator((MessageInterpolator) c.getConstructor().newInstance());
        } catch (final ReflectiveOperationException roe) {
            roe.printStackTrace();
        }
        return configuration;
    }

    /**
     * An instance of {@link ValidatorFactory} to be used in testing.
     * @see Validation#byDefaultProvider()
     */
    public static final ValidatorFactory TEST_VALIDATION_FACTORY
            = messageInterpolator(Validation.byDefaultProvider().configure()).buildValidatorFactory();

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Applies an instance of {@link Validator} created from {@link #TEST_VALIDATION_FACTORY} to specified function and
     * returns the result.
     *
     * @param function the function to be applied.
     * @param <R>      result type parameter
     * @return the result of the function.
     */
    public static <R> R applyValidator(final Function<? super Validator, ? extends R> function) {
        return requireNonNull(function, "function is null").apply(TEST_VALIDATION_FACTORY.getValidator());
    }

    public static <U, R> R applyValidator(final BiFunction<? super Validator, ? super U, ? extends R> function,
                                          final Supplier<? extends U> supplier) {
        if (function == null) {
            throw new NullPointerException("function is null");
        }
        if (supplier == null) {
            throw new NullPointerException("supplier is null");
        }
        return applyValidator(v -> function.apply(v, supplier.get()));
    }

    public static void acceptValidator(final Consumer<? super Validator> consumer) {
        if (consumer == null) {
            throw new NullPointerException("consumer is null");
        }
        applyValidator(v -> {
            consumer.accept(v);
            return null;
        });
    }

    public static <U> void acceptValidator(final BiConsumer<? super Validator, ? super U> consumer,
                                           final Supplier<? extends U> supplier) {
        if (consumer == null) {
            throw new NullPointerException("consumer is null");
        }
        if (supplier == null) {
            throw new NullPointerException("supplier is null");
        }
        acceptValidator(v -> consumer.accept(v, supplier.get()));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Validates specified bean.
     *
     * @param object the bean to be validated; must be not {@code null}.
     * @param <T>    bean type parameter
     * @return a set of constraint violations; {@code empty} if valid.
     */
    public static <T> Set<ConstraintViolation<T>> validate(final T object) {
        if (object == null) {
            throw new NullPointerException("object is null");
        }
        return applyValidator(v -> v.validate(object));
    }

    /**
     * Checks whether specified bean is valid.
     *
     * @param object the bean to be checked.
     * @return {@code true} if {@code object} is {@code null} or the result of {@link #validate(Object)} method with
     * {@code object} is empty.
     */
    public static boolean isValid(final Object object) {
        if (object == null) {
            return true;
        }
        return validate(object).isEmpty();
    }

    /**
     * Checks whether specified is valid and throws a constraint violation exception if not.
     *
     * @param object the bean to be validated; may be {@code null}.
     * @param <T>    bean type parameter
     * @return specified bean.
     */
    public static <T> T requireValid(final T object) {
        if (object == null) {
            return null;
        }
        final Set<ConstraintViolation<T>> violations = validate(object);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return object;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    private BeanValidationTests() {
        super();
    }
}