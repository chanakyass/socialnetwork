package com.springboot.rest.model.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.istack.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "likes_on_comments")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@likeCommentId")
public class LikeComment {

    @Id
    @SequenceGenerator(name = "like_comment_sequence", sequenceName = "like_comment_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "like_comment_sequence")
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    @JoinColumn(name = "owned_by_user", foreignKey = @ForeignKey(name = "fk_like_comment_owner_id"))
    private User owner;

    private LocalDateTime likedAtTime;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    @JoinColumn(name = "liked_comment", foreignKey = @ForeignKey(name = "fk_liked_comment"))
    @JsonProperty("likedComment")
    private Comment likedComment;

    public LikeComment(Long id, User owner, LocalDateTime likedAtTime, Comment likedComment) {
        this.id = id;
        this.owner = owner;
        this.likedAtTime = likedAtTime;
        this.likedComment = likedComment;
    }

    public LikeComment() {
    }

    public User getOwner() {
        return owner;
    }

    public LocalDateTime getLikedAtTime() {
        return likedAtTime;
    }

    public void setLikedAtTime(LocalDateTime likedOnDate) {
        this.likedAtTime = likedOnDate;
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

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
