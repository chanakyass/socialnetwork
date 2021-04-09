package com.springboot.rest.model.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
public class SecureResource {

    @Id
    @SequenceGenerator(name = "secure_resource_seq",
            sequenceName = "secure_resource_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "secure_resource_seq")
    Long id;

    Character type;

    @OneToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "post_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_post_id"))
    Post post;

    @OneToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "comment_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_comment_id"))
    Comment comment;



    @OneToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "like_post_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_like_post_id"))
    Post likePost;

    @OneToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "like_comment_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_like_comment_id"))
    Comment likeComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "owner_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_resource_owner_id"))
    User owner;

    public SecureResource(Long id, Character type , Post post, Comment comment, Post likePost, Comment likeComment, User owner) {
        this.type = type;
        this.post = post;
        this.comment = comment;
        this.likePost = likePost;
        this.likeComment = likeComment;
        this.owner = owner;
    }

    public SecureResource() {
    }

    public static SecureResourceBuilder builder() {
        return new SecureResourceBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Character getType() {
        return type;
    }

    public void setType(Character type) {
        this.type = type;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Post getLikePost() {
        return likePost;
    }

    public void setLikePost(Post likePost) {
        this.likePost = likePost;
    }

    public Comment getLikeComment() {
        return likeComment;
    }

    public void setLikeComment(Comment likeComment) {
        this.likeComment = likeComment;
    }

    public static class SecureResourceBuilder {
        private Long id;
        private Character type;
        private Post post;
        private Comment comment;
        private Post likePost;
        private Comment likeComment;
        private User owner;

        SecureResourceBuilder() {
        }

        public SecureResourceBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public SecureResourceBuilder type(Character type) {
            this.type = type;
            return this;
        }

        public SecureResourceBuilder post(Post post) {
            this.post = post;
            return this;
        }

        public SecureResourceBuilder comment(Comment comment) {
            this.comment = comment;
            return this;
        }

        public SecureResourceBuilder likePost(Post likePost) {
            this.likePost = likePost;
            return this;
        }

        public SecureResourceBuilder likeComment(Comment likeComment) {
            this.likeComment = likeComment;
            return this;
        }

        public SecureResourceBuilder owner(User owner) {
            this.owner = owner;
            return this;
        }

        public SecureResource build() {
            return new SecureResource(id, type, post, comment, likePost, likeComment, owner);
        }

        public String toString() {
            return "SecureResource.SecureResourceBuilder(id=" + this.id + ", type=" + this.type + ", post=" + this.post + ", comment=" + this.comment + ", likePost=" + this.likePost + ", likeComment=" + this.likeComment + ", owner=" + this.owner + ")";
        }
    }
}
