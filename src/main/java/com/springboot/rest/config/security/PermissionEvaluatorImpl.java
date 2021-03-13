package com.springboot.rest.config.security;

import com.springboot.rest.config.exceptions.ApiAccessException;
import com.springboot.rest.model.dto.ApiResourceMarker;
import com.springboot.rest.model.entities.SecureResource;
import com.springboot.rest.model.entities.User;
import com.springboot.rest.model.dto.UserPersonalMarker;
import com.springboot.rest.repository.SecureResourceRepos;
import com.springboot.rest.repository.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.Serializable;
import java.util.Optional;

@Component("permissionEvaluator")
public class PermissionEvaluatorImpl implements PermissionEvaluator {

    private final UserRepos userRepos;
    private final SecureResourceRepos secureResourceRepos;
    private final HandlerExceptionResolver exceptionHandler;

    @Autowired
    public PermissionEvaluatorImpl(UserRepos userRepos, SecureResourceRepos secureResourceRepos, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionHandler) {
        this.userRepos = userRepos;
        this.secureResourceRepos = secureResourceRepos;
        this.exceptionHandler = exceptionHandler;

    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {

            if (authentication != null) {
                UserDetails loggedUserDetails = (UserDetails) authentication.getPrincipal();

                //Gathering user details and user id of the logged in user
                //access denied if user not logged in

                Long loggedInUserId = null;

                if (loggedUserDetails == null || targetDomainObject == null)
                    throw new AccessDeniedException("Access is denied");

                User loggedUser = userRepos.findUserByEmail(loggedUserDetails.getUsername()).orElse(null);

                if (loggedUser == null || loggedUser.getId() == null) {
                    throw new AccessDeniedException("Access is denied");
                } else loggedInUserId = loggedUser.getId();

                //Check which class resource belongs to

                if (targetDomainObject instanceof ApiResourceMarker) {
                    ApiResourceMarker resource = (ApiResourceMarker) targetDomainObject;
                    Optional<User> optionalUser = userRepos.findById(resource.getOwnerId());

                    if (optionalUser.isEmpty() || loggedInUserId.compareTo(optionalUser.get().getId()) != 0) {
                        throw new AccessDeniedException("Access is denied");
                    }

                    if (resource.getId() != null) {

                        //Get the secured resource details like id, owner, etc from the secure_resource table

                        SecureResource secureResource = secureResourceRepos.findByResourceTypeAndId(resource.getId()).orElse(null);

                        if (secureResource != null)
                            //check if the resource belongs to the logged in user
                            if( secureResource.getOwner().getId().compareTo(loggedInUserId) != 0)
                                throw new AccessDeniedException("Access is denied");
                            else return true;
                        return true;
                    }
                    return true;
                } else if (targetDomainObject instanceof UserPersonalMarker) {

                    //If object is user profile then we only need to check if logged in user is trying to change the owner of the resource

                    UserPersonalMarker targetUser = (UserPersonalMarker) targetDomainObject;

                    if (targetUser.getId() != null) {

                        if(targetUser.getId().compareTo(loggedInUserId) != 0)
                            throw new AccessDeniedException("Access is denied");
                        else return true;
                    }
                    return true;
                } else throw new AccessDeniedException("Access is denied");

            }

            return false;
        }


    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if(targetId != null)
        {
            Long resourceId = (Long) targetId;
            UserDetails loggedUserDetails = (UserDetails) authentication.getPrincipal();

            //Gathering user details and user id of the logged in user
            //access denied if user not logged in

            Long loggedInUserId = null;

            if (loggedUserDetails == null )
                throw new AccessDeniedException("Access is denied");

            User loggedUser = userRepos.findUserByEmail(loggedUserDetails.getUsername()).orElse(null);

            if (loggedUser == null || loggedUser.getId() == null) {
                throw new AccessDeniedException("Access is denied");
            } else loggedInUserId = loggedUser.getId();


            if(targetType.equals("ApiResourceMarker"))
            {
                SecureResource secureResource = secureResourceRepos.findByResourceTypeAndId(resourceId).orElse(null);

                if (secureResource != null)
                    //check if the resource belongs to the logged in user
                    if( secureResource.getOwner().getId().compareTo(loggedInUserId) != 0)
                        throw new AccessDeniedException("Access is denied");
                    else return true;
                return true;
            }
            else if(targetType.equals("UserPersonalMarker")){
                if(resourceId.compareTo(loggedInUserId) != 0)
                {
                    throw new AccessDeniedException("Access is denied");
                }
                else return true;
            }
            else throw new ApiAccessException("Access is denied");
        }
        return false;
    }

}
