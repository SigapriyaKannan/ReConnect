package com.dal.asdc.reconnect;

import com.dal.asdc.reconnect.dto.LoginDto.LoginRequest;
import com.dal.asdc.reconnect.dto.SignUp.SignUpFirstPhaseBody;
import com.dal.asdc.reconnect.dto.SignUp.SignUpFirstPhaseRequest;
import com.dal.asdc.reconnect.dto.SignUp.SignUpSecondPhaseRequest;
import com.dal.asdc.reconnect.model.*;
import com.dal.asdc.reconnect.repository.*;
import com.dal.asdc.reconnect.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class AuthenticationServiceTest
{
    @Mock
    UserTypeRepository userTypeRepository;

    @Mock
    UsersSkillsRepository usersSkillsRepository;

    @Mock
    UsersRepository usersRepository;

    @Mock
    UserDetailsRepository userDetailsRepository;

    @Mock
    CompanyRepository companyRepository;

    @Mock
    CityRepository cityRepository;

    @Mock
    CountryRepository countryRepository;

    @Mock
    SkillsRepository skillsRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testValidateFirstPhase_UserAlreadyPresent()
    {
        SignUpFirstPhaseRequest request = new SignUpFirstPhaseRequest();
        request.setEmail("test@example.com");
        request.setPassword("Password1!");
        request.setReenteredPassword("Password1!");

        when(usersRepository.findByUserDetailsUserName(anyString())).thenReturn(Optional.of(new Users()));

        SignUpFirstPhaseBody response = authenticationService.validateFirstPhase(request);

        assertFalse(response.areAllValuesNull());
    }

    @Test
    void testValidateFirstPhase_UserNotPresent()
    {
        SignUpFirstPhaseRequest request = new SignUpFirstPhaseRequest();
        request.setEmail("test@example.com");
        request.setPassword("Password1!");
        request.setReenteredPassword("Password1!");

        when(usersRepository.findByUserDetailsUserName(anyString())).thenReturn(Optional.empty());

        SignUpFirstPhaseBody response = authenticationService.validateFirstPhase(request);

        assertTrue(response.areAllValuesNull());
    }


    @Test
    void testValidateFirstPhase_ReenterPasswordError()
    {
        SignUpFirstPhaseRequest request = new SignUpFirstPhaseRequest();
        request.setEmail("test@example.com");
        request.setPassword("Password1!");
        request.setReenteredPassword("DifferentPassword1!");

        when(usersRepository.findByUserDetailsUserName(anyString())).thenReturn(Optional.empty());

        SignUpFirstPhaseBody response = authenticationService.validateFirstPhase(request);

        assertFalse(response.areAllValuesNull());
    }


    @Test
    void testValidateFirstPhase_InvalidEmail()
    {
        SignUpFirstPhaseRequest request = new SignUpFirstPhaseRequest();
        request.setEmail("invalid-email");
        request.setPassword("Password1!");
        request.setReenteredPassword("Password1!");

        when(usersRepository.findByUserDetailsUserName(anyString())).thenReturn(Optional.empty());

        SignUpFirstPhaseBody response = authenticationService.validateFirstPhase(request);

        assertFalse(response.areAllValuesNull());
    }


    @Test
    void testAddNewUser_Success()
    {
        SignUpSecondPhaseRequest request = new SignUpSecondPhaseRequest();
        request.setEmail("test@example.com");
        request.setPassword("Password1!");
        request.setUserType(1);
        request.setCompany(1);
        request.setCity(1);
        request.setCountry(1);
        request.setSkills(List.of(new Integer[]{1, 2}));

        when(userTypeRepository.findById(anyInt())).thenReturn(Optional.of(new UserType()));
        when(companyRepository.findById(anyInt())).thenReturn(Optional.of(new Company()));
        when(cityRepository.findById(anyInt())).thenReturn(Optional.of(new City()));
        when(countryRepository.findById(anyInt())).thenReturn(Optional.of(new Country()));
        when(skillsRepository.findById(anyInt())).thenReturn(Optional.of(new Skills()));
        when(usersRepository.findByUserDetailsUserName(anyString())).thenReturn(Optional.of(new Users()));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        assertTrue(authenticationService.addNewUser(request, ""));
    }



    @Test
    void testAddNewUser_NotSuccess()
    {
        SignUpSecondPhaseRequest request = new SignUpSecondPhaseRequest();
        request.setEmail("test@example.com");
        request.setPassword("Password1!");
        request.setUserType(1);
        request.setCompany(1);
        request.setCity(1);
        request.setCountry(1);
        request.setSkills(List.of(new Integer[]{1, 2}));

        when(userTypeRepository.findById(anyInt())).thenReturn(Optional.of(new UserType()));
        when(companyRepository.findById(anyInt())).thenReturn(Optional.of(new Company()));
        when(cityRepository.findById(anyInt())).thenReturn(Optional.of(new City()));
        when(countryRepository.findById(anyInt())).thenReturn(Optional.of(new Country()));
        when(skillsRepository.findById(anyInt())).thenReturn(Optional.of(new Skills()));
        when(usersRepository.findByUserDetailsUserName(anyString())).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        assertFalse(authenticationService.addNewUser(request, ""));
    }


    @Test
    void testAuthenticate_Failure()
    {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("WrongPassword1!");

        Users user = new Users();
        user.setUserEmail("test@example.com");
        user.setPassword("encodedPassword");

        when(usersRepository.findByUserDetailsUserName(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        Optional<Users> result = authenticationService.authenticate(request);

        assertFalse(result.isPresent());
    }


    @Test
    void testAuthenticate_Success() {
        // Scenario 1: User is found and the password matches
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("Password1!");

        Users user = new Users();
        user.setUserEmail("test@example.com");
        user.setPassword("encodedPassword");

        when(usersRepository.findByUserDetailsUserName(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        Optional<Users> result = authenticationService.authenticate(request);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testAuthenticate_PasswordDoesNotMatch() {
        // Scenario 2: User is found but the password does not match
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("WrongPassword1!");

        Users user = new Users();
        user.setUserEmail("test@example.com");
        user.setPassword("encodedPassword");

        when(usersRepository.findByUserDetailsUserName(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        Optional<Users> result = authenticationService.authenticate(request);

        assertFalse(result.isPresent());
    }

    @Test
    void testAuthenticate_UserNotFound() {
        // Scenario 3: User is not found
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("Password1!");

        when(usersRepository.findByUserDetailsUserName(anyString())).thenReturn(Optional.empty());

        Optional<Users> result = authenticationService.authenticate(request);

        assertFalse(result.isPresent());
    }


    @Test
    void testAddDetails_UserNotFound() {
        SignUpSecondPhaseRequest request = new SignUpSecondPhaseRequest();
        request.setEmail("test@example.com");
        request.setCompany(1);
        request.setCity(1);
        request.setCountry(1);

        when(usersRepository.findByUserDetailsUserName(anyString())).thenReturn(Optional.empty());
        when(companyRepository.findById(anyInt())).thenReturn(Optional.of(new Company()));
        when(cityRepository.findById(anyInt())).thenReturn(Optional.of(new City()));
        when(countryRepository.findById(anyInt())).thenReturn(Optional.of(new Country()));

        boolean result = authenticationService.addDetails(request, "file/path");

        assertFalse(result);
    }

    @Test
    void testAddDetails_CompanyNotFound() {
        SignUpSecondPhaseRequest request = new SignUpSecondPhaseRequest();
        request.setEmail("test@example.com");
        request.setCompany(1);
        request.setCity(1);
        request.setCountry(1);

        when(usersRepository.findByUserDetailsUserName(anyString())).thenReturn(Optional.of(new Users()));
        when(companyRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(cityRepository.findById(anyInt())).thenReturn(Optional.of(new City()));
        when(countryRepository.findById(anyInt())).thenReturn(Optional.of(new Country()));

        boolean result = authenticationService.addDetails(request, "file/path");

        assertFalse(result);
    }

    @Test
    void testAddDetails_CityNotFound() {
        SignUpSecondPhaseRequest request = new SignUpSecondPhaseRequest();
        request.setEmail("test@example.com");
        request.setCompany(1);
        request.setCity(1);
        request.setCountry(1);

        when(usersRepository.findByUserDetailsUserName(anyString())).thenReturn(Optional.of(new Users()));
        when(companyRepository.findById(anyInt())).thenReturn(Optional.of(new Company()));
        when(cityRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(countryRepository.findById(anyInt())).thenReturn(Optional.of(new Country()));

        boolean result = authenticationService.addDetails(request, "file/path");

        assertFalse(result);
    }

    @Test
    void testAddDetails_CountryNotFound() {
        SignUpSecondPhaseRequest request = new SignUpSecondPhaseRequest();
        request.setEmail("test@example.com");
        request.setCompany(1);
        request.setCity(1);
        request.setCountry(1);

        when(usersRepository.findByUserDetailsUserName(anyString())).thenReturn(Optional.of(new Users()));
        when(companyRepository.findById(anyInt())).thenReturn(Optional.of(new Company()));
        when(cityRepository.findById(anyInt())).thenReturn(Optional.of(new City()));
        when(countryRepository.findById(anyInt())).thenReturn(Optional.empty());

        boolean result = authenticationService.addDetails(request, "file/path");

        assertFalse(result);
    }


    @Test
    void testAddSkills1() {
        SignUpSecondPhaseRequest request = new SignUpSecondPhaseRequest();
        request.setEmail("test@example.com");
        request.setSkills(List.of(1, 2));
        when(usersRepository.findByUserDetailsUserName(anyString())).thenReturn(Optional.empty());

        boolean result = authenticationService.addSkills(request);

        assertFalse(result);
    }


    @Test
    void testAddSkills() {
        SignUpSecondPhaseRequest request = new SignUpSecondPhaseRequest();
        request.setEmail("test@example.com");
        request.setSkills(List.of(1, 2, 3));

        Users user = new Users();
        when(usersRepository.findByUserDetailsUserName(anyString())).thenReturn(Optional.of(user));
        when(skillsRepository.findById(1)).thenReturn(Optional.of(new Skills()));
        when(skillsRepository.findById(2)).thenReturn(Optional.of(new Skills()));
        when(skillsRepository.findById(3)).thenReturn(Optional.empty());

        boolean result = authenticationService.addSkills(request);

        assertFalse(result);
    }


    @Test
    void testAddUser_UserTypeEmpty() {
        SignUpSecondPhaseRequest request = new SignUpSecondPhaseRequest();
        request.setEmail("test@example.com");
        request.setPassword("Password1!");
        request.setUserType(1);
        when(userTypeRepository.findById(anyInt())).thenReturn(Optional.empty());

        boolean result = authenticationService.addUser(request);

        assertFalse(result);
    }


    @Test
    void testValidateSecondPhase_AllPresent() {
        SignUpSecondPhaseRequest request = new SignUpSecondPhaseRequest();
        request.setUserType(1);
        request.setCompany(1);
        request.setCity(1);
        request.setCountry(1);
        request.setSkills(List.of(1, 2));

        when(userTypeRepository.findById(anyInt())).thenReturn(Optional.of(new UserType()));
        when(companyRepository.findById(anyInt())).thenReturn(Optional.of(new Company()));
        when(cityRepository.findById(anyInt())).thenReturn(Optional.of(new City()));
        when(countryRepository.findById(anyInt())).thenReturn(Optional.of(new Country()));
        when(skillsRepository.findById(anyInt())).thenReturn(Optional.of(new Skills()));

        boolean result = authenticationService.validateSecondPhase(request);

        assertTrue(result);
    }

    @Test
    void testValidateSecondPhase_UserTypeEmpty() {
        SignUpSecondPhaseRequest request = new SignUpSecondPhaseRequest();
        request.setUserType(1);
        when(userTypeRepository.findById(anyInt())).thenReturn(Optional.empty());

        boolean result = authenticationService.validateSecondPhase(request);

        assertFalse(result);
    }

    @Test
    void testAddNewUser_ValidateSecondPhaseFalse() {
        SignUpSecondPhaseRequest request = new SignUpSecondPhaseRequest();
        request.setUserType(1);
        when(userTypeRepository.findById(anyInt())).thenReturn(Optional.empty());

        boolean result = authenticationService.addNewUser(request, "fileName");

        assertFalse(result);
    }

    @Test
    void testValidateSecondPhase_SkillsEmpty() {
        SignUpSecondPhaseRequest request = new SignUpSecondPhaseRequest();
        request.setUserType(1);
        request.setCompany(1);
        request.setCity(1);
        request.setCountry(1);
        request.setSkills(Collections.singletonList(1));
        when(userTypeRepository.findById(anyInt())).thenReturn(Optional.of(new UserType()));
        when(companyRepository.findById(anyInt())).thenReturn(Optional.of(new Company()));
        when(cityRepository.findById(anyInt())).thenReturn(Optional.of(new City()));
        when(countryRepository.findById(anyInt())).thenReturn(Optional.of(new Country()));
        when(skillsRepository.findById(1)).thenReturn(Optional.empty());

        boolean result = authenticationService.validateSecondPhase(request);

        assertFalse(result);
    }

}
