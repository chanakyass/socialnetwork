package com.springboot.rest.model.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
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
@Table(name = "comments", indexes = @Index(name = "comment_index", columnList = "id"))
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@commentId")
public class Comment {

    @Id
    @SequenceGenerator(name = "comment_sequence", sequenceName = "comment_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_sequence")
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "owned_by_user", foreignKey = @ForeignKey(name = "fk_comment_owner_id"), nullable = false)
    private User owner;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "parent_comment", foreignKey = @ForeignKey(name = "fk_parent_comment_id"))
    Comment parentComment;

    @Column(nullable = false, length = 5000)
    private String commentContent;
    private LocalDateTime commentedAtTime;
    private LocalDateTime modifiedAtTime;

    private String commentPath;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "commented_on_post", foreignKey = @ForeignKey(name = "fk_parent_post_id"), nullable = false)
    private Post commentedOn;

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
}
