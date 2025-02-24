package org.example.field;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.example.field.Dimensions.isValid;

@ExtendWith(MockitoExtension.class)
class DimensionsTest {

    @Test
    public void testIsValid(){
        Assertions.assertEquals("",isValid("20 * 20 inches"));
        Assertions.assertEquals("reqArtDimensions", isValid(null));
        Assertions.assertEquals("reqArtDimensions", isValid(""));
        Assertions.assertEquals("errArtDimensions", isValid("20 + 20 inches"));
        Assertions.assertEquals("errArtDimensions", isValid("20 * 20 feet"));
        Assertions.assertEquals("errArtDimensions", isValid("20*20inches"));
    }

}