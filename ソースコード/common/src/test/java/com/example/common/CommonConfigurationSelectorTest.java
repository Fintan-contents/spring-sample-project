package com.example.common;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CommonConfigurationSelectorTest {

    @Test
    void testSelectImports() {
        CommonConfigurationSelector sut = new CommonConfigurationSelector();
        String[] imports = sut.selectImports(null);
        assertArrayEquals(new String[] {
                CommonConfiguration.class.getName()
        }, imports);
    }
}
