package com.springboot.rest.model.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "[users]", uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@userId")
public class User implements Serializable{
    @Id
    @SequenceGenerator(name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "user_sequence")
    private Long id;
    private String name;
    @NotNull
    private String profileName;
    private String email;
    @NotNull
    private String password;
    private int age;
    private LocalDate DOB;
    private String userSummary;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @NotNull
    @JoinTable(name = "user_role", joinColumns = {@JoinColumn(name = "role_id")}, inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<Role> grantedAuthoritiesList;

    public User(Long id, String name, String profileName, String email, String password, int age,
                LocalDate DOB, String userSummary, List<Role> grantedAuthoritiesList) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.DOB = DOB;
        this.userSummary = userSummary;
        this.grantedAuthoritiesList = grantedAuthoritiesList;
        this.profileName = profileName;
    }

    public User() {
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDate getDOB() {
        return DOB;
    }

    public void setDOB(LocalDate DOB) {
        this.DOB = DOB;
    }

    public String getUserSummary() {
        return userSummary;
    }

    public void setUserSummary(String userSummary) {
        this.userSummary = userSummary;
    }

    public List<Role> getGrantedAuthoritiesList() {
        return grantedAuthoritiesList;
    }

    public void setGrantedAuthoritiesList(List<Role> grantedAuthoritiesList) {
        this.grantedAuthoritiesList = grantedAuthoritiesList;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public static class UserBuilder {
        private Long id;
        private String name;
        private String profileName;
        private String email;
        private String password;
        private int age;
        private LocalDate DOB;
        private String userSummary;
        private List<Role> grantedAuthoritiesList;

        UserBuilder() {
        }

        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder profileName(String profileName) {
            this.profileName = profileName;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder age(int age) {
            this.age = age;
            return this;
        }

        public UserBuilder DOB(LocalDate DOB) {
            this.DOB = DOB;
            return this;
        }

        public UserBuilder userSummary(String userSummary) {
            this.userSummary = userSummary;
            return this;
        }

        public UserBuilder grantedAuthoritiesList(List<Role> grantedAuthoritiesList) {
            this.grantedAuthoritiesList = grantedAuthoritiesList;
            return this;
        }

        public User build() {
            return new User(id, name, profileName, email, password, age, DOB, userSummary, grantedAuthoritiesList);
        }

        public String toString() {
            return "User.UserBuilder(id=" + this.id + ", name=" + this.name + ", profileName=" + this.profileName + ", email=" + this.email + ", password=" + this.password + ", age=" + this.age + ", DOB=" + this.DOB + ", userSummary=" + this.userSummary + ", grantedAuthoritiesList=" + this.grantedAuthoritiesList + ")";
        }
    }
}
