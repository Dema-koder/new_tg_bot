package org.example.project.service.command.save_debt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.project.service.TelegramMessageSender;
import org.example.project.service.UserSessionService;
import org.example.project.service.command.CommandHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class DescriptionCommandHandler  implements SaveDebtCommandHandler, CommandHandler {

    private final UserSessionService userSessionService;
    private final TelegramMessageSender messageSender;

    @Override
    public void handleCommand(long chatId, Update update) {
        var amount = BigDecimal.valueOf(Double.parseDouble(update.getMessage().getText()));
        userSessionService.setAmount(chatId, amount);
        String answer = "Добавьте описание долга";
        messageSender.sendMessage(chatId, answer);
    }

    @Override
    public boolean canHandle(String command) {
        for (int i = 0; i < command.length(); i++)
            if (command.charAt(i) < '0' || command.charAt(i) > '9')
                if (command.charAt(i) != '.')
                    return false;
        return true;
    }
}
