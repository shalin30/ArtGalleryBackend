package org.example.service;

import org.example.entity.UserDetails;
import org.example.json.*;
import org.example.mapper.UserResponseMapper;
import org.example.repository.BlacklistRepository;
import org.example.repository.UserRepository;
import org.example.security.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserOrchestratorServiceTest {

    @Mock
    UserFieldValidationService userFieldValidationService;

    @Mock
    UserResponseMapper userResponseMapper;

    @Mock
    UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private BlacklistRepository blacklistRepository;

    @Mock
    @Qualifier("appPasswordEncoder")
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    UserOrchestratorService userOrchestratorService;
    String traceId = UUID.randomUUID().toString();

    @Test
    public void testCreateUser(){
        UserCreationRequest request = getUserCreationRequest();
        UserCreationResponse userCreationResponse = new UserCreationResponse();
        userCreationResponse.setMessage("user created");

        when(userFieldValidationService.userRegistrationFieldValidation(any(UserCreationRequest.class), anyString())).thenReturn(new ValidationResponse());
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUserName(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("");
        when(userRepository.save(Mockito.any(UserDetails.class))).thenReturn(null);
        when(userResponseMapper.mapSuccessResponse(any(UserDetails.class), anyString())).thenReturn(userCreationResponse);

        UserCreationResponse response = userOrchestratorService.createUser(request,traceId);
        assertEquals("user created", response.getMessage());
    }

    @Test
    public void testCreateUserWithEmailExists(){
        UserCreationRequest request = getUserCreationRequest();
        UserCreationResponse userCreationResponse = new UserCreationResponse();
        userCreationResponse.setMessage("email already in use");

        when(userFieldValidationService.userRegistrationFieldValidation(any(UserCreationRequest.class), anyString())).thenReturn(new ValidationResponse());
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        when(userResponseMapper.mapDuplicateEmailResponse(anyString())).thenReturn(userCreationResponse);

        UserCreationResponse response = userOrchestratorService.createUser(request,traceId);
        assertEquals("email already in use", response.getMessage());
    }

    @Test
    public void testCreateUserWithUserNameExists(){
        UserCreationRequest request = getUserCreationRequest();
        UserCreationResponse userCreationResponse = new UserCreationResponse();
        userCreationResponse.setMessage("username already in use");

        when(userFieldValidationService.userRegistrationFieldValidation(any(UserCreationRequest.class), anyString())).thenReturn(new ValidationResponse());
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUserName(anyString())).thenReturn(true);
        when(userResponseMapper.mapDuplicateUserNameResponse(anyString())).thenReturn(userCreationResponse);

        UserCreationResponse response = userOrchestratorService.createUser(request,traceId);
        assertEquals("username already in use", response.getMessage());
    }

    @Test
    public void testCreateUserWithValidationError(){
        UserCreationResponse userCreationResponse = new UserCreationResponse();
        ValidationResponse validationResponse = getValidationResponse();
        userCreationResponse.setValidationResponse(validationResponse.toString());

        when(userFieldValidationService.userRegistrationFieldValidation(any(UserCreationRequest.class), anyString())).thenReturn(validationResponse);
        when(userResponseMapper.mapValidationErrorResponse(any(ValidationResponse.class), anyString())).thenReturn(userCreationResponse);

        UserCreationResponse response = userOrchestratorService.createUser(getUserCreationRequest(), traceId);
        Assertions.assertTrue(response.getValidationResponse().contains("Failure"));
    }

    @Test
    public void testUpdateUser(){
        UserCreationRequest request = getUserCreationRequest();
        UserCreationResponse userCreationResponse = new UserCreationResponse();
        userCreationResponse.setMessage("user updated");

        when(userFieldValidationService.updateUserFieldValidation(any(UserCreationRequest.class), anyString())).thenReturn(new ValidationResponse());
        when(userRepository.findByUserId(anyInt())).thenReturn(new UserDetails());
        when(userRepository.save(Mockito.any(UserDetails.class))).thenReturn(null);
        when(userResponseMapper.mapUpdateSuccessResponse(any(UserDetails.class), anyString())).thenReturn(userCreationResponse);
        when(passwordEncoder.encode(anyString())).thenReturn("");

        UserCreationResponse response = userOrchestratorService.updateUser("1",request,traceId);
        assertEquals("user updated", response.getMessage());
    }

    @Test
    public void testUpdateUserWithNullRequest(){
        UserCreationRequest request = new UserCreationRequest();
        request.setUserName(null);
        request.setEmail(null);
        request.setAddress1(null);
        request.setAddress2(null);
        request.setCity(null);
        request.setState(null);
        request.setPostalCode(null);
        request.setPassword(null);

        UserCreationResponse userCreationResponse = new UserCreationResponse();
        userCreationResponse.setMessage("user updated");

        when(userFieldValidationService.updateUserFieldValidation(any(UserCreationRequest.class), anyString())).thenReturn(new ValidationResponse());
        when(userRepository.findByUserId(anyInt())).thenReturn(new UserDetails());
        when(userRepository.save(Mockito.any(UserDetails.class))).thenReturn(null);
        when(userResponseMapper.mapUpdateSuccessResponse(any(UserDetails.class), anyString())).thenReturn(userCreationResponse);

        UserCreationResponse response = userOrchestratorService.updateUser("1",request,traceId);
        assertEquals("user updated", response.getMessage());
    }

    @Test
    public void testUpdateUserWithNoUserFound(){
        UserCreationRequest request = getUserCreationRequest();
        UserCreationResponse userCreationResponse = new UserCreationResponse();
        userCreationResponse.setMessage("user not found");

        when(userFieldValidationService.updateUserFieldValidation(any(UserCreationRequest.class), anyString())).thenReturn(new ValidationResponse());
        when(userRepository.findByUserId(anyInt())).thenReturn(null);
        when(userResponseMapper.mapNoUserFoundResponse(anyString())).thenReturn(userCreationResponse);

        UserCreationResponse response = userOrchestratorService.updateUser("1",request,traceId);
        assertEquals("user not found", response.getMessage());
    }

    @Test
    public void testUpdateUserWithValidationError(){
        UserCreationResponse userCreationResponse = new UserCreationResponse();
        ValidationResponse validationResponse = getValidationResponse();
        userCreationResponse.setValidationResponse(validationResponse.toString());

        when(userFieldValidationService.updateUserFieldValidation(any(UserCreationRequest.class), anyString())).thenReturn(validationResponse);
        when(userResponseMapper.mapValidationErrorResponse(any(ValidationResponse.class), anyString())).thenReturn(userCreationResponse);

        UserCreationResponse response = userOrchestratorService.updateUser("1",getUserCreationRequest(), traceId);
        Assertions.assertTrue(response.getValidationResponse().contains("Failure"));
    }

    @Test
    public void testDeactivateUser(){
        UserCreationResponse userCreationResponse = new UserCreationResponse();
        userCreationResponse.setMessage("user deleted");

        when(userRepository.findByUserId(anyInt())).thenReturn(new UserDetails());
        when(userRepository.save(Mockito.any(UserDetails.class))).thenReturn(null);
        when(userResponseMapper.mapDeActivateUserResponse(anyString())).thenReturn(userCreationResponse);

        UserCreationResponse response = userOrchestratorService.deactivateUser("1",traceId);
        assertEquals("user deleted", response.getMessage());
    }

    @Test
    public void testDeactivateUserWithNoUserFound(){
        UserCreationResponse userCreationResponse = new UserCreationResponse();
        userCreationResponse.setMessage("no user found");

        when(userRepository.findByUserId(anyInt())).thenReturn(null);
        when(userResponseMapper.mapNoUserFoundResponse(anyString())).thenReturn(userCreationResponse);

        UserCreationResponse response = userOrchestratorService.deactivateUser("1",traceId);
        assertEquals("no user found", response.getMessage());
    }

    @Test
    public void testLoginSuccess() {
        String username = "testUser";
        String password = "testPass";
        String traceId = "12345";
        String expectedToken = "jwtToken";

        Authentication authentication = mock(Authentication.class);

        doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
                .when(authentication).getAuthorities();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(jwtTokenProvider.generateToken(eq(username), anyList())).thenReturn(expectedToken);

        String token = userOrchestratorService.login(username, password, traceId);

        assertEquals(expectedToken, token);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider).generateToken(eq(username), anyList());
    }

    @Test
    public void testLogoutFailure() {
        String authHeader = null;
        UserCreationResponse userCreationResponse = new UserCreationResponse();
        userCreationResponse.setMessage("Invalid token");
        Mockito.when(userResponseMapper.mapLogOutFailureResponse(anyString())).thenReturn(userCreationResponse);
        UserCreationResponse response = userOrchestratorService.logout(authHeader, "trace123");
        Assertions.assertEquals("Invalid token", response.getMessage());
    }

    private static ValidationResponse getValidationResponse() {
        ValidationResponse validationResponse = new ValidationResponse();
        ArrayValidationErrors validationErrors = new ArrayValidationErrors();
        List<ValidationError> validationErrorList = new ArrayList<>();
        ValidationError validationError = new ValidationError();
        validationError.setTextID("test ID");
        validationError.setText("Failure");
        validationErrorList.add(validationError);
        validationErrors.setErrorList(validationErrorList);
        validationResponse.setValidationErrors(validationErrors);
        return validationResponse;
    }

    private static UserCreationRequest getUserCreationRequest() {
        UserCreationRequest request = new UserCreationRequest();
        request.setUserName("test name");
        request.setEmail("abc@gmail.com");
        request.setAddress1("test address1");
        request.setAddress2("test address2");
        request.setCity("test city");
        request.setState("test state");
        request.setPostalCode("123456");
        request.setPassword("test password");
        return request;
    }
}