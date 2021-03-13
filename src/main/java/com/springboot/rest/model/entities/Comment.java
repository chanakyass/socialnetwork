package com.springboot.rest.model.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "comments")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@commentId")
public class Comment {

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
    private LocalDateTime commentedAtTime;
    private LocalDateTime modifiedAtTime;
    private Long noOfLikes;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "commented_on_post", foreignKey = @ForeignKey(name = "fk_parent_post_id"), nullable = false)
    private Post commentedOn;

    public Comment() {
    }

    public Comment(Long id, User owner, Comment parentComment, String commentContent, LocalDateTime commentedAtTime, LocalDateTime modifiedAtTime, Long noOfLikes, Post commentedOn) {
        this.id = id;
        this.owner = owner;
        this.parentComment = parentComment;
        this.commentContent = commentContent;
        this.commentedAtTime = commentedAtTime;
        this.modifiedAtTime = modifiedAtTime;
        this.noOfLikes = noOfLikes;
        this.commentedOn = commentedOn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public LocalDateTime getCommentedAtTime() {
        return commentedAtTime;
    }

    @JsonDeserialize(using = LocalDateDeserializer.class)
    public void setCommentedAtTime(LocalDateTime commentedOnDate) {
        this.commentedAtTime = commentedOnDate;
    }

    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDateTime getModifiedAtTime() {
        return modifiedAtTime;
    }

    @JsonDeserialize(using = LocalDateDeserializer.class)
    public void setModifiedAtTime(LocalDateTime modifiedOnDate) {
        this.modifiedAtTime = modifiedOnDate;
    }

    public Long getNoOfLikes() {
        return Objects.requireNonNullElse(noOfLikes, 0L);
    }

    public void setNoOfLikes(Long noOfLikes) {
        this.noOfLikes = Objects.requireNonNullElse(noOfLikes, 0L);
    }

    public Post getCommentedOn() {
        return commentedOn;
    }

    public void setCommentedOn(Post commentedOn) {
        this.commentedOn = commentedOn;
    }
}
