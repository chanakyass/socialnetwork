package com.springboot.rest.model.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.istack.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "likes_on_posts")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@likeId")
public class LikePost {

    @Id
    @SequenceGenerator(name = "like_post_sequence", sequenceName = "like_post_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "like_post_sequence")
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    @JoinColumn(name = "owned_by_user", foreignKey = @ForeignKey(name = "fk_owner_user_id"))
    private User owner;

    private LocalDate likedOnDate;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    @JoinColumn(name = "liked_post", foreignKey = @ForeignKey(name = "fk_liked_post"))
    @JsonProperty("likedPost")
    private Post likedPost;


    public LikePost() {
    }

    public LikePost(Long id, User owner, LocalDate likedOnDate, Post likedPost) {
        this.id = id;
        this.owner = owner;
        this.likedOnDate = likedOnDate;
        this.likedPost = likedPost;
    }

    public User getOwner() {
        return owner;
    }

    public LocalDate getLikedOnDate() {
        return likedOnDate;
    }

    public void setLikedOnDate(LocalDate likedOnDate) {
        this.likedOnDate = likedOnDate;
    }

    public Post getLikedPost() {
        return likedPost;
    }

    public void setLikedPost(Post likedPost) {
        this.likedPost = likedPost;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
