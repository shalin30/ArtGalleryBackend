package org.example.field;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.example.field.Price.isValid;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PriceTest {

    @Test
    public void testIsValid(){
        Assertions.assertEquals("",isValid("100.00"));
        Assertions.assertEquals("reqPrice", isValid(null));
        Assertions.assertEquals("reqPrice", isValid(""));
        Assertions.assertEquals("errPrice", isValid("12345678"));
        Assertions.assertEquals("errPrice", isValid("fail"));
        Assertions.assertEquals("errPrice", isValid("1034.304"));
        Assertions.assertEquals("errPrice", isValid("10344.30"));
    }
}