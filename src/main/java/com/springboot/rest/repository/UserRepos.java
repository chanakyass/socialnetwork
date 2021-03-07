package com.springboot.rest.repository;

import com.springboot.rest.model.entities.User;
import com.springboot.rest.model.projections.UserView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepos extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    Optional<UserView> findUserById(Long id);


}
