package com.tcc.beautyplannerpro.activity.data;


import com.tcc.beautyplannerpro.activity.data.model.LoggedInUser;

public class LoginRepository {
    private LoginDataSource dataSource;
    private LoggedInUser user;

    public LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
        this.user = null;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    public Result<LoggedInUser> login(String username, String password) {
        // handle login
        Result<LoggedInUser> result = dataSource.login(username, password);

        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }

        return result;
    }

    private void setLoggedInUser(LoggedInUser loggedInUser) {
        this.user = loggedInUser;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}
