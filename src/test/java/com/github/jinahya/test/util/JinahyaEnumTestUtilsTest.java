package com.github.jinahya.test.util;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.Set;

import static com.github.jinahya.test.util.JinahyaEnumTestUtils.getRandomEnumConstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JinahyaEnumTestUtilsTest {

    private enum E_ {
    }

    private enum S_ {
        A
    }

    private enum D_ {
        A,
        B
    }

    @Test
    void assertGetRandomEnumConstantThrowsIllegalArgumentExceptionWhenThereIsNoConstants() {
        assertThrows(IllegalArgumentException.class, () -> getRandomEnumConstant(E_.class));
    }

    @Test
    void testGetRandomEnumConstantWithS_() {
        assertThat(getRandomEnumConstant(S_.class))
                .isNotNull();
    }

    @Test
    void testGetRandomEnumConstantWithD_() {
        for (final Set<D_> set = EnumSet.noneOf(D_.class); set.size() < D_.values().length; ) {
            assertThat(getRandomEnumConstant(D_.class))
                    .isNotNull()
                    .satisfies(set::add);
        }
    }
}