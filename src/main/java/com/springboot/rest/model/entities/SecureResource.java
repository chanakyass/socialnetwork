package com.springboot.rest.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "secure_resource", indexes = {
        @Index(name = "secure_post_index", columnList = "post_id"),
        @Index(name = "secure_comment_index", columnList = "comment_id"),
        @Index(name = "secure_like_post_index", columnList = "like_post_id"),
        @Index(name = "secure_like_comment_index", columnList = "like_comment_id")
})
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
    @JoinColumn(name = "like_post_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_liked_post_id"))
    Post likePost;

    @OneToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "like_comment_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_liked_comment_id"))
    Comment likeComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "owner_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_resource_owner_id"))
    User owner;
}
