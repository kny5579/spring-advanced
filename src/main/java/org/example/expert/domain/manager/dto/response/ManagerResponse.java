package org.example.expert.domain.manager.dto.response;

import lombok.Getter;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;

@Getter
public class ManagerResponse {

    private final Long id;
    private final UserResponse user;

    public ManagerResponse(Manager manager) {
        this.id = manager.getId();
        this.user = new UserResponse(manager.getUser());
    }

    //test 코드용
    public ManagerResponse(long id, UserResponse user) {
        this.id = id;
        this.user = new UserResponse(new User());
    }
}
