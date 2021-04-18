package com.springboot.rest.model.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@JsonInclude(value = Include.NON_EMPTY)
@Table(name = "posts")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@postId")
public class Post {

    @Id
    @SequenceGenerator(name = "post_sequence", sequenceName = "post_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_sequence")
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "owned_by_user", foreignKey = @ForeignKey(name = "fk_post_owner_id"), nullable = false)
    private User owner;

    @Column(nullable = false)
    private String postHeading;

    @Column(nullable = false, length = 65536)
    private String postBody;

    private LocalDateTime postedAtTime;

    private LocalDateTime modifiedAtTime;

    public Post() {

    }

    public Post(Long id, User owner, String postHeading, String postBody, LocalDateTime postedAtTime, LocalDateTime modifiedAtTime) {
        this.id = id;
        this.owner = owner;
        this.postHeading = postHeading;
        this.postBody = postBody;
        this.postedAtTime = postedAtTime;
        this.modifiedAtTime = modifiedAtTime;
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


    public String getPostHeading() {
        return postHeading;
    }

    public void setPostHeading(String postHeading) {
        this.postHeading = postHeading;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDateTime getPostedAtTime() {
        return postedAtTime;
    }

    @JsonDeserialize(using = LocalDateDeserializer.class)
    public void setPostedAtTime(LocalDateTime postedOnDate) {
        this.postedAtTime = postedOnDate;
    }

    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDateTime getModifiedAtTime() {
        return modifiedAtTime;
    }

    @JsonDeserialize(using = LocalDateDeserializer.class)
    public void setModifiedAtTime(LocalDateTime modifiedOnDate) {
        this.modifiedAtTime = modifiedOnDate;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", owner=" + owner +
                ", postHeading='" + postHeading + '\'' +
                ", postBody='" + postBody + '\'' +
                ", postedOnDate=" + postedAtTime +
                ", modifiedOnDate=" + modifiedAtTime +
                //", noOfLikes=" + noOfLikes +
                '}';
    }
}
