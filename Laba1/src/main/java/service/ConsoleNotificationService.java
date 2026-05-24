package service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ConsoleNotificationService implements NotificationService {
    private final List<String> notifications = new ArrayList<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void notify(String message) {
        String formatted = String.format("[%s] %s", LocalDateTime.now().format(formatter), message);
        notifications.add(formatted);
        System.out.println(formatted);
    }

    @Override
    public List<String> getNotifications() {
        return new ArrayList<>(notifications);
    }
}