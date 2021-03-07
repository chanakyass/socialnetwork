package com.springboot.rest.model.dto;

import com.springboot.rest.model.entities.Comment;
import com.springboot.rest.model.entities.Post;
import com.springboot.rest.model.entities.User;

import java.time.LocalDate;
import java.util.Objects;

public class CommentEdit{

    Long id;

    User owner;

    String commentContent;

    LocalDate commentedOnDate;

    LocalDate modifiedOnDate;

    Long noOfLikes;

    Post commentedOn;

    Comment parentComment;

    public CommentEdit(Long id, User owner, String commentContent, LocalDate commentedOnDate, LocalDate modifiedOnDate, Long noOfLikes, Post commentedOn, Comment parentComment) {
        this.id = id;
        this.owner = owner;
        this.commentContent = commentContent;
        this.commentedOnDate = commentedOnDate;
        this.modifiedOnDate = modifiedOnDate;
        this.noOfLikes = noOfLikes;
        this.commentedOn = commentedOn;
        this.parentComment = parentComment;
    }

    public CommentEdit() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentEdit)) return false;
        CommentEdit that = (CommentEdit) o;
        return getId().equals(that.getId()) && getOwner().equals(that.getOwner());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOwner());
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

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public LocalDate getCommentedOnDate() {
        return commentedOnDate;
    }

    public void setCommentedOnDate(LocalDate commentedOnDate) {
        this.commentedOnDate = commentedOnDate;
    }

    public LocalDate getModifiedOnDate() {
        return modifiedOnDate;
    }

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

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }
}
