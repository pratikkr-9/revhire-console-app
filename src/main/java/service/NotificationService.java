package service;

import dao.NotificationDAO;
import model.Notification;

import exception.DatabaseException;
import exception.ValidationException;

import java.util.List;

public class NotificationService {

    private final NotificationDAO dao = new NotificationDAO();

    public void notify(int userId, String msg)
            throws ValidationException, DatabaseException {

        if (userId <= 0) {
            throw new ValidationException("Invalid user.");
        }
        if (msg == null || msg.isBlank()) {
            throw new ValidationException("Notification message cannot be empty.");
        }

        dao.addNotification(userId, msg);
    }

    public List<Notification> getMyNotifications(int userId)
            throws ValidationException, DatabaseException {

        if (userId <= 0) {
            throw new ValidationException("Invalid user.");
        }

        return dao.getUserNotifications(userId);
    }
}
