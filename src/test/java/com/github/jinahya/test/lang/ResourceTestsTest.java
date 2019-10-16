package com.github.jinahya.test.lang;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.jinahya.test.lang.ResourceTests.acceptResourceStream;
import static com.github.jinahya.test.lang.ResourceTests.applyResourceStream;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class ResourceTestsTest {

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void testApplyResourceStream() throws IOException {
        assertTrue((boolean) applyResourceStream(null,
                                                 "com/github/jinahya/test/lang/empty",
                                                 (s, u) -> s != null,
                                                 () -> null));
        assertTrue((boolean) applyResourceStream(getClass().getClassLoader(),
                                                 "com/github/jinahya/test/lang/empty",
                                                 (s, u) -> s != null,
                                                 () -> null));
    }

    @Test
    public void testAcceptResourceStream() throws IOException {
        acceptResourceStream(null,
                             "com/github/jinahya/test/lang/empty",
                             (s, u) -> {
                                 assert s != null;
                             },
                             () -> null);
        acceptResourceStream(getClass().getClassLoader(),
                             "com/github/jinahya/test/lang/empty",
                             (s, u) -> {
                                 assert s != null;
                             },
                             () -> null);
    }
}
