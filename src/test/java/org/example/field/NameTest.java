package org.example.field;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.example.field.Name.isValid;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NameTest {

    @Test
    public void testIsValid(){
        Assertions.assertEquals("",isValid("test name"));
        Assertions.assertEquals("reqName", isValid(null));
        Assertions.assertEquals("reqName", isValid(""));
        Assertions.assertEquals("errName", isValid("this is sample test name which will fail because name should be in certain limit"));
        Assertions.assertEquals("errName", isValid("testname@30"));
    }
}