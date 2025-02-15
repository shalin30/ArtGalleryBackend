package org.example.field;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.example.field.Description.isValid;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DescriptionTest {

    @Test
    public void testIsValid(){
        Assertions.assertEquals("",isValid("this is a sample description"));
        Assertions.assertEquals("reqArtDescription", isValid(null));
        Assertions.assertEquals("reqArtDescription", isValid(""));
        Assertions.assertEquals("errArtDescription", isValid("this is a sample description just to validate that description length with more than certain characters will fail validation checks"));
        Assertions.assertEquals("errArtDescription", isValid("description cannot have special characters like @#"));
    }
}