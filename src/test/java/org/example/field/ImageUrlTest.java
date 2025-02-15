package org.example.field;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.example.field.ImageUrl.isValid;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ImageUrlTest {

    @Test
    public void testIsValid(){
        Assertions.assertEquals("",isValid("https://test.png"));
        Assertions.assertEquals("reqImageUrl", isValid(null));
        Assertions.assertEquals("reqImageUrl", isValid(""));
        Assertions.assertEquals("errImageUrl", isValid("image url has to follow certain pattern"));
    }
}