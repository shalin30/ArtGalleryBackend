package org.example.field;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.example.field.Email.isValid;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmailTest {

    @Test
    public void testIsValid(){
        Assertions.assertEquals("",isValid("abc@gmail.com"));
        Assertions.assertEquals("reqEmail", isValid(null));
        Assertions.assertEquals("reqEmail", isValid(""));
        Assertions.assertEquals("errEmail", isValid("this is a sample email just to validate that email length with more than 30 characters will fail validation checks"));
        Assertions.assertEquals("errEmail", isValid("thishouldbetheemailformatbutlengthmusthavebeenlessthan30@gmail.com"));
    }
}