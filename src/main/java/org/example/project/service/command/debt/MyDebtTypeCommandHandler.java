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
public class MyDebtTypeCommandHandler implements DebtCommandHandler, CommandHandler {

    private final UserSessionService userSessionService;
    private final UserService userService;
    private final TelegramMessageSender messageSender;

    @Override
    public void handleCommand(long chatId, Update update) {
        String answer = "Выбери кому ты должен(-на)";
        userSessionService.setDialogMode(chatId, DialogMode.MY_DEBT);
        userSessionService.setDebtType(chatId, Boolean.TRUE);
        messageSender.sendMessageWithKeyboard(chatId, answer, userService.choosePersonKeyboard(chatId));
    }

    @Override
    public boolean canHandle(String command) {
        return command.equals("Я должен(-на)");
    }
}
