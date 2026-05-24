package service;

import java.util.List;

public interface NotificationService {
    void notify(String message);
    List<String> getNotifications();
}