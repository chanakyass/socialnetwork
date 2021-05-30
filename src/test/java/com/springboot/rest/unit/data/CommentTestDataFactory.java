package com.springboot.rest.unit.data;

import com.springboot.rest.model.dto.comment.CommentDto;
import com.springboot.rest.model.dto.comment.CommentEditDto;
import com.springboot.rest.model.dto.post.PostProxyDto;
import com.springboot.rest.model.dto.user.UserProxyDto;
import com.springboot.rest.model.entities.Comment;
import com.springboot.rest.model.entities.Post;
import com.springboot.rest.model.entities.User;
import com.springboot.rest.model.projections.CommentView;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentTestDataFactory {
    private final UserTestDataFactory userTestDataFactory;
    private final PostTestDataFactory postTestDataFactory;

    public CommentTestDataFactory(UserTestDataFactory userTestDataFactory, PostTestDataFactory postTestDataFactory) {
        this.userTestDataFactory = userTestDataFactory;
        this.postTestDataFactory = postTestDataFactory;
    }

    public Comment getRandomComment(){
        Post post = postTestDataFactory.getRandomPost();
        User otherUser = userTestDataFactory.getRandomOtherUser();

        return new Comment(1L, otherUser, null, "Comment content", LocalDateTime.now(),
                null, "dummy", post);
    }

    public Comment getRandomComment(Long i) {
        Comment comment = getRandomComment();
        comment.setId(i);
        comment.setCommentContent(comment.getCommentContent()+" "+i);
        return comment;
    }

    public CommentDto getRandomCommentDto() {
        PostProxyDto postProxyDto = postTestDataFactory.getRandomPostDto();
        UserProxyDto otherUserProxyDto = userTestDataFactory.getRandomOtherUserProxyDto();

        return new CommentDto(1L, otherUserProxyDto, LocalDateTime.now(), null,
                postProxyDto,null, "Comment content", 0L, 0L, false);

    }

    public CommentDto getRandomCommentDto(Long i){
        CommentDto commentDto = getRandomCommentDto();
        commentDto.setId(i);
        commentDto.setCommentContent(commentDto.getCommentContent()+" "+i);
        return commentDto;
    }

    public List<CommentDto> getRandomListOfCommentDtos(){
        List<CommentDto> list = new ArrayList<>();
        for(long i=1; i<=10; i++){
            list.add(getRandomCommentDto(i));
        }
        return list;
    }

    public CommentEditDto getRandomCommentEditDto(){
        CommentDto commentDto = getRandomCommentDto();
        CommentEditDto commentEditDto = new CommentEditDto();
        commentEditDto.setId(commentDto.getId());
        commentEditDto.setCommentContent(commentDto.getCommentContent());
        commentEditDto.setModifiedAtTime(commentDto.getModifiedAtTime());
        commentEditDto.setOwner(commentDto.getOwner());
        return commentEditDto;
    }

    public static void editComment(CommentEditDto commentEditDto, Comment comment){
        commentEditDto.setCommentContent("Updated comment content");
        comment.setCommentContent("Updated comment content");
        commentEditDto.setModifiedAtTime(LocalDateTime.now());
        comment.setModifiedAtTime(LocalDateTime.now());
    }

    public CommentView getRandomCommentView(){
        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        CommentView projection = factory.createProjection(CommentView.class);
        projection.setComment(getRandomComment());
        projection.setCommentLikedByCurrentUser(false);
        projection.setNoOfReplies(0L);
        projection.setNoOfLikes(0L);
        return projection;
    }

    public CommentView getRandomCommentView(Long i){
        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        CommentView projection = factory.createProjection(CommentView.class);
        projection.setComment(getRandomComment(i));
        projection.setCommentLikedByCurrentUser(false);
        projection.setNoOfReplies(0L);
        projection.setNoOfLikes(0L);
        return projection;
    }

    public List<CommentView> getListOfCommentViews(){
        List<CommentView> commentViewList = new ArrayList<>();
        for(long i=1; i<=10; i++){
            commentViewList.add(getRandomCommentView(i));
        }
        return commentViewList;
    }
}
