package org.example.service;

import org.example.json.ValidationResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class BaseFieldValidationServiceTest {

    @Test
    public void testPrepareValidationMessageCatchBlock() {
        BaseFieldValidationService service = Mockito.spy(new BaseFieldValidationService());
        ResourceBundle bundle = Mockito.mock(ResourceBundle.class);

        ReflectionTestUtils.setField(service, "errorMessages", bundle);

        Mockito.when(bundle.getString("dummyCode")).thenThrow(new MissingResourceException("Not found", "ResourceBundle", "dummyCode"));

        List<String> errorList = Arrays.asList("dummyCode");
        ValidationResponse response = service.prepareValidationMessage(errorList);

        Assertions.assertFalse(response.getValidationErrors().getErrorList().isEmpty());
        Assertions.assertEquals("Validation Message not found", response.getValidationErrors().getErrorList().get(0).getText());
    }

    @Test
    public void testAddErrorToListWithValidErrorCode() {
        List<String> errorList = new ArrayList<>();
        String errorCode = "missingField";

        BaseFieldValidationService service = Mockito.spy(new BaseFieldValidationService());
        service.addErrorToList(errorCode, errorList);

        Assertions.assertFalse(errorList.isEmpty());
        Assertions.assertEquals(1, errorList.size());
        Assertions.assertEquals("missingField", errorList.get(0));
    }

    @Test
    public void testAddErrorToListWithNullOrEmptyErrorCode() {
        List<String> errorList = new ArrayList<>();

        BaseFieldValidationService service = Mockito.spy(new BaseFieldValidationService());
        service.addErrorToList(null, errorList);
        service.addErrorToList("", errorList);

        Assertions.assertTrue(errorList.isEmpty());
    }
}