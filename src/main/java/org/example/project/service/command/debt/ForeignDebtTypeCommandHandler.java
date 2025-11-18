package org.example.project.service.command.debt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.project.service.TelegramMessageSender;
import org.example.project.service.UserService;
import org.example.project.service.UserSessionService;
import org.example.project.service.command.CommandHandler;
import org.example.project.service.state.DialogMode;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class ForeignDebtTypeCommandHandler implements DebtCommandHandler, CommandHandler {

    private  final UserSessionService userSessionService;
    private final TelegramMessageSender messageSender;
    private final UserService userService;

    @Override
    public void handleCommand(long chatId, Update update) {
        String answer = "Выбери кто тебе должен";
        userSessionService.setDialogMode(chatId, DialogMode.FOREIGN_DEBT);
        userSessionService.setDebtType(chatId, Boolean.FALSE);
        messageSender.sendMessageWithKeyboard(chatId, answer, userService.choosePersonKeyboard(chatId));
    }

    @Override
    public boolean canHandle(String command) {
        return command.equals("Мне должны");
    }
}
