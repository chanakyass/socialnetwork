package com.springboot.rest.unit.data;

public class AbstractDataFactory {
    public static UserTestDataFactory provideUserTestDataFactory(){
        return new UserTestDataFactory();
    }

    public static PostTestDataFactory providePostTestDataFactory(){
        return new PostTestDataFactory(provideUserTestDataFactory());
    }

    public static CommentTestDataFactory provideCommentTestDataFactory(){
        return new CommentTestDataFactory(provideUserTestDataFactory(), providePostTestDataFactory());
    }

    public static LikeTestDataFactory provideLikeTestDataFactory(){
        return new LikeTestDataFactory(providePostTestDataFactory(), provideCommentTestDataFactory(), provideUserTestDataFactory());
    }
}
