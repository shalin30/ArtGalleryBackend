package org.example.field;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.example.field.Medium.isValid;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MediumTest {

    @Test
    public void testIsValid(){
        Assertions.assertEquals("",isValid("oil on canvas"));
        Assertions.assertEquals("reqArtMedium", isValid(null));
        Assertions.assertEquals("reqArtMedium", isValid(""));
        Assertions.assertEquals("errArtMedium", isValid("20 + 20 inches"));
        Assertions.assertEquals("errArtMedium", isValid("This art medium will fail because it has more than valid characters"));
    }
}