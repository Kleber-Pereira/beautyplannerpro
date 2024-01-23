package com.tcc.beautyplannerpro.activity.data;

import com.tcc.beautyplannerpro.activity.data.Result;
import com.tcc.beautyplannerpro.activity.data.model.LoggedInUser;

import java.util.UUID;

/**
 * Class that handles authentication with login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {
        try {
            // TODO: Handle loggedInUser authentication
            LoggedInUser fakeUser = new LoggedInUser(UUID.randomUUID().toString(), "Jane Doe");
            return Result.success(fakeUser);
        } catch (Throwable e) {
            return Result.error(null);
        }
    }

    public void logout() {
        // TODO: Revoke authentication
    }
}