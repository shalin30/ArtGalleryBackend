package org.example.field;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.example.field.PostalCode.isValid;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PostalCodeTest {

    @Test
    public void testIsValid(){
        Assertions.assertEquals("",isValid("123456"));
        Assertions.assertEquals("reqPostalCode", isValid(null));
        Assertions.assertEquals("reqPostalCode", isValid(""));
        Assertions.assertEquals("errPostalCode", isValid("12345678"));
        Assertions.assertEquals("errPostalCode", isValid("12345"));
        Assertions.assertEquals("errPostalCode", isValid("fail12"));
    }
}