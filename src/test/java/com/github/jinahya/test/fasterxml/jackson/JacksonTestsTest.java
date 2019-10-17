package com.github.jinahya.test.fasterxml.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.jinahya.test.fasterxml.jackson.JacksonTests.acceptObjectMapper;
import static com.github.jinahya.test.fasterxml.jackson.JacksonTests.acceptPrettyPrinter;
import static com.github.jinahya.test.fasterxml.jackson.JacksonTests.applyObjectMapper;
import static com.github.jinahya.test.fasterxml.jackson.JacksonTests.applyPrettyPrinter;
import static com.github.jinahya.test.fasterxml.jackson.JacksonTests.printPrettyStringToSystemOut;
import static com.github.jinahya.test.fasterxml.jackson.JacksonTests.readTreeFromResource;
import static com.github.jinahya.test.fasterxml.jackson.JacksonTests.readValueFromResource;
import static com.github.jinahya.test.lang.ResourceTests.acceptResourceStream;
import static com.github.jinahya.test.lang.ResourceTests.applyResourceStream;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JacksonTestsTest {

    // -----------------------------------------------------------------------------------------------------------------
    private static class TheObject {

        @Setter
        @Getter
        private String name;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void testApplyObjectMapper_emptyObject() throws IOException {
        final JsonNode jsonNode = applyResourceStream(
                null,
                "com/github/jinahya/test/fasterxml/jackson/object.json",
                s -> applyObjectMapper(
                        (m, u) -> {
                            try {
                                return m.readTree(s);
                            } catch (final IOException ioe) {
                                throw new RuntimeException(ioe);
                            }
                        },
                        () -> null
                )
        );
        assertNotNull(jsonNode);
        assertEquals(JsonNodeType.OBJECT, jsonNode.getNodeType());
    }

    @Test
    public void testApplyObjectMapper_emptyArray() throws IOException {
        final JsonNode jsonNode = applyResourceStream(
                null,
                "com/github/jinahya/test/fasterxml/jackson/array.json",
                s -> applyObjectMapper(
                        (m, u) -> {
                            try {
                                return m.readTree(s);
                            } catch (final IOException ioe) {
                                throw new RuntimeException(ioe);
                            }
                        },
                        () -> null
                )
        );
        assertNotNull(jsonNode);
        assertEquals(JsonNodeType.ARRAY, jsonNode.getNodeType());
    }

    @Test
    public void testAcceptObjectMapper_emptyObject() throws IOException {
        acceptResourceStream(
                null,
                "com/github/jinahya/test/fasterxml/jackson/object.json",
                s -> acceptObjectMapper(
                        (m, u) -> {
                            try {
                                final JsonNode jsonNode = m.readTree(s);
                                assertNotNull(jsonNode);
                                assertEquals(JsonNodeType.OBJECT, jsonNode.getNodeType());
                            } catch (final IOException ioe) {
                                throw new RuntimeException(ioe);
                            }
                        },
                        () -> null
                )
        );
    }

    @Test
    public void testAcceptObjectMapper_emptyArray() throws IOException {
        acceptResourceStream(
                null,
                "com/github/jinahya/test/fasterxml/jackson/array.json",
                s -> acceptObjectMapper(
                        (m, u) -> {
                            try {
                                final JsonNode jsonNode = m.readTree(s);
                                assertNotNull(jsonNode);
                                assertEquals(JsonNodeType.ARRAY, jsonNode.getNodeType());
                            } catch (final IOException ioe) {
                                throw new RuntimeException(ioe);
                            }
                        },
                        () -> null
                )
        );
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void testReadTreeFromResource_emptyObject() throws IOException {
        final JsonNode jsonNode = readTreeFromResource(
                null, "com/github/jinahya/test/fasterxml/jackson/object.json");
        assertNotNull(jsonNode);
        assertEquals(JsonNodeType.OBJECT, jsonNode.getNodeType());
    }

    @Test
    public void testReadTreeFromResource_emptyArray() throws IOException {
        final JsonNode jsonNode = readTreeFromResource(
                null, "com/github/jinahya/test/fasterxml/jackson/array.json");
        assertNotNull(jsonNode);
        assertEquals(JsonNodeType.ARRAY, jsonNode.getNodeType());
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void testReadValueFromResource() throws IOException {
        {
            final String[] value = readValueFromResource(
                    null, "com/github/jinahya/test/fasterxml/jackson/array.json", String[].class);
            assertNotNull(value);
            assertArrayEquals(new String[] {"a", "b", "c"}, value);
        }
        {
            final TheObject value = readValueFromResource(
                    null, "com/github/jinahya/test/fasterxml/jackson/object.json", TheObject.class);
            assertNotNull(value);
            assertEquals("unknown", value.name);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void testApplyPrettyPrinter() {
        final ObjectWriter prettyPrinter = applyPrettyPrinter((p, u) -> p, () -> null);
        assertNotNull(prettyPrinter);
    }

    @Test
    public void testAcceptPrettyPrinter() {
        acceptPrettyPrinter((p, u) -> assertNotNull(p), () -> null);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void testPrettyPrintStreamToSystemOut() {
        printPrettyStringToSystemOut(asList("a", "b", "c"));
        printPrettyStringToSystemOut(new TheObject());
    }
}
