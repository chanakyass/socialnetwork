package com.springboot.rest.repository;

import com.springboot.rest.model.entities.SecureResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecureResourceRepos extends JpaRepository<SecureResource, Long> {



}
