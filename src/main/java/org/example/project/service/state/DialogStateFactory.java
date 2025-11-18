package org.example.project.service.state;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DialogStateFactory {
    private final MainState mainState;
    private final DebtState debtState;
    private final MyDebtState myDebtState;
    private final ForeignDebtState foreignDebtState;
    private final SaveDebtState saveDebtState;

    public DialogState getState(DialogMode mode) {
        return switch (mode) {
            case MAIN -> mainState;
            case DEBT -> debtState;
            case MY_DEBT -> myDebtState;
            case FOREIGN_DEBT -> foreignDebtState;
            case SAVE_DEBT -> saveDebtState;
        };
    }
}
