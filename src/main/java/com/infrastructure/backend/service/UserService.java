package com.infrastructure.backend.service;

import com.infrastructure.backend.model.common.response.CommonResponse;
import com.infrastructure.backend.model.user.request.ChangePassword;
import com.infrastructure.backend.model.user.request.UserLogin;
import com.infrastructure.backend.model.user.response.UserTokenState;

public interface UserService {

    UserTokenState login(UserLogin userLogin);

    UserTokenState refreshToken(String token);

    CommonResponse changePassword(String token, ChangePassword changePasswordJson);
}
