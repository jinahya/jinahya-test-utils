package com.github.jinahya.test.lang;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

public final class ClassTestUtils {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Applies a stream of specified resource to specified function and returns the result.
     *
     * @param name     the resource name to open.
     * @param function the function to be applied with the resource stream.
     * @param <R>      result type parameter.
     * @return the result of the function
     * @throws IOException if an I/O error occurs.
     */
    public static <R> R applyResourceStream(final String name,
                                            final Function<? super InputStream, ? extends R> function)
            throws IOException {
        if (name == null) {
            throw new NullPointerException("name is null");
        }
        if (function == null) {
            throw new NullPointerException("function is null");
        }
        try (InputStream resourceStream = ClassTestUtils.class.getResourceAsStream(name)) {
            assert resourceStream != null : "null resource stream from '" + name + "'";
            return function.apply(resourceStream);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    private ClassTestUtils() {
        super();
    }
}
