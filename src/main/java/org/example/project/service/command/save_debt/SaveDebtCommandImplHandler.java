package org.example.project.service.command.save_debt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.project.domain.Debts;
import org.example.project.repository.DebtRepository;
import org.example.project.service.TelegramMessageSender;
import org.example.project.service.UserService;
import org.example.project.service.UserSessionService;
import org.example.project.service.command.CommandHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class SaveDebtCommandImplHandler implements SaveDebtCommandHandler, CommandHandler {

    private final ReplyKeyboardMarkup mainMenuKeyboard;
    private final DebtRepository debtRepository;
    private final UserSessionService userSessionService;
    private final UserService userService;
    private final TelegramMessageSender messageSender;

    @Override
    public void handleCommand(long chatId, Update update) {
        var amount = BigDecimal.valueOf(Double.parseDouble(update.getMessage().getText()));
        var firstPerson = userService.getUserByChatId(chatId);
        var secondPerson = userService.getUserByName(userSessionService.getSession(chatId).getAnotherPerson());
        var debtType = userSessionService.getSession(chatId).getDebtType();
        var debt = new Debts();
        if (debtType) {
            debt = debtRepository.getDebtByFromAndToIds(secondPerson.getId(), firstPerson.getId()).get();
        } else {
            debt = debtRepository.getDebtByFromAndToIds(firstPerson.getId(), secondPerson.getId()).get();
        }
        debt.setAmount(debt.getAmount().add(amount));
        debtRepository.save(debt);
        userSessionService.setDefaultState(chatId);
        String answer = "Долг добавлен";
        messageSender.sendMessageWithKeyboard(chatId, answer, mainMenuKeyboard);
    }

    @Override
    public boolean canHandle(String command) {
        // TODO: добавить проверку на число
        return true;
    }
}
