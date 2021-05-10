package com.infrastructure.backend.service;

import com.github.fge.jsonpatch.JsonPatch;
import com.infrastructure.backend.entity.user.User;
import com.infrastructure.backend.model.common.request.UserAddition;
import com.infrastructure.backend.model.common.response.CommonResponse;
import com.infrastructure.backend.model.user.request.ChangePassword;
import com.infrastructure.backend.model.user.request.UserLogin;
import com.infrastructure.backend.model.user.response.UserTokenState;

import java.util.List;

public interface UserService {

    UserTokenState login(UserLogin userLogin);

    UserTokenState refreshToken(String token);

    CommonResponse changePassword(String token, ChangePassword changePasswordJson);

    User create(UserAddition user);

    User update(int userId, JsonPatch user);

    User delete(int userId);

    List<User> findAll();

}
