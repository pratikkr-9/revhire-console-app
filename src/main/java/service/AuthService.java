package service;

import dao.UserDAO;
import model.User;

import exception.AuthenticationException;
import exception.DatabaseException;
import exception.ValidationException;

public class AuthService {

    private final UserDAO dao = new UserDAO();

    public void register(User user)
            throws ValidationException, DatabaseException {

        if (user == null) {
            throw new ValidationException("User details cannot be null.");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Email is required.");
        }
        if (user.getPassword() == null || user.getPassword().length() < 4) {
            throw new ValidationException("Password must be at least 4 characters.");
        }

        dao.register(user);
    }

    public User login(String email, String password)
            throws ValidationException,
                   AuthenticationException,
                   DatabaseException {

        if (email == null || email.isBlank()) {
            throw new ValidationException("Email cannot be empty.");
        }
        if (password == null || password.isBlank()) {
            throw new ValidationException("Password cannot be empty.");
        }

        return dao.login(email, password);
    }
}
