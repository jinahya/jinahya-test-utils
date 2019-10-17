package com.github.jinahya.test.fasterxml.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.github.jinahya.test.lang.ResourceTests.applyResourceStream;
import static java.util.Objects.requireNonNull;

@Slf4j
public final class JacksonTests {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A shared instance of object mapper.
     */
    public static final ObjectMapper TEST_OBJECT_MAPPER = new ObjectMapper(); // fully thread-safe!

    // -----------------------------------------------------------------------------------------------------------------
    public static <R> R applyObjectMapper(final Function<? super ObjectMapper, ? extends R> function) {
        return function.apply(TEST_OBJECT_MAPPER);
    }

    public static <U, R> R applyObjectMapper(final BiFunction<? super ObjectMapper, ? super U, ? extends R> function,
                                             final Supplier<? extends U> supplier) {
        return applyObjectMapper(v -> function.apply(v, supplier.get()));
    }

    public static void acceptObjectMapper(final Consumer<? super ObjectMapper> consumer) {
        applyObjectMapper(v -> {
            consumer.accept(v);
            return null;
        });
    }

    public static <U> void acceptObjectMapper(final BiConsumer<? super ObjectMapper, ? super U> consumer,
                                              final Supplier<? extends U> supplier) {
        acceptObjectMapper(v -> consumer.accept(v, supplier.get()));
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static JsonNode readTreeFromResource(final ClassLoader classLoader, final String resourceName)
            throws IOException {
        return applyResourceStream(classLoader,
                                   resourceName,
                                   s -> applyObjectMapper(m -> {
                                       try {
                                           return m.readTree(s);
                                       } catch (final IOException ioe) {
                                           throw new RuntimeException(ioe);
                                       }
                                   })
        );
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static <T> T readValueFromResource(final ClassLoader classLoader, final String resourceName,
                                              final Class<? extends T> valueClass)
            throws IOException {
        return applyResourceStream(classLoader,
                                   resourceName,
                                   s -> applyObjectMapper(m -> {
                                       try {
                                           return m.readValue(s, valueClass);
                                       } catch (final IOException ioe) {
                                           throw new RuntimeException(ioe);
                                       }
                                   })
        );
    }

    public static <T> T readValueFromResource(final ClassLoader classLoader, final String resourceName,
                                              final JavaType javaType)
            throws IOException {
        return applyResourceStream(classLoader,
                                   resourceName,
                                   s -> applyObjectMapper(m -> {
                                       try {
                                           return m.readValue(s, javaType);
                                       } catch (final IOException ioe) {
                                           throw new RuntimeException(ioe);
                                       }
                                   })
        );
    }

    public static <T> T readValueFromResource(final ClassLoader classLoader, final String resourceName,
                                              final TypeReference<? extends T> typeReference)
            throws IOException {
        return applyResourceStream(classLoader,
                                   resourceName,
                                   s -> applyObjectMapper(m -> {
                                       try {
                                           return m.readValue(s, typeReference);
                                       } catch (final IOException ioe) {
                                           throw new RuntimeException(ioe);
                                       }
                                   })
        );
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static <R> R applyPrettyPrinter(final Function<? super ObjectWriter, ? extends R> function) {
        if (function == null) {
            throw new NullPointerException("function is null");
        }
        return applyObjectMapper(m -> function.apply(m.writerWithDefaultPrettyPrinter()));
    }

    public static <U, R> R applyPrettyPrinter(final BiFunction<? super ObjectWriter, ? super U, ? extends R> function,
                                              final Supplier<? extends U> supplier) {
        if (function == null) {
            throw new NullPointerException("function is null");
        }
        if (supplier == null) {
            throw new NullPointerException("supplier is null");
        }
        return applyPrettyPrinter(p -> function.apply(p, supplier.get()));
    }

    public static void acceptPrintPrinter(final Consumer<? super ObjectWriter> consumer) {
        if (consumer == null) {
            throw new NullPointerException("consumer is null");
        }
        applyPrettyPrinter(p -> {
            consumer.accept(p);
            return null;
        });
    }

    public static <U> void acceptPrintPrinter(final BiConsumer<? super ObjectWriter, ? super U> consumer,
                                              final Supplier<? extends U> supplier) {
        if (consumer == null) {
            throw new NullPointerException("consumer is null");
        }
        if (supplier == null) {
            throw new NullPointerException("supplier is null");
        }
        acceptPrintPrinter(p -> consumer.accept(p, supplier.get()));
    }

    public static String getPrettyString(final Object value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        return applyPrettyPrinter(w -> {
            try {
                return w.writeValueAsString(value);
            } catch (final JsonProcessingException jpe) {
                throw new RuntimeException(jpe);
            }
        });
    }

    public static void printPrettyStringTo(final Object value, final PrintWriter writer) {
        requireNonNull(writer, "writer is null").println(
                getPrettyString(requireNonNull(value, "value is null")));
    }

    public static void printPrettyStringTo(final Object value, final PrintStream stream) {
        requireNonNull(stream, "writer is null").println(
                getPrettyString(requireNonNull(value, "value is null")));
    }

    public static void printPrettyStringToSystemOut(final Object value) {
        printPrettyStringTo(value, System.out);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    private JacksonTests() {
        super();
    }
}
