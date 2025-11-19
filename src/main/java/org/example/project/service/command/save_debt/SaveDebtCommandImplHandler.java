package org.example.project.service.command.save_debt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.project.domain.Debts;
import org.example.project.domain.History;
import org.example.project.repository.DebtRepository;
import org.example.project.repository.HistoryRepository;
import org.example.project.service.HistoryService;
import org.example.project.service.TelegramMessageSender;
import org.example.project.service.UserService;
import org.example.project.service.UserSessionService;
import org.example.project.service.command.CommandHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class SaveDebtCommandImplHandler implements SaveDebtCommandHandler, CommandHandler {

    private final ReplyKeyboardMarkup mainMenuKeyboard;
    private final DebtRepository debtRepository;
    private final UserSessionService userSessionService;
    private final UserService userService;
    private final HistoryRepository historyRepository;
    private final TelegramMessageSender messageSender;

    @Override
    public void handleCommand(long chatId, Update update) {
        var description = update.getMessage().getText();
        var firstPerson = userService.getUserByChatId(chatId);
        var secondPerson = userService.getUserByName(userSessionService.getSession(chatId).getAnotherPerson());
        var debtType = userSessionService.getSession(chatId).getDebtType();
        var amount = userSessionService.getSession(chatId).getAmount();
        var debt = new Debts();
        var history = new History();
        history.setDescription(description);
        history.setCreatedAt(LocalDateTime.now());
        history.setAmount(amount);
        if (debtType) {
            debt = debtRepository.getDebtByFromAndToIds(firstPerson.getId(), secondPerson.getId()).get();
            history.setNameFrom(firstPerson.getName());
            history.setNameTo(secondPerson.getName());
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append("Пользователь ").append(firstPerson.getName()).append(" добавил ваш долг ему ")
                    .append(amount).append("\n").append("Описание: ").append(description);
            messageSender.sendMessage(secondPerson.getChatId(), builder.toString());
            debt = debtRepository.getDebtByFromAndToIds(secondPerson.getId(), firstPerson.getId()).get();
            history.setNameFrom(secondPerson.getName());
            history.setNameTo(firstPerson.getName());
        }
        debt.setAmount(debt.getAmount().add(amount));
        debtRepository.save(debt);
        userSessionService.setDefaultState(chatId);
        historyRepository.save(history);
        String answer = "Долг добавлен";
        messageSender.sendMessageWithKeyboard(chatId, answer, mainMenuKeyboard);
    }

    @Override
    public boolean canHandle(String command) {
        return command.charAt(0) >= '0' && command.charAt(0) <= '9';
    }
}
