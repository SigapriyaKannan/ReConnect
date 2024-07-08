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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
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
        request.setUserEmail("test@example.com");
        request.setPassword("Password1!");
        request.setReenteredPassword("Password1!");

        when(usersRepository.findByUserEmail(anyString())).thenReturn(new Users());

        SignUpFirstPhaseBody response = authenticationService.validateFirstPhase(request);

        assertFalse(response.areAllValuesNull());
    }

    @Test
    void testValidateFirstPhase_UserNotPresent()
    {
        SignUpFirstPhaseRequest request = new SignUpFirstPhaseRequest();
        request.setUserEmail("test@example.com");
        request.setPassword("Password1!");
        request.setReenteredPassword("Password1!");

        when(usersRepository.findByUserEmail(anyString())).thenReturn(null);

        SignUpFirstPhaseBody response = authenticationService.validateFirstPhase(request);

        assertTrue(response.areAllValuesNull());
    }


    @Test
    void testValidateFirstPhase_ReenterPasswordError()
    {
        SignUpFirstPhaseRequest request = new SignUpFirstPhaseRequest();
        request.setUserEmail("test@example.com");
        request.setPassword("Password1!");
        request.setReenteredPassword("DifferentPassword1!");

        when(usersRepository.findByUserEmail(anyString())).thenReturn(null);

        SignUpFirstPhaseBody response = authenticationService.validateFirstPhase(request);

        assertFalse(response.areAllValuesNull());
    }


    @Test
    void testValidateFirstPhase_InvalidEmail()
    {
        SignUpFirstPhaseRequest request = new SignUpFirstPhaseRequest();
        request.setUserEmail("invalid-email");
        request.setPassword("Password1!");
        request.setReenteredPassword("Password1!");

        when(usersRepository.findByUserEmail(anyString())).thenReturn(null);

        SignUpFirstPhaseBody response = authenticationService.validateFirstPhase(request);

        assertFalse(response.areAllValuesNull());
    }


    @Test
    void testAddNewUser_Success()
    {
        SignUpSecondPhaseRequest request = new SignUpSecondPhaseRequest();
        request.setUserEmail("test@example.com");
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
        when(usersRepository.findByUserEmail(anyString())).thenReturn(new Users());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        assertTrue(authenticationService.addNewUser(request));
    }



    @Test
    void testAddNewUser_NotSuccess()
    {
        SignUpSecondPhaseRequest request = new SignUpSecondPhaseRequest();
        request.setUserEmail("test@example.com");
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
        when(usersRepository.findByUserEmail(anyString())).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        assertFalse(authenticationService.addNewUser(request));
    }


    @Test
    void testAuthenticate_Failure()
    {
        LoginRequest request = new LoginRequest();
        request.setUserEmail("test@example.com");
        request.setPassword("WrongPassword1!");

        Users user = new Users();
        user.setUserEmail("test@example.com");
        user.setPassword("encodedPassword");

        when(usersRepository.findByUserEmail(anyString())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        Optional<Users> result = authenticationService.authenticate(request);

        assertFalse(result.isPresent());
    }



}
