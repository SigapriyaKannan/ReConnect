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

    public String getResumePath(int userId) {
        Optional<Users> user = usersRepository.findById(userId);
        if (user.isPresent()) {
            return user.get().getUserDetails().getResume();
        } else {
            log.error("User not found with ID: {}", userId);
            throw new UsernameNotFoundException("User not found with ID: " + userId);
        }
    }

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
