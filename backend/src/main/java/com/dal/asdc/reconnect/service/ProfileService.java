package com.dal.asdc.reconnect.service;

import com.dal.asdc.reconnect.dto.Skill.SkillsDto;
import com.dal.asdc.reconnect.dto.userdetails.UserDetailsRequest;
import com.dal.asdc.reconnect.dto.userdetails.UserDetailsResponse;
import com.dal.asdc.reconnect.model.*;
import com.dal.asdc.reconnect.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class ProfileService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UsersSkillsRepository usersSkillsRepository;

    @Autowired
    private SkillsRepository skillsRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CompanyRepository companyRepository;
    /**
     * Retrieves user details and skills for a given user ID.
     *
     * @param userID The unique identifier of the user to retrieve details for.
     * @return UserDetailsResponse containing user details and skills.
     * @throws UsernameNotFoundException if no user is found with the given ID.
     */
    public UserDetailsResponse getUserDetailsByUserID(int userID) {
        Optional<Users> userOptional = usersRepository.findByUserID(userID);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            UserDetails userDetails = user.getUserDetails();
            List<UserSkills> skills = usersSkillsRepository.findByUsersUserID(user.getUserID());

            return getUserDetailsResponse(userDetails, skills.stream());
        } else {
            log.error("User not found with ID: {}", userID);
            throw new UsernameNotFoundException("User not found with ID: " + userID);
        }
    }
    /**
     * Converts UserDetails and a stream of UserSkills into a UserDetailsResponse object.
     *
     * This method maps various fields from UserDetails to a UserDetailsResponse object.
     * It also processes a stream of UserSkills, converting each skill into a SkillsDto
     * and adding it to the response.
     *
     * @param userDetails The UserDetails object containing the user's information.
     * @param stream A Stream of UserSkills objects representing the user's skills.
     * @return A UserDetailsResponse object populated with the user's details and skills.
     */
    private UserDetailsResponse getUserDetailsResponse(UserDetails userDetails, Stream<UserSkills> stream) {
        UserDetailsResponse response = new UserDetailsResponse();
        response.setUserName(userDetails.getUserName());
        response.setExperience(userDetails.getExperience());
        response.setCompany(userDetails.getCompany().getCompanyId());
        response.setCity(userDetails.getCity().getCityId());
        response.setCountry(userDetails.getCountry().getCountryId());
        response.setProfilePicture(userDetails.getProfilePicture());

        List<SkillsDto> skillDtos = stream.map(skill -> {
            SkillsDto dto = new SkillsDto();
            dto.setSkillId(skill.getSkill().getSkillId());
            dto.setSkillName(skill.getSkill().getSkillName());
            return dto;

        }).collect(Collectors.toList());

        response.setSkills(skillDtos);
        return response;
    }

    /**
     * Updates user details and skills based on the provided request.
     *
     * This method performs the following operations within a transaction:
     * 1. Updates the user's basic details (name, experience, company, city, country).
     * 2. Deletes all existing skills for the user.
     * 3. Adds new skills based on the provided skill IDs.
     * 4. Saves all changes to the database.
     *
     * @param request A UserDetailsRequest object containing the updated user information and skill IDs.
     * @return A UserDetailsResponse object containing the updated user details and skills.
     * @throws UsernameNotFoundException if the user is not found.
     * @throws RuntimeException if the specified company, city, country, or any skill is not found.
     */
        @Transactional
        public UserDetailsResponse updateUserDetails(UserDetailsRequest request) {
            Users user = usersRepository.findById(Integer.valueOf(request.getUserId())).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            UserDetails userDetails = user.getUserDetails();

            userDetails.setUserName(request.getUserName());
            userDetails.setExperience(request.getExperience());
            Company company = companyRepository.findById(request.getCompany())
                    .orElseThrow(() -> new RuntimeException("Company not found"));
            userDetails.setCompany(company);

            City city = cityRepository.findById(request.getCity())
                    .orElseThrow(() -> new RuntimeException("City not found"));
            userDetails.setCity(city);

            Country country = countryRepository.findById(request.getCountry())
                    .orElseThrow(() -> new RuntimeException("Country not found"));
            userDetails.setCountry(country);

            userDetails = userDetailsRepository.save(userDetails);

            usersSkillsRepository.deleteByUsers(user);

            List<UserSkills> skills = request.getSkillIds().stream().map(skillId -> {
                UserSkills userSkill = new UserSkills();
                userSkill.setUsers(user);
                Skills skill = skillsRepository.findById(skillId)
                        .orElseThrow(() -> new RuntimeException("Skill not found with ID: " + skillId));
                userSkill.setSkill(skill);
                return userSkill;
            }).collect(Collectors.toList());

            usersSkillsRepository.saveAll(skills);
            log.info("User details updated for user ID: {}", request.getUserId());
            return getUserDetailsResponse(userDetails, skills.stream());
        }
    /**
     * Updates the resume path for a user.
     *
     * This method updates the resume path for the user with the specified ID.
     *
     * @param userId The ID of the user.
     * @param resumePath The new resume path.
     * @throws UsernameNotFoundException if the user is not found.
     */
    public void updateResumePath(int userId, String resumePath) {
        Optional<Users> user = usersRepository.findById(userId);
        if (user.isPresent()) {
            UserDetails userDetails = user.get().getUserDetails();
            userDetails.setResume(resumePath);
            userDetailsRepository.save(userDetails);
            log.info("Resume path updated for user ID: {}", userId);
        } else {
            throw new UsernameNotFoundException("User not found with ID: " + userId);
        }
    }
    /**
     * Updates the profile picture path for a user.
     *
     * This method updates the profile picture path for the user with the specified ID.
     *
     * @param userId The ID of the user.
     * @param profilePicturePath The new profile picture path.
     * @throws UsernameNotFoundException if the user is not found.
     */
    public void updateProfilePicturePath(int userId, String profilePicturePath) {
        Optional<Users> user = usersRepository.findById(userId);
        if (user.isPresent()) {
            UserDetails userDetails = user.get().getUserDetails();
            userDetails.setProfilePicture(profilePicturePath);
            userDetailsRepository.save(userDetails);
            log.info("Profile picture path updated for user ID: {}", userId);
        } else {
            throw new UsernameNotFoundException("User not found with ID: " + userId);
        }
    }
    /**
     * Gets the resume path for a user.
     *
     * This method retrieves the resume path for the user with the specified ID.
     *
     * @param userId The ID of the user.
     * @return The resume path.
     * @throws UsernameNotFoundException if the user is not found.
     */
    public String getResumePath(int userId) {
        Optional<Users> user = usersRepository.findById(userId);
        if (user.isPresent()) {
            return user.get().getUserDetails().getResume();
        } else {
            log.error("User not found with ID: {}", userId);
            throw new UsernameNotFoundException("User not found with ID: " + userId);
        }
    }
    /**
     * Gets the profile picture path for a user.
     *
     * This method retrieves the profile picture path for the user with the specified ID.
     *
     * @param userId The ID of the user.
     * @return The profile picture path.
     * @throws UsernameNotFoundException if the user is not found.
     */
    public String getProfilePicturePath(int userId) {
        Optional<Users> user = usersRepository.findById(userId);
        if (user.isPresent()) {
            return user.get().getUserDetails().getProfilePicture();
        } else {
            log.error("User not found with ID: {}", userId);
            throw new UsernameNotFoundException("User not found with ID: " + userId);
        }
    }
}
