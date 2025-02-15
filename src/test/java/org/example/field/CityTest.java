package org.example.field;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.example.field.City.isValid;

@ExtendWith(MockitoExtension.class)
class CityTest {

    @Test
    public void testIsValid(){
        Assertions.assertEquals("",isValid("this is a sample city"));
        Assertions.assertEquals("reqCity", isValid(null));
        Assertions.assertEquals("reqCity", isValid(""));
        Assertions.assertEquals("errCity", isValid("this is a sample city just to validate that city length with more than certain characters will fail validation checks"));
        Assertions.assertEquals("errCity", isValid("city cannot have numbers like 99"));
    }
}