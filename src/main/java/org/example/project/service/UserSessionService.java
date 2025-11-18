package org.example.project.service;

import org.example.project.data.UserSession;
import org.example.project.service.state.DialogMode;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserSessionService {
    private final ConcurrentHashMap<Long, UserSession> userSessions = new ConcurrentHashMap<>();

    public UserSession getSession(long chatId) {
        return userSessions.computeIfAbsent(chatId, k -> new UserSession(DialogMode.MAIN, null, null));
    }

    public void setDialogMode(long chatId, DialogMode mode) {
        userSessions.computeIfPresent(chatId, (k, userSession) -> new UserSession(mode, userSession.getAnotherPerson(), userSession.getDebtType()));
    }

    public void setAnotherPerson(long chatId, String anotherPerson) {
        userSessions.computeIfPresent(chatId, (k, userSession) -> new UserSession(userSession.getDialogMode(), anotherPerson, userSession.getDebtType()));
    }

    public void setDebtType(long chatId, Boolean debtType) {
        userSessions.computeIfPresent(chatId, (k, userSession) -> new UserSession(userSession.getDialogMode(), userSession.getAnotherPerson(), debtType));
    }

    public void setDefaultState(long chatId) {
        userSessions.computeIfPresent(chatId, (k, userSession) -> new UserSession(DialogMode.MAIN, null, null));
    }
}
