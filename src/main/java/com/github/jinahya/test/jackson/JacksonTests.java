package com.github.jinahya.test.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.github.jinahya.test.lang.ResourceTests.applyResourceStream;
import static com.github.jinahya.test.validation.BeanValidationTests.requireValid;

@Slf4j
public final class JacksonTests {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A shared instance of object mapper.
     */
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(); // fully thread-safe!

    // -----------------------------------------------------------------------------------------------------------------
    public static <R> R applyObjectMapper(final Function<? super ObjectMapper, ? extends R> function) {
        return function.apply(OBJECT_MAPPER);
    }

    public static <U, R> R applyObjectMapper(final Supplier<? extends U> supplier,
                                             final BiFunction<? super ObjectMapper, ? super U, ? extends R> function) {
        return applyObjectMapper(v -> function.apply(v, supplier.get()));
    }

    public static void acceptObjectMapper(final Consumer<? super ObjectMapper> consumer) {
        applyObjectMapper(v -> {
            consumer.accept(v);
            return null;
        });
    }

    public static <U> void acceptObjectMapper(final Supplier<? extends U> supplier,
                                              final BiConsumer<? super ObjectMapper, ? super U> consumer) {
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
                                           return requireValid(m.readValue(s, valueClass));
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
                                           return requireValid(OBJECT_MAPPER.readValue(s, javaType));
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
                                           return requireValid(m.readValue(s, typeReference));
                                       } catch (final IOException ioe) {
                                           throw new RuntimeException(ioe);
                                       }
                                   })
        );
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    private JacksonTests() {
        super();
    }
}
