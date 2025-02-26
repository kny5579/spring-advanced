package org.example.expert.domain.comment.dto.response;

import lombok.Getter;
import org.example.expert.domain.comment.entity.Comment;
import org.example.expert.domain.user.dto.response.UserResponse;

@Getter
public class CommentSaveResponse {

    private final Long id;
    private final String contents;
    private final UserResponse user;

    public CommentSaveResponse(Comment comment) {
        this.id = comment.getId();
        this.contents = comment.getContents();
        this.user = new UserResponse(comment.getUser());
    }
}
