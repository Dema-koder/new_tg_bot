package org.example.project.service;


import org.example.project.domain.Debts;
import org.example.project.domain.Users;
import org.example.project.repository.DebtRepository;
import org.example.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DebtRepository debtRepository;

    public Users addUser(Long chatId, String name) {
        Users users = new Users();
        users.setName(name);
        users.setChatId(chatId);
        return userRepository.save(users);
    }

    public void addAllRelationshipsForUser(Long id) {
        var users = userRepository.getAllUsers();
        var currentUser = userRepository.findById(id).get();
        for (Users user: users) {
            if (!Objects.equals(user.getId(), id)) {
                var debtFrom = new Debts();
                debtFrom.setFrom(currentUser);
                debtFrom.setAmount(BigDecimal.valueOf(0));
                debtFrom.setTo(user);
                debtRepository.save(debtFrom);

                var debtTo = new Debts();
                debtTo.setFrom(user);
                debtTo.setTo(currentUser);
                debtTo.setAmount(BigDecimal.valueOf(0));
                debtRepository.save(debtTo);
            }
        }
    }

    public ReplyKeyboardMarkup choosePersonKeyboard(long chatId) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        var users = userRepository.getAllUsers();
        for (var user: users)
            if (user.getChatId() == chatId) {
                users.remove(user);
                break;
            }
        List<KeyboardRow> keyboard = new ArrayList<>();
        for (int i = 0; i < users.size(); i += 2) {
            KeyboardRow row = new KeyboardRow();
            row.add(new KeyboardButton(users.get(i).getName()));
            if (i + 1 < users.size())
                row.add(new KeyboardButton(users.get(i + 1).getName()));
            keyboard.add(row);
        }

        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        return keyboardMarkup;
    }

    public boolean isUserExist(String name) {
        var users = userRepository.getAllUsers();
        for (var user: users)
            if (user.getName().equals(name))
                return true;
        return false;
    }

    public Users getUserByChatId(Long chatId) {
        return userRepository.findByChatId(chatId);
    }

    public Users getUserByName(String name) {
        return userRepository.findByName(name);
    }
}
