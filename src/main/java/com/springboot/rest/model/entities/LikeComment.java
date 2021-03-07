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
@Table(name = "likes_on_comments")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@likeCommentId")
public class LikeComment implements ApiResourceMarker {

    @Id
    @SequenceGenerator(name = "resource_sequence", sequenceName = "resource_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resource_sequence")
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
    @JoinColumn(name = "liked_comment", foreignKey = @ForeignKey(name = "fk_liked_comment"))
    @JsonProperty("likedComment")
    private Comment likedComment;

    public LikeComment(Long id, User owner, LocalDate likedOnDate, Comment likedComment) {
        this.id = id;
        this.owner = owner;
        this.likedOnDate = likedOnDate;
        this.likedComment = likedComment;
    }

    public LikeComment() {
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

    public Comment getLikedComment() {
        return likedComment;
    }

    public void setLikedComment(Comment likedComment) {
        this.likedComment = likedComment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getOwnerId() {
        return owner.getId();
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
