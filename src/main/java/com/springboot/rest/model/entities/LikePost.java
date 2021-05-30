package com.springboot.rest.model.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "likes_on_posts", indexes = { @Index(name = "liked_post_index", columnList = "liked_post"),
                                           @Index(name = "liked_post_user_index", columnList = "liked_post, owned_by_user")} )
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@likeId")
public class LikePost {

    @Id
    @SequenceGenerator(name = "like_post_sequence", sequenceName = "like_post_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "like_post_sequence")
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    @JoinColumn(name = "owned_by_user", foreignKey = @ForeignKey(name = "fk_like_post_owner_id"))
    private User owner;

    private LocalDateTime likedAtTime;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    @JoinColumn(name = "liked_post", foreignKey = @ForeignKey(name = "fk_liked_post"))
    @JsonProperty("likedPost")
    private Post likedPost;
}
