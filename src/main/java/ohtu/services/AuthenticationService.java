package ohtu.services;

import ohtu.domain.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ohtu.data_access.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationService {

    private UserDao userDao;

    @Autowired
    public AuthenticationService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean logIn(String username, String password) {
        for (User user : userDao.listAll()) {
            if (authentication(username, password, user)) return true;
        }

        return false;
    }

    private boolean authentication(String username, String password, User user) {
        if (user.getUsername().equals(username)
                && user.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    public boolean createUser(String username, String password) {
        if (userDao.findByName(username) != null) {
            return false;
        }
        if (invalid(username, password)) {
            return false;
        }
        userDao.add(new User(username, password));
        return true;
    }

    private boolean invalid(String username, String password) {
        // validity check of username and password
        if (checkValidityOfUsername(username)) return true;
        return checkValidityOfPassword(password);
    }

    private boolean checkValidityOfPassword(String password) {
        if (password.length() < 8) return true;
        Pattern p = Pattern.compile("(.*)((\\d+)|(\\W+))(.*)");
        Matcher m = p.matcher(password);
        return !m.matches();
    }

    private boolean checkValidityOfUsername(String username) {
        if (username.length() < 3) return true;
        return checkCharsInName(username);
    }

    private boolean checkCharsInName(String username) {
        for (int i = 0; i < username.length(); ++i) {
            if (checkSingleChar(username, i)) return true;
        }
        return false;
    }

    private boolean checkSingleChar(String username, int i) {
        if (username.charAt(i) < 97 && username.charAt(i) > 122) {
            return true;
        }
        return false;
    }
}
