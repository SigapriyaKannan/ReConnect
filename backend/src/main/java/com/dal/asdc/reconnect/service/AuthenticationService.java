package com.dal.asdc.reconnect.service;

import com.dal.asdc.reconnect.dto.LoginDto.LoginRequest;
import com.dal.asdc.reconnect.dto.SignUp.SignUpFirstPhaseBody;
import com.dal.asdc.reconnect.dto.SignUp.SignUpFirstPhaseRequest;
import com.dal.asdc.reconnect.dto.SignUp.SignUpSecondPhaseRequest;
import com.dal.asdc.reconnect.model.*;
import com.dal.asdc.reconnect.repository.*;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Service
@Slf4j
public class AuthenticationService {
    private static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    private static final String EMAIL_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final Pattern password_pattern = Pattern.compile(PASSWORD_PATTERN);
    private static final Pattern email_pattern = Pattern.compile(EMAIL_PATTERN);
    @Autowired
    UserTypeRepository userTypeRepository;
    @Autowired
    UsersSkillsRepository usersSkillsRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    UserDetailsRepository userDetailsRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    CityRepository cityRepository;
    @Autowired
    CountryRepository countryRepository;
    @Autowired
    SkillsRepository skillsRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;

    /**
     * This method will validate the email and check if it's correct.
     */
    public static boolean validateEmail(String email) {
        Matcher matcher = email_pattern.matcher(email);
        boolean isValid = matcher.matches();
        log.debug("Email validation for '{}': {}", email, isValid);
        return matcher.matches();
    }

    /**
     * @param signUpFirstPhaseRequest This will Check the initial SignUp request if it valid or not.
     * @return If the all validations pass then it will return true.
     */
    public SignUpFirstPhaseBody validateFirstPhase(SignUpFirstPhaseRequest signUpFirstPhaseRequest) {

        SignUpFirstPhaseBody signUpFirstPhaseBody = new SignUpFirstPhaseBody();
        Users user = getUserByEmail(signUpFirstPhaseRequest.getEmail());

        if (user != null) {
            log.warn("Email '{}' is already present", signUpFirstPhaseRequest.getEmail());
            signUpFirstPhaseBody.setEmailAlreadyPresent(true);
        }

        if (!matchPasswordWithConfirmPassword(signUpFirstPhaseRequest.getPassword(), signUpFirstPhaseRequest.getReenteredPassword())) {
            log.warn("Passwords do not match for email '{}'", signUpFirstPhaseRequest.getEmail());
            signUpFirstPhaseBody.setRepeatPasswordError(true);
        }

        if (!validateEmail(signUpFirstPhaseRequest.getEmail())) {
            log.warn("Invalid email format for '{}'", signUpFirstPhaseRequest.getEmail());
            signUpFirstPhaseBody.setEmailAlreadyPresent(true);
        }

        return signUpFirstPhaseBody;


    }

