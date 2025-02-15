package org.example.field;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.example.field.Title.isValid;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TitleTest {

    @Test
    public void testIsValid(){
        Assertions.assertEquals("",isValid("state"));
        Assertions.assertEquals("reqArtTitle", isValid(null));
        Assertions.assertEquals("reqArtTitle", isValid(""));
        Assertions.assertEquals("errArtTitle", isValid("art30"));
        Assertions.assertEquals("errArtTitle", isValid("this art will fail so because it should be in to certain limit"));
    }
}