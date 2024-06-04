package com.dal.asdc.reconnect.service;
import com.dal.asdc.reconnect.DTO.SignUpRequest;
import com.dal.asdc.reconnect.DTO.SignUpRequestFinal;
import com.dal.asdc.reconnect.DTO.SignUpResponse;
import com.dal.asdc.reconnect.model.*;
import com.dal.asdc.reconnect.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Service
public class SignUpService
{
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


    private static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    private static final String EMAIL_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    private static final Pattern password_pattern = Pattern.compile(PASSWORD_PATTERN);

    private static final Pattern email_pattern = Pattern.compile(EMAIL_PATTERN);


    /**
     * @param signUpRequest This will Check the initial SignUp request if it valid or not.
     * @return If the all validations pass then it will return true.
     */
    public SignUpResponse validateFirstPhase(SignUpRequest signUpRequest)
    {
        SignUpResponse signUpResponse = new SignUpResponse();

        if(!checkIfUserAlreadyPresent(signUpRequest.getUserEmail()))
        {
            signUpResponse.setEmailAlreadyPresent(true);
        }

        if(!validatePassword(signUpRequest.getPassword()))
        {
            signUpResponse.setPasswordError(true);
        }

        if(!matchPasswordWithConfirmPassword(signUpRequest.getPassword(), signUpRequest.getReenteredPassword()))
        {
            signUpResponse.setRepeatPasswordError(true);
        }

        if(!validateEmail(signUpRequest.getUserEmail()))
        {
            signUpResponse.setEmailAlreadyPresent(true);
        }

        return signUpResponse;
    }

    /**
     *  This method will check if the password and confirm password are same.
     */
    private boolean matchPasswordWithConfirmPassword(String password, String confirmPassword)
    {
        return password.equals(confirmPassword);
    }

    /**
     * This method will verify if users already has a account
     */
    private boolean checkIfUserAlreadyPresent(String email)
    {
        Optional<Users> user= usersRepository.findByUserEmail(email);
        return user.isEmpty();
    }

    /**
     * This method will check the strength of the password.
     */
    public static boolean validatePassword(String password)
    {
        Matcher matcher = password_pattern.matcher(password);
        return matcher.matches();
    }

    /**
     * This method will validate the email and check if it's correct.
     */
    public static boolean validateEmail(String email)
    {
        Matcher matcher = email_pattern.matcher(email);
        return matcher.matches();
    }


    /**
     * This method will add the user,user's details and skill details in the database.
     */
    public Boolean AddNewUser(SignUpRequestFinal signUpRequestFinal)
    {
        if(validateSecondPhase(signUpRequestFinal))
        {
            try
            {
                if(!addUser(signUpRequestFinal) || !addDetails(signUpRequestFinal) || !addSkills(signUpRequestFinal))
                {
                    return false;
                }

            }catch(Exception e)
            {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * This method is used to verify the correctness of the second phase signup response
     */
    private boolean validateSecondPhase(SignUpRequestFinal signUpRequestFinal)
    {
        Optional<UserType> userType = userTypeRepository.findById(signUpRequestFinal.getUserType());

        Optional<Company> comapany = companyRepository.findById(signUpRequestFinal.getCompany());

        Optional<City> city = cityRepository.findById(signUpRequestFinal.getCity());

        Optional<Country> country = countryRepository.findById(signUpRequestFinal.getCountry());

        if (userType.isEmpty() || comapany.isEmpty() || city.isEmpty() || country.isEmpty())
        {
            return false;
        }

        for(Integer skillId : signUpRequestFinal.getSkill())
        {
            Optional<Skills> skills = skillsRepository.findById(skillId);
            if(skills.isEmpty())
            {
                return false;
            }
        }
        return true;
    }

    /**
     * This method will add the skills into the database. (UserSkills Table)
     */
    private boolean addSkills(SignUpRequestFinal signUpRequestFinal)
    {
        Optional<Users> users = usersRepository.findByUserEmail(signUpRequestFinal.getUserEmail());
        if(users.isEmpty())
        {
            return false;
        }
        for(Integer skillId : signUpRequestFinal.getSkill())
        {
            Optional<Skills> skills = skillsRepository.findById(skillId);
            if(skills.isEmpty())
            {
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
    private boolean addDetails(SignUpRequestFinal signUpRequestFinal)
    {

        Optional<Users> users = usersRepository.findByUserEmail(signUpRequestFinal.getUserEmail());
        Optional<Company> comapany = companyRepository.findById(signUpRequestFinal.getCompany());
        Optional<City> city = cityRepository.findById(signUpRequestFinal.getCity());
        Optional<Country> country = countryRepository.findById(signUpRequestFinal.getCountry());

        if (users.isEmpty() || comapany.isEmpty() || city.isEmpty() || country.isEmpty())
        {
            return false;
        }

        UserDetails userDetails = new UserDetails();
        userDetails.setUserName(signUpRequestFinal.getUserEmail());
        userDetails.setUsers(users.get());
        userDetails.setCompany(comapany.get());
        userDetails.setExperience(signUpRequestFinal.getExperience());
        userDetails.setResume(signUpRequestFinal.getResume());
        userDetails.setProfilePicture(signUpRequestFinal.getProfile());
        userDetails.setCity(city.get());
        userDetails.setCountry(country.get());
        userDetailsRepository.save(userDetails);

        return true;

    }

    /**
     * This method will add the data into user table.
     */
    private boolean addUser(SignUpRequestFinal signUpRequestFinal)
    {
        Optional<UserType> userType = userTypeRepository.findById(signUpRequestFinal.getUserType());

        if (userType.isEmpty())
        {
            return false;
        }

        Users user = new Users();
        user.setUserEmail(signUpRequestFinal.getUserEmail());
        user.setPassword(signUpRequestFinal.getPassword());
        user.setUserType(userType.get());
        usersRepository.save(user);

        return true;
    }
}
