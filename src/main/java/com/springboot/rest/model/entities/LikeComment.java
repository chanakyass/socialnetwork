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
@Table(name = "likes_on_comments", indexes = { @Index(name = "liked_comment_index", columnList = "liked_comment"),
        @Index(name = "liked_comment_user_index", columnList = "liked_comment, owned_by_user")})
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
}
