package com.github.jinahya.test.lang;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ResourceTests {

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
        try (InputStream resourceStream = ResourceTests.class.getResourceAsStream(name)) {
            assert resourceStream != null : "null resource stream from '" + name + "'";
            return function.apply(resourceStream);
        }
    }

    public static <U, R> R applyResourceStream(final String name,
                                               final BiFunction<? super InputStream, ? super U, ? extends R> function,
                                               final Supplier<? extends U> supplier)
            throws IOException {
        if (function == null) {
            throw new NullPointerException("function is null");
        }
        if (supplier == null) {
            throw new NullPointerException("supplier is null");
        }
        return applyResourceStream(name, s -> function.apply(s, supplier.get()));
    }

    public static void acceptResourceStream(final String name, final Consumer<? super InputStream> consumer)
            throws IOException {
        if (consumer == null) {
            throw new NullPointerException("consumer is null");
        }
        applyResourceStream(name, s -> {
            consumer.accept(s);
            return null;
        });
    }

    public static <U> void acceptResourceStream(final String name,
                                                final BiConsumer<? super InputStream, ? super U> consumer,
                                                final Supplier<? extends U> supplier)
            throws IOException {
        if (consumer == null) {
            throw new NullPointerException("consumer is null");
        }
        if (supplier == null) {
            throw new NullPointerException("supplier is null");
        }
        acceptResourceStream(name, s -> consumer.accept(s, supplier.get()));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    private ResourceTests() {
        super();
    }
}
