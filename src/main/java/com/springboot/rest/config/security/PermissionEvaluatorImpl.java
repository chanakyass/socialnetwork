package com.springboot.rest.config.security;

import com.springboot.rest.model.entities.ApiResource;
import com.springboot.rest.model.entities.SecureResource;
import com.springboot.rest.model.entities.User;
import com.springboot.rest.repository.SecureResourceRepos;
import com.springboot.rest.repository.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Optional;

@Component("permissionEvaluator")
public class PermissionEvaluatorImpl implements PermissionEvaluator {

    private final UserRepos userRepos;
    private final SecureResourceRepos secureResourceRepos;

    @Autowired
    public PermissionEvaluatorImpl(UserRepos userRepos, SecureResourceRepos secureResourceRepos) {
        this.userRepos = userRepos;
        this.secureResourceRepos = secureResourceRepos;

    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {

        if (authentication != null) {
            UserDetails loggedUserDetails = (UserDetails) authentication.getPrincipal();

            //Gathering user details and user id of the logged in user
            //access denied if user not logged in

            Long loggedInUserId = null;

            if (loggedUserDetails == null || targetDomainObject == null)
                return false;

            User loggedUser = userRepos.findUserByEmail(loggedUserDetails.getUsername()).orElse(null);

            if (loggedUser == null || loggedUser.getId() == null) {
                return false;
            }

            else loggedInUserId = loggedUser.getId();

            //Check which class resource belongs to

            if (targetDomainObject instanceof ApiResource) {
                ApiResource resource = (ApiResource) targetDomainObject;
                Optional<User> optionalUser = userRepos.findById(resource.getOwner().getId());

                if (optionalUser.isEmpty() || loggedInUserId.compareTo(optionalUser.get().getId()) != 0) {
                    return false;
                }

                if (resource.getId() != null) {

                    //Get the secured resource details like id, owner, etc from the secure_resource table
                    SecureResource secureResource = secureResourceRepos.findById(resource.getId()).orElse(null);

                    if (secureResource != null)
                        //check if the resource belongs to the logged in user
                        return (secureResource.getOwner().getId().compareTo(loggedInUserId) == 0);
                    return true;
                }
                return true;
            } else if (targetDomainObject instanceof User) {

                //If object is user profile then we only need to check if logged in user is trying to change the owner of the resource

                User targetUser = (User) targetDomainObject;

                if (targetUser.getId() != null) {

                    return targetUser.getId().compareTo(loggedInUserId) == 0;
                }
                return true;
            }
            else return false;

        }

        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return true;
    }

}
