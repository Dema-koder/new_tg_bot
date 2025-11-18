package org.example.project.service.command.foreign_debt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.project.service.TelegramMessageSender;
import org.example.project.service.UserSessionService;
import org.example.project.service.command.CommandHandler;
import org.example.project.service.state.DialogMode;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class ForeignDebtCommandImplHandler implements ForeignDebtCommandHandler, CommandHandler {

    private final UserSessionService userSessionService;
    private final TelegramMessageSender messageSender;

    @Override
    public void handleCommand(long chatId, Update update) {
        String anotherPerson = update.getMessage().getText();
        userSessionService.setAnotherPerson(chatId, anotherPerson);
        userSessionService.setDialogMode(chatId, DialogMode.SAVE_DEBT);
        String answer = "Тебе должен " + update.getMessage().getText() + "\nТеперь напиши сумму долга";
        messageSender.sendMessage(chatId, answer);
    }

    @Override
    public boolean canHandle(String command) {
        return true;
    }
}
