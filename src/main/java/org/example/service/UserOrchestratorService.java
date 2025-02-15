package org.example.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.BlacklistedToken;
import org.example.entity.UserDetails;
import org.example.json.UserCreationRequest;
import org.example.json.UserCreationResponse;
import org.example.json.ValidationResponse;
import org.example.mapper.UserResponseMapper;
import org.example.repository.BlacklistRepository;
import org.example.repository.UserRepository;
import org.example.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserOrchestratorService extends BaseFieldValidationService{

    @Autowired
    UserFieldValidationService userFieldValidationService;

    @Autowired
    UserResponseMapper userResponseMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private BlacklistRepository blacklistRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    @Qualifier("appPasswordEncoder")
    private BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;

    public UserOrchestratorService(UserFieldValidationService userFieldValidationService, UserResponseMapper userResponseMapper,
                                   UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
                                   JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager,
                                   BlacklistRepository blacklistRepository) {
        this.userFieldValidationService = userFieldValidationService;
        this.userResponseMapper = userResponseMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.blacklistRepository = blacklistRepository;
    }

    public UserCreationResponse createUser(UserCreationRequest userCreationRequest, String traceId) {
        UserCreationResponse response;
        log.info("UserRegisterService started, traceId : {}", traceId);

        ValidationResponse validationResponse = userFieldValidationService.userRegistrationFieldValidation(userCreationRequest, traceId);

        if(!anyValidationErrorExists(validationResponse)) {

            if(userRepository.existsByEmail(userCreationRequest.getEmail())){
                response = userResponseMapper.mapDuplicateEmailResponse(traceId);
            }
             else if(userRepository.existsByUserName(userCreationRequest.getUserName())){
                response = userResponseMapper.mapDuplicateUserNameResponse(traceId);
            } else {
                UserDetails userDetails = new UserDetails();
                userDetails.setUserName(userCreationRequest.getUserName());
                userDetails.setEmail(userCreationRequest.getEmail());
                userDetails.setAddress1(userCreationRequest.getAddress1());
                userDetails.setAddress2(userCreationRequest.getAddress2());
                userDetails.setCity(userCreationRequest.getCity());
                userDetails.setState(userCreationRequest.getState());
                userDetails.setPostalCode(userCreationRequest.getPostalCode());
                userDetails.setCreatedAt(LocalDateTime.now());
                userDetails.setModifiedAt(LocalDateTime.now());

                String hashedPassword = passwordEncoder.encode(userCreationRequest.getPassword());
                userDetails.setPassword(hashedPassword);

                userRepository.save(userDetails);
                response = userResponseMapper.mapSuccessResponse(userDetails,traceId);
            }
        } else {
            response = userResponseMapper.mapValidationErrorResponse(validationResponse, traceId);
        }

        log.info("UserRegisterService ended, traceId : {}", traceId);
        return response;
    }

    public UserCreationResponse updateUser(String userId, UserCreationRequest userCreationRequest, String traceId){
        log.info("updateUser service started, traceId: {}", traceId);

        UserCreationResponse response;

        ValidationResponse validationResponse = userFieldValidationService.updateUserFieldValidation(userCreationRequest, traceId);
        if(!anyValidationErrorExists(validationResponse)) {

            UserDetails user = userRepository.findByUserId(Integer.valueOf(userId));

            if(user != null) {
                if (userCreationRequest.getUserName() != null) user.setUserName(userCreationRequest.getUserName());
                if (userCreationRequest.getEmail() != null) user.setEmail(userCreationRequest.getEmail());
                if (userCreationRequest.getAddress1() != null) user.setAddress1(userCreationRequest.getAddress1());
                if (userCreationRequest.getAddress2() != null) user.setAddress2(userCreationRequest.getAddress2());
                if (userCreationRequest.getCity() != null) user.setCity(userCreationRequest.getCity());
                if (userCreationRequest.getState() != null) user.setState(userCreationRequest.getState());
                if (userCreationRequest.getPostalCode() != null) user.setPostalCode(userCreationRequest.getPostalCode());
                if(userCreationRequest.getPassword() != null) {
                    String hashedPassword = passwordEncoder.encode(userCreationRequest.getPassword());
                    user.setPassword(hashedPassword);
                }
                user.setModifiedAt(LocalDateTime.now());
                userRepository.save(user);
                response = userResponseMapper.mapUpdateSuccessResponse(user,traceId);
            } else {
                response = userResponseMapper.mapNoUserFoundResponse(traceId);
            }
        } else {
            response = userResponseMapper.mapValidationErrorResponse(validationResponse, traceId);
        }
        log.info("updateUser service ended, traceId: {}", traceId);
        return response;
    }

    @Transactional
    public UserCreationResponse deactivateUser(String userId, String traceId) {
        log.info("deactivateUser service started, traceId: {}", traceId);
        UserDetails user = userRepository.findByUserId(Integer.valueOf(userId));
        UserCreationResponse response;
        if(user != null) {
            user.setActive(false);
            userRepository.save(user);
            response = userResponseMapper.mapDeActivateUserResponse(traceId);
        } else {
            response = userResponseMapper.mapNoUserFoundResponse(traceId);
        }
        log.info("deactivateUser service ended, traceId: {}", traceId);
        return response;
    }

    public String login(String username, String password, String traceId) {
        log.info("login service started, traceId: {}", traceId);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String token = jwtTokenProvider.generateToken(username, roles);
        log.info("login service ended, traceId: {}", traceId);
        return token;
    }

    public UserCreationResponse logout(String authHeader, String traceId) {
        log.info("logout service started, traceId: {}", traceId);
        UserCreationResponse response;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();

            Date expiryDate = claims.getExpiration();
            blacklistRepository.save(new BlacklistedToken(token, expiryDate));
            response = userResponseMapper.mapLogOutSuccessResponse(traceId);
        } else {
            response = userResponseMapper.mapLogOutFailureResponse(traceId);
        }
        log.info("logout service started, traceId: {}", traceId);
        return response;
    }
}
