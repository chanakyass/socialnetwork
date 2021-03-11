package com.springboot.rest.model.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.sun.istack.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "comments")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@commentId")
public class Comment implements ApiResourceMarker {

    @Id
    @SequenceGenerator(name = "comment_sequence", sequenceName = "comment_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_sequence")
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "owned_by_user", foreignKey = @ForeignKey(name = "fk_owner_user_id"), nullable = false)
    private User owner;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "parent_comment", foreignKey = @ForeignKey(name = "fk_parent_comment_id"))
    Comment parentComment;

    @Column(nullable = false)
    private String commentContent;
    private LocalDate commentedOnDate;
    private LocalDate modifiedOnDate;
    private Long noOfLikes;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "commented_on_post", foreignKey = @ForeignKey(name = "fk_parent_post_id"), nullable = false)
    private Post commentedOn;

    public Comment() {
    }

    public Comment(Long id, User owner, Comment parentComment, String commentContent, LocalDate commentedOnDate, LocalDate modifiedOnDate, Long noOfLikes, Post commentedOn) {
        this.id = id;
        this.owner = owner;
        this.parentComment = parentComment;
        this.commentContent = commentContent;
        this.commentedOnDate = commentedOnDate;
        this.modifiedOnDate = modifiedOnDate;
        this.noOfLikes = noOfLikes;
        this.commentedOn = commentedOn;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDate getCommentedOnDate() {
        return commentedOnDate;
    }

    @JsonDeserialize(using = LocalDateDeserializer.class)
    public void setCommentedOnDate(LocalDate commentedOnDate) {
        this.commentedOnDate = commentedOnDate;
    }

    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDate getModifiedOnDate() {
        return modifiedOnDate;
    }

    @JsonDeserialize(using = LocalDateDeserializer.class)
    public void setModifiedOnDate(LocalDate modifiedOnDate) {
        this.modifiedOnDate = modifiedOnDate;
    }

    public Long getNoOfLikes() {
        return noOfLikes;
    }

    public void setNoOfLikes(Long noOfLikes) {
        this.noOfLikes = noOfLikes;
    }

    public Post getCommentedOn() {
        return commentedOn;
    }

    public void setCommentedOn(Post commentedOn) {
        this.commentedOn = commentedOn;
    }
}
