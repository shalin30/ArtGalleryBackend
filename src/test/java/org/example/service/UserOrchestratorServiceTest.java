package org.example.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.example.entity.BlacklistedToken;
import org.example.entity.UserDetails;
import org.example.json.*;
import org.example.mapper.UserResponseMapper;
import org.example.repository.BlacklistRepository;
import org.example.repository.UserRepository;
import org.example.security.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
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
    private Authentication authentication;

    @Mock
    private BlacklistRepository blacklistRepository;

    @Mock
    private Claims claims;

    @Mock
    private JwtParser jwtParser;

    @Mock
    private Jws<Claims> jws;

    @Mock
    @Qualifier("appPasswordEncoder")
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    UserOrchestratorService userOrchestratorService;
    String traceId = UUID.randomUUID().toString();
    private static final String USERNAME = "testUser";
    private static final String PASSWORD = "testPassword";
    private static final String USER_ID = "1";
    private static final String TOKEN = "jwt.token.string";

    private final String jwtSecret = "test-jwt-secret-key";

    private static final String VALID_AUTH_HEADER = "Bearer valid.jwt.token";
    private static final String INVALID_AUTH_HEADER = "Invalid-Header";
    private static final String NULL_AUTH_HEADER = null;
    private static final String EXTRACTED_TOKEN = "valid.jwt.token";

    private LogoutResponse successResponse;
    private LogoutResponse failureResponse;

    @BeforeEach
    void setUp() throws Exception {
        java.lang.reflect.Field field = UserOrchestratorService.class.getDeclaredField("jwtSecret");
        field.setAccessible(true);
        field.set(userOrchestratorService, jwtSecret);

        successResponse = new LogoutResponse();
        successResponse.setMessage("success");

        failureResponse = new LogoutResponse();
        failureResponse.setMessage("failure");
    }

    @Test
    void testSuccessfulLogout() {
        Date futureDate = new Date(System.currentTimeMillis() + 3600000); // 1 hour in the future

        try (MockedStatic<Jwts> mockedJwts = mockStatic(Jwts.class)) {
            mockedJwts.when(Jwts::parser).thenReturn(jwtParser);
            when(jwtParser.setSigningKey(anyString())).thenReturn(jwtParser);
            when(jwtParser.parseClaimsJws(EXTRACTED_TOKEN)).thenReturn(jws);
            when(jws.getBody()).thenReturn(claims);
            when(claims.getExpiration()).thenReturn(futureDate);
            when(userResponseMapper.mapLogOutSuccessResponse(traceId)).thenReturn(successResponse);

            LogoutResponse response = userOrchestratorService.logout(VALID_AUTH_HEADER, traceId);

            assertNotNull(response);
            assertEquals("success", response.getMessage());

            ArgumentCaptor<BlacklistedToken> tokenCaptor = ArgumentCaptor.forClass(BlacklistedToken.class);
            verify(blacklistRepository).save(tokenCaptor.capture());

            BlacklistedToken blacklistedToken = tokenCaptor.getValue();
            assertEquals(EXTRACTED_TOKEN, blacklistedToken.getToken());
            assertEquals(futureDate, blacklistedToken.getExpiryDate());

            verify(userResponseMapper).mapLogOutSuccessResponse(traceId);
        }
    }

    @Test
    void testInvalidAuthHeaderFormat() {
        when(userResponseMapper.mapLogOutFailureResponse(traceId)).thenReturn(failureResponse);
        LogoutResponse response = userOrchestratorService.logout(INVALID_AUTH_HEADER, traceId);

        assertNotNull(response);
        assertEquals("failure", response.getMessage());

        verify(blacklistRepository, never()).save(any(BlacklistedToken.class));
        verify(userResponseMapper).mapLogOutFailureResponse(traceId);
    }

    @Test
    void testNullAuthHeader() {
        when(userResponseMapper.mapLogOutFailureResponse(traceId)).thenReturn(failureResponse);
        LogoutResponse response = userOrchestratorService.logout(NULL_AUTH_HEADER, traceId);

        assertNotNull(response);
        assertEquals("failure", response.getMessage());

        verify(blacklistRepository, never()).save(any(BlacklistedToken.class));
        verify(userResponseMapper).mapLogOutFailureResponse(traceId);
    }

    @Test
    void testSuccessfulLogin() {
        org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User(
                        USERNAME,
                        PASSWORD,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                );

        UserDetails userEntity = new UserDetails();
        userEntity.setUserId(Integer.valueOf(USER_ID));
        userEntity.setUserName(USERNAME);

        List<String> roles = Collections.singletonList("ROLE_USER");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authentication.getAuthorities())
                .thenReturn((Collection) Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        when(userRepository.findByUserName(USERNAME)).thenReturn(userEntity);
        when(jwtTokenProvider.generateToken(USERNAME, roles)).thenReturn(TOKEN);

        LoginResponse response = userOrchestratorService.login(USERNAME, PASSWORD, traceId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(TOKEN, response.getToken());
        assertEquals(USER_ID, response.getUserId());
        assertEquals(USERNAME, response.getUserName());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByUserName(USERNAME);
        verify(jwtTokenProvider).generateToken(USERNAME, roles);
    }

    @Test
    void testFailedAuthentication() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        LoginResponse response = userOrchestratorService.login(USERNAME, PASSWORD, traceId);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        assertNull(response.getToken());
        assertNull(response.getUserId());
        assertNull(response.getUserName());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, never()).findByUserName(anyString());
        verify(jwtTokenProvider, never()).generateToken(anyString(), anyList());
    }

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
    void testAuthenticationException() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        LoginResponse response = userOrchestratorService.login(USERNAME, PASSWORD, traceId);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        assertNull(response.getToken());
        assertNull(response.getUserId());
        assertNull(response.getUserName());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, never()).findByUserName(anyString());
        verify(jwtTokenProvider, never()).generateToken(anyString(), anyList());
    }

    @Test
    void testNullUser() {
        org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User(
                        USERNAME,
                        PASSWORD,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                );

        List<String> roles = Collections.singletonList("ROLE_USER");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authentication.getAuthorities())
                .thenReturn((Collection) Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        when(userRepository.findByUserName(USERNAME)).thenReturn(null);
        when(jwtTokenProvider.generateToken(USERNAME, roles)).thenReturn(TOKEN);

        LoginResponse response = userOrchestratorService.login(USERNAME, PASSWORD, traceId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(TOKEN, response.getToken());
        assertNull(response.getUserId());
        assertNull(response.getUserName());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByUserName(USERNAME);
        verify(jwtTokenProvider).generateToken(USERNAME, roles);
    }

    @Test
    void testGenericException() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        LoginResponse response = userOrchestratorService.login(USERNAME, PASSWORD, traceId);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        assertNull(response.getToken());
        assertNull(response.getUserId());
        assertNull(response.getUserName());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, never()).findByUserName(anyString());
        verify(jwtTokenProvider, never()).generateToken(anyString(), anyList());
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
    public void testLogoutFailure() {
        String authHeader = null;
        LogoutResponse logoutResponse = new LogoutResponse();
        logoutResponse.setMessage("Invalid token");
        Mockito.when(userResponseMapper.mapLogOutFailureResponse(anyString())).thenReturn(logoutResponse);
        LogoutResponse response = userOrchestratorService.logout(authHeader, "trace123");
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
        request.setPassword("test password");
        return request;
    }

}