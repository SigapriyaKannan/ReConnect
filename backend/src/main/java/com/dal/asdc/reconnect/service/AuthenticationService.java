package com.dal.asdc.reconnect.service;

import com.dal.asdc.reconnect.dto.LoginDto.LoginRequest;
import com.dal.asdc.reconnect.dto.SignUp.SignUpFirstPhaseBody;
import com.dal.asdc.reconnect.dto.SignUp.SignUpFirstPhaseRequest;
import com.dal.asdc.reconnect.dto.SignUp.SignUpSecondPhaseRequest;
import com.dal.asdc.reconnect.model.*;
import com.dal.asdc.reconnect.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Service
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
            signUpFirstPhaseBody.setEmailAlreadyPresent(true);
        }

        if (!matchPasswordWithConfirmPassword(signUpFirstPhaseRequest.getPassword(), signUpFirstPhaseRequest.getReenteredPassword())) {
            signUpFirstPhaseBody.setRepeatPasswordError(true);
        }

        if (!validateEmail(signUpFirstPhaseRequest.getEmail())) {
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
        return user.orElse(null);
    }

    /**
     * This method will add the user,user's details and skill details in the database.
     */
    public boolean addNewUser(SignUpSecondPhaseRequest signUpSecondPhaseRequest, String fileNameAndPath) {
        if (validateSecondPhase(signUpSecondPhaseRequest)) {
            try {
                if (!addUser(signUpSecondPhaseRequest) || !addDetails(signUpSecondPhaseRequest, fileNameAndPath) || !addSkills(signUpSecondPhaseRequest)) {
                    return false;
                }

            } catch (Exception e) {
                return false;
            }
            return true;
        }
        return false;
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
            return false;
        }

        for (Integer skillId : signUpSecondPhaseRequest.getSkills()) {
            Optional<Skills> skills = skillsRepository.findById(skillId);
            if (skills.isEmpty()) {
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
    public boolean addDetails(SignUpSecondPhaseRequest signUpSecondPhaseRequest, String fileNameAndPath) {

        Optional<Users> users = usersRepository.findByUserEmail(signUpSecondPhaseRequest.getEmail());
        Optional<Company> comapany = companyRepository.findById(signUpSecondPhaseRequest.getCompany());
        Optional<City> city = cityRepository.findById(signUpSecondPhaseRequest.getCity());
        Optional<Country> country = countryRepository.findById(signUpSecondPhaseRequest.getCountry());

        if (users.isEmpty() || comapany.isEmpty() || city.isEmpty() || country.isEmpty()) {
            return false;
        }

        UserDetails userDetails = new UserDetails();
        userDetails.setUserName(signUpSecondPhaseRequest.getEmail());
        userDetails.setUsers(users.get());
        userDetails.setCompany(comapany.get());
        userDetails.setExperience(signUpSecondPhaseRequest.getExperience());
        userDetails.setResume(signUpSecondPhaseRequest.getResume());
        userDetails.setProfilePicture(fileNameAndPath);
        userDetails.setCity(city.get());
        userDetails.setCountry(country.get());
        userDetails.setProfilePicture(fileNameAndPath);
        userDetailsRepository.save(userDetails);

        return true;

    }

    /**
     * This method will add the data into user table.
     */
    public boolean addUser(SignUpSecondPhaseRequest signUpSecondPhaseRequest) {
        Optional<UserType> userType = userTypeRepository.findById(signUpSecondPhaseRequest.getUserType());

        if (userType.isEmpty()) {
            return false;
        }

        Users user = new Users();
        user.setUserEmail(signUpSecondPhaseRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpSecondPhaseRequest.getPassword()));
        user.setUserType(userType.get());
        usersRepository.save(user);

        return true;
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
            return user;
        } else {
            return Optional.empty();
        }
    }
}
