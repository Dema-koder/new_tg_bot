package org.example.project.service.command.main;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.project.repository.DebtRepository;
import org.example.project.service.DebtService;
import org.example.project.service.TelegramMessageSender;
import org.example.project.service.UserSessionService;
import org.example.project.service.command.CommandHandler;
import org.example.project.service.state.DialogMode;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Slf4j
@Component
@RequiredArgsConstructor
public class CalcCommandHandler implements MainCommandHandler, CommandHandler {
    private final ReplyKeyboardMarkup mainMenuKeyboard;
    private final TelegramMessageSender messageSender;
    private final DebtService debtService;

    @Override
    public void handleCommand(long chatId, Update update) {
        String answer = debtService.calc();
        messageSender.sendMessageWithKeyboard(chatId, answer, mainMenuKeyboard);
    }

    @Override
    public boolean canHandle(String command) {
        return command.equals("Расчет");
    }
}
