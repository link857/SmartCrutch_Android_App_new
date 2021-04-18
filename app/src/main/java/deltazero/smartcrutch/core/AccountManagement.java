package deltazero.smartcrutch.core;

public class AccountManagement {

    public enum LoginStatus {
        SUCCESS,
        USER_NOT_EXIST,
        PASSWORD_ERROR,
        VALIDATION_ERROR,
        UNKNOWN_ERROR,
        NETWORK_ERROR
    }

    public static class LoginResult {

        public LoginStatus status;
        public String msg;

        public LoginResult (LoginStatus status, String msg) {
            this.status = status;
            this.msg = msg;
        }
    }

    public static LoginResult login(String username, String password) {
        return new LoginResult(LoginStatus.PASSWORD_ERROR, "password error");
    }

}