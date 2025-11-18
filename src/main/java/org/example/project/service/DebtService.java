package org.example.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.project.repository.DebtRepository;
import org.example.project.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class DebtService {
    private final UserRepository userRepository;
    private final DebtRepository debtRepository;

    private void rebalancing() {
        var debts = debtRepository.getAllDebts();

        for (int i = 0; i < 100; i++) {

            // 1 часть. Взаимные долги
            for (var debt : debts)
                for (var debt2 : debts)
                    if (!debt.equals(debt2))
                        if (Objects.equals(debt2.getFrom().getId(), debt.getTo().getId()) &&
                                Objects.equals(debt.getFrom().getId(), debt2.getTo().getId())) {
                            var amount1 = debt.getAmount();
                            var amount2 = debt2.getAmount();
                            if (amount1.compareTo(amount2) < 0) {
                                debt2.setAmount(amount2.subtract(amount1));
                                debt.setAmount(BigDecimal.ZERO);
                            } else {
                                debt.setAmount(amount1.subtract(amount2));
                                debt2.setAmount(BigDecimal.ZERO);
                            }
                        }

            for (var debt1 : debts)
                for (var debt2 : debts)
                    if (!debt1.equals(debt2))
                        if (Objects.equals(debt2.getFrom().getId(), debt1.getTo().getId()) &&
                                !Objects.equals(debt1.getFrom().getId(), debt2.getTo().getId())) {
                            var amount1 = debt1.getAmount();
                            var amount2 = debt2.getAmount();
                            if (amount1.compareTo(BigDecimal.ZERO) == 0 || amount2.compareTo(BigDecimal.ZERO) == 0)
                                continue;
                            if (amount1.compareTo(amount2) < 0) {
                                debt2.setAmount(amount2.subtract(amount1));
                                debt1.setAmount(BigDecimal.ZERO);
                                for (var debt3 : debts)
                                    if (Objects.equals(debt3.getFrom().getId(), debt1.getFrom().getId()) &&
                                            Objects.equals(debt3.getTo().getId(), debt2.getTo().getId())) {
                                        debt3.setAmount(amount1);
                                        break;
                                    }
                            } else if (amount1.compareTo(amount2) > 0) {
                                debt1.setAmount(amount1.subtract(amount2));
                                debt2.setAmount(BigDecimal.ZERO);
                                for (var debt3 : debts)
                                    if (Objects.equals(debt3.getFrom().getId(), debt1.getFrom().getId()) &&
                                            Objects.equals(debt3.getTo().getId(), debt2.getTo().getId())) {
                                        debt3.setAmount(amount2);
                                        break;
                                    }
                            }
                        }
        }

        debtRepository.saveAll(debts);
    }

    public String calc() {
        rebalancing();
        var debts = debtRepository.getAllDebts();
        var builder = new StringBuilder();
        for (var debt: debts) {
            var amount = debt.getAmount();
            var from = userRepository.findById(debt.getFrom().getId()).get();
            var to = userRepository.findById(debt.getTo().getId()).get();
            if (amount.intValue() != 0)
                builder.append(from.getName()).append(" должен ").append(to.getName()).append(" ").append(amount).append("\n");
        }
        if (builder.isEmpty())
            return "Никто никому ничего не должен";
        return builder.toString();
    }
}
