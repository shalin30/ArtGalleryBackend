package org.example.field;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.example.field.State.isValid;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StateTest {

    @Test
    public void testIsValid(){
        Assertions.assertEquals("",isValid("state"));
        Assertions.assertEquals("reqState", isValid(null));
        Assertions.assertEquals("reqState", isValid(""));
        Assertions.assertEquals("errState", isValid("state30"));
        Assertions.assertEquals("errState", isValid("this state will fail so because it should be in to certain limit"));
    }
}