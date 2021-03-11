package com.springboot.rest.model.entities;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.sun.istack.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@JsonInclude(value = Include.NON_EMPTY)
@Table(name = "posts")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@postId")
public class Post implements ApiResourceMarker {

    @Id
    @SequenceGenerator(name = "post_sequence", sequenceName = "post_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_sequence")
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "owned_by_user", foreignKey = @ForeignKey(name = "fk_owner_user_id"), nullable = false)
    private User owner;

    @Column(nullable = false)
    private String postHeading;

    @Column(nullable = false)
    private String postBody;

    private LocalDate postedOnDate;

    private LocalDate modifiedOnDate;
    private Long noOfLikes;

    public Post() {

    }

    public Post(Long id, User owner, String postHeading, String postBody, LocalDate postedOnDate, LocalDate modifiedOnDate, Long noOfLikes) {
        this.id = id;
        this.owner = owner;
        this.postHeading = postHeading;
        this.postBody = postBody;
        this.postedOnDate = postedOnDate;
        this.modifiedOnDate = modifiedOnDate;
        this.noOfLikes = noOfLikes;
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
    public LocalDate getPostedOnDate() {
        return postedOnDate;
    }

    @JsonDeserialize(using = LocalDateDeserializer.class)
    public void setPostedOnDate(LocalDate postedOnDate) {
        this.postedOnDate = postedOnDate;
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
        this.noOfLikes = Optional.of(noOfLikes).orElse(0L);
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", owner=" + owner +
                ", postHeading='" + postHeading + '\'' +
                ", postBody='" + postBody + '\'' +
                ", postedOnDate=" + postedOnDate +
                ", modifiedOnDate=" + modifiedOnDate +
                ", noOfLikes=" + noOfLikes +
                '}';
    }
}
