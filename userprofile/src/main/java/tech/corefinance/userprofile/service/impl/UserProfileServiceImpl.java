package tech.corefinance.userprofile.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.userprofile.repository.UserProfileRepository;
import tech.corefinance.userprofile.service.UserProfileService;

@Transactional
@Service
public class UserProfileServiceImpl implements UserProfileService {
    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    public UserProfileRepository getRepository() {
        return userProfileRepository;
    }
}
