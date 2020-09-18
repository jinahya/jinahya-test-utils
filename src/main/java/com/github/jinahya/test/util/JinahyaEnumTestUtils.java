package com.github.jinahya.test.util;

import java.util.Objects;

import static java.util.concurrent.ThreadLocalRandom.current;

public abstract class JinahyaEnumTestUtils {

    /**
     * Returns a randomly selected enum constant defined in specified enum class.
     *
     * @param enumClass the enum class.
     * @param <E>       enum type parameter
     * @return an randomly selected enum constant.
     */
    public static <E extends Enum<E>> E getRandomEnumConstant(final Class<E> enumClass) {
        final E[] enumConstants = Objects.requireNonNull(enumClass, "enumClazz is null").getEnumConstants();
        if (enumConstants.length == 0) {
            throw new IllegalArgumentException("zero-length enum constants from " + enumClass);
        }
        return enumConstants[current().nextInt(enumConstants.length)];
    }

    /**
     * Creates a new instance.
     */
    protected JinahyaEnumTestUtils() {
        super();
    }
}
