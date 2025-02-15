package org.example.field;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.example.field.Address.isValid;

@ExtendWith(MockitoExtension.class)
class AddressTest {

    @Test
    public void testIsValid(){
        Assertions.assertEquals("",isValid("this is a sample address"));
        Assertions.assertEquals("reqAddress", isValid(null));
        Assertions.assertEquals("reqAddress", isValid(""));
        Assertions.assertEquals("errAddress", isValid("this is a sample address just to validate that address length with more than 30 characters will fail validation checks"));
        Assertions.assertEquals("errAddress", isValid("address cannot have special characters like @#"));
    }
}