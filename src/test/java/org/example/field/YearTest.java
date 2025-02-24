package org.example.field;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.example.field.Year.isValid;

@ExtendWith(MockitoExtension.class)
class YearTest {

    @Test
    public void testIsValid(){
        Assertions.assertEquals("",isValid("1234"));
        Assertions.assertEquals("reqArtYear", isValid(null));
        Assertions.assertEquals("reqArtYear", isValid(""));
        Assertions.assertEquals("errArtYear", isValid("12345678"));
        Assertions.assertEquals("errArtYear", isValid("123"));
        Assertions.assertEquals("errArtYear", isValid("fail12"));
    }
}