    /**
     * This method will check if the password and confirm password are same.
     */
    private boolean matchPasswordWithConfirmPassword(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    /**
     * This method will verify if users already has a account
     */
    public Users getUserByEmail(String email) {
        Optional<Users> user = usersRepository.findByUserEmail(email);
        log.debug("User retrieval by email '{}': {}", email, user.isPresent());
        return user.orElse(null);
    }

    /**
     * This method will add the user,user's details and skill details in the database.
     */
    @Transactional
    public boolean addNewUser(SignUpSecondPhaseRequest signUpSecondPhaseRequest, String fileNameAndPath) {
        try {
            UserDetails userDetails = addDetails(signUpSecondPhaseRequest, fileNameAndPath);
            if (userDetails == null) {
                return false;
            }

            Users user = addUser(signUpSecondPhaseRequest, userDetails);
            if (user == null) {
                return false;
            }

            if (!addSkills(signUpSecondPhaseRequest)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }

        return true;
    }

    /**
     * This method is used to verify the correctness of the second phase signup response
     */
    public boolean validateSecondPhase(SignUpSecondPhaseRequest signUpSecondPhaseRequest) {
        Optional<UserType> userType = userTypeRepository.findById(signUpSecondPhaseRequest.getUserType());

        Optional<Company> company = companyRepository.findById(signUpSecondPhaseRequest.getCompany());

        Optional<City> city = cityRepository.findById(signUpSecondPhaseRequest.getCity());

        Optional<Country> country = countryRepository.findById(signUpSecondPhaseRequest.getCountry());

        if (userType.isEmpty() || company.isEmpty() || city.isEmpty() || country.isEmpty()) {
            log.warn("Validation failed for second phase sign-up request: missing entities");
            return false;
        }

        for (Integer skillId : signUpSecondPhaseRequest.getSkills()) {
            Optional<Skills> skills = skillsRepository.findById(skillId);
            if (skills.isEmpty()) {
                log.warn("Skill ID '{}' not found during second phase sign-up validation", skillId);

                return false;
            }
        }
        return true;
    }

    /**
     * This method will add the skills into the database. (UserSkills Table)
     */
    public boolean addSkills(SignUpSecondPhaseRequest signUpSecondPhaseRequest) {
        Optional<Users> users = usersRepository.findByUserEmail(signUpSecondPhaseRequest.getEmail());
        if (users.isEmpty()) {
            return false;
        }
        for (Integer skillId : signUpSecondPhaseRequest.getSkills()) {
            Optional<Skills> skills = skillsRepository.findById(skillId);
            if (skills.isEmpty()) {
                return false;
            }
            UserSkills userSkills = new UserSkills();
            userSkills.setSkill(skills.get());
            userSkills.setUsers(users.get());
            usersSkillsRepository.save(userSkills);
        }
        return true;
    }

    /**
     * This method will add user's details into database. (UserDetails Table)
     */
    public UserDetails addDetails(SignUpSecondPhaseRequest signUpSecondPhaseRequest, String fileNameAndPath) {

        Optional<Company> company = companyRepository.findById(signUpSecondPhaseRequest.getCompany());
        Optional<City> city = cityRepository.findById(signUpSecondPhaseRequest.getCity());
        Optional<Country> country = countryRepository.findById(signUpSecondPhaseRequest.getCountry());

        if (company.isEmpty() || city.isEmpty() || country.isEmpty()) {
            log.error("Company, City, or Country not found during user details addition");

            return null;
        }

        UserDetails userDetails = new UserDetails();
        userDetails.setUserName(signUpSecondPhaseRequest.getUserName());
        userDetails.setCompany(company.get());
        userDetails.setExperience(signUpSecondPhaseRequest.getExperience());
        userDetails.setResume(signUpSecondPhaseRequest.getResume());
        userDetails.setProfilePicture(fileNameAndPath);
        userDetails.setCity(city.get());
        userDetails.setCountry(country.get());
        userDetails.setProfilePicture(fileNameAndPath);


        return userDetailsRepository.save(userDetails);
    }

    /**
     * This method will add the data into user table.
     */
    public Users addUser(SignUpSecondPhaseRequest signUpSecondPhaseRequest, UserDetails userDetails) {
        Optional<UserType> userType = userTypeRepository.findById(signUpSecondPhaseRequest.getUserType());

        if (userType.isEmpty())
        {
            log.error("UserType ID '{}' not found during user addition", signUpSecondPhaseRequest.getUserType());
            return null;
        }

        Users user = new Users();
        user.setUserEmail(signUpSecondPhaseRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpSecondPhaseRequest.getPassword()));
        user.setUserType(userType.get());
        user.setUserDetails(userDetails);

        return usersRepository.save(user);
    }


    /**
     * Authenticates a user based on the provided login request.
     *
     * @param input the LoginRequest object containing the user's email and password.
     * @return an Optional containing the authenticated user if the credentials are valid, or an empty Optional if not.
     */
    public Optional<Users> authenticate(LoginRequest input) {

        Optional<Users> user = usersRepository.findByUserEmail(input.getEmail());

        if (user.isPresent() && passwordEncoder.matches(input.getPassword(), user.get().getPassword())) {
            log.info("User '{}' authenticated successfully", input.getEmail());
            return user;
        } else {
            log.warn("Authentication failed for '{}'", input.getEmail());
            return Optional.empty();
        }
    }


}
