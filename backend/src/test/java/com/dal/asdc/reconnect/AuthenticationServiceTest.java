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
import org.mockito.Spy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


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
    private UserDetails userDetails;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    private SignUpSecondPhaseRequest signUpSecondPhaseRequest;

    @Spy
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

        when(usersRepository.findByUserEmail(anyString())).thenReturn(Optional.of(new Users()));

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
    void testAddNewUser_Success() {
        SignUpSecondPhaseRequest request = new SignUpSecondPhaseRequest();
        request.setEmail("test@example.com");
        request.setPassword("Password1!");
        request.setUserType(1);
        request.setCompany(1);
        request.setCity(1);
        request.setCountry(1);
        request.setSkills(List.of(1, 2));

        // Mock the repositories and service methods
        when(userTypeRepository.findById(anyInt())).thenReturn(Optional.of(new UserType()));
        when(companyRepository.findById(anyInt())).thenReturn(Optional.of(new Company()));
        when(cityRepository.findById(anyInt())).thenReturn(Optional.of(new City()));
        when(countryRepository.findById(anyInt())).thenReturn(Optional.of(new Country()));
        when(skillsRepository.findById(anyInt())).thenReturn(Optional.of(new Skills()));
        when(usersRepository.findByUserEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(usersRepository.findByUserDetailsUserName(anyString())).thenReturn(Optional.empty());

        doReturn(new UserDetails()).when(authenticationService).addDetails(any(SignUpSecondPhaseRequest.class), anyString());
        doReturn(new Users()).when(authenticationService).addUser(any(SignUpSecondPhaseRequest.class), any(UserDetails.class));
        doReturn(true).when(authenticationService).addSkills(any(SignUpSecondPhaseRequest.class));

        // Execute the method under test
        boolean result = authenticationService.addNewUser(request, "");

        // Assert the result
        assertTrue(result);
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

        when(usersRepository.findByUserEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        Optional<Users> result = authenticationService.authenticate(request);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
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

        UserDetails result = authenticationService.addDetails(request, "file/path");

        assertNull(result);
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

        UserDetails result = authenticationService.addDetails(request, "file/path");

        assertNull(result);
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

        UserDetails result = authenticationService.addDetails(request, "file/path");

        assertNull(result);
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

        UserDetails result = authenticationService.addDetails(request, "file/path");

        assertNull(result);
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

        Users result = authenticationService.addUser(request, null);

        assertNull(result);
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

    @Test
    void addSkills_ShouldReturnFalseIfUserDoesNotExist() {
        SignUpSecondPhaseRequest request = new SignUpSecondPhaseRequest();
        request.setEmail("test@example.com");
        request.setSkills(Arrays.asList(1, 2, 3));

        when(usersRepository.findByUserEmail("test@example.com")).thenReturn(Optional.empty());

        boolean result = authenticationService.addSkills(request);

        assertFalse(result, "Expected addSkills to return false if the user does not exist");
    }

    @Test
    void addSkills_ShouldReturnFalseIfAnySkillDoesNotExist() {
        SignUpSecondPhaseRequest request = new SignUpSecondPhaseRequest();
        request.setEmail("test@example.com");
        request.setSkills(Arrays.asList(1, 2, 3));

        Users user = new Users();
        when(usersRepository.findByUserEmail("test@example.com")).thenReturn(Optional.of(user));
        when(skillsRepository.findById(1)).thenReturn(Optional.of(new Skills()));
        when(skillsRepository.findById(2)).thenReturn(Optional.empty()); // Skill 2 does not exist
        when(skillsRepository.findById(3)).thenReturn(Optional.of(new Skills()));

        boolean result = authenticationService.addSkills(request);

        assertFalse(result, "Expected addSkills to return false if any skill does not exist");
    }

    @Test
    void addSkills_ShouldReturnTrueAndSaveUserSkillsIfAllSkillsExist() {
        SignUpSecondPhaseRequest request = new SignUpSecondPhaseRequest();
        request.setEmail("test@example.com");
        request.setSkills(Arrays.asList(1, 2, 3));

        Users user = new Users();
        Skills skill1 = new Skills();
        Skills skill2 = new Skills();
        Skills skill3 = new Skills();

        when(usersRepository.findByUserEmail("test@example.com")).thenReturn(Optional.of(user));
        when(skillsRepository.findById(1)).thenReturn(Optional.of(skill1));
        when(skillsRepository.findById(2)).thenReturn(Optional.of(skill2));
        when(skillsRepository.findById(3)).thenReturn(Optional.of(skill3));

        boolean result = authenticationService.addSkills(request);

        assertTrue(result, "Expected addSkills to return true if all skills exist");
        verify(usersSkillsRepository, times(3)).save(any(UserSkills.class)); // Verify save is called three times
    }

    @Test
    void addUser_ShouldReturnUserIfUserTypeExists() {
        SignUpSecondPhaseRequest request = new SignUpSecondPhaseRequest();
        request.setEmail("test@example.com");
        request.setPassword("Password1!");
        request.setUserType(1); // Assuming 1 is a valid user type ID

        UserDetails userDetails = new UserDetails();
        UserType userType = new UserType();
        userType.setTypeID(1);

        Users expectedUser = new Users();
        expectedUser.setUserEmail(request.getEmail());
        expectedUser.setPassword("encodedPassword");
        expectedUser.setUserType(userType);
        expectedUser.setUserDetails(userDetails);

        // Mocking repository behavior
        when(userTypeRepository.findById(request.getUserType())).thenReturn(Optional.of(userType));
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(usersRepository.save(any(Users.class))).thenReturn(expectedUser);

        Users result = authenticationService.addUser(request, userDetails);

        assertNotNull(result);
        assertEquals(expectedUser.getUserEmail(), result.getUserEmail());
        assertEquals(expectedUser.getPassword(), result.getPassword());
        assertEquals(expectedUser.getUserType(), result.getUserType());
        assertEquals(expectedUser.getUserDetails(), result.getUserDetails());
    }

    @Test
    void addUser_ShouldReturnNullIfUserTypeDoesNotExist() {
        SignUpSecondPhaseRequest request = new SignUpSecondPhaseRequest();
        request.setEmail("test@example.com");
        request.setPassword("Password1!");
        request.setUserType(1); // Assuming 1 is a user type ID that does not exist

        UserDetails userDetails = new UserDetails();

        // Mocking repository behavior
        when(userTypeRepository.findById(request.getUserType())).thenReturn(Optional.empty());

        Users result = authenticationService.addUser(request, userDetails);

        assertNull(result, "Expected addUser to return null if user type does not exist");
    }

    @Test
    void addNewUser_ShouldReturnFalseIfAddDetailsReturnsNull() {
        when(authenticationService.addDetails(signUpSecondPhaseRequest, "filePath")).thenReturn(null);

        boolean result = authenticationService.addNewUser(signUpSecondPhaseRequest, "filePath");

        assertFalse(result, "Expected addNewUser to return false if addDetails returns null");
    }

    @Test
    void addNewUse_ShouldReturnFalseIfAddDetailsReturnsNull() {
        when(authenticationService.addDetails(signUpSecondPhaseRequest, "filePath")).thenReturn(null);

        boolean result = authenticationService.addNewUser(signUpSecondPhaseRequest, "filePath");

        assertFalse(result, "Expected addNewUser to return false if addDetails returns null");
    }

    @Test
    public void testAddNewUser_AddDetailsReturnsNull() {
        // Arrange
        SignUpSecondPhaseRequest request = new SignUpSecondPhaseRequest();
        String fileNameAndPath = "file/path";

        when(authenticationService.addDetails(request, fileNameAndPath)).thenReturn(null);

        // Act
        boolean result = authenticationService.addNewUser(request, fileNameAndPath);

        // Assert
        assertFalse(result);
        verify(authenticationService, never()).addUser(any(), any());
        verify(authenticationService, never()).addSkills(any());
    }

}


