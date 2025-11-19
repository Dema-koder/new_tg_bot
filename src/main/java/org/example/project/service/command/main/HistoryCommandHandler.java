package org.example.project.service.command.main;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.project.service.HistoryService;
import org.example.project.service.TelegramMessageSender;
import org.example.project.service.command.CommandHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Slf4j
@Component
@RequiredArgsConstructor
public class HistoryCommandHandler implements MainCommandHandler, CommandHandler {

    private final HistoryService historyService;
    private final TelegramMessageSender messageSender;
    private final ReplyKeyboardMarkup mainMenuKeyboard;

    @Override
    public void handleCommand(long chatId, Update update) {
        String answer = historyService.getAllRecords();
        messageSender.sendMessageWithKeyboard(chatId, answer, mainMenuKeyboard);
    }

    @Override
    public boolean canHandle(String command) {
        return command.equals("История");
    }
}
