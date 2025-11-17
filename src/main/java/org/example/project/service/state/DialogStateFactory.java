package org.example.project.service.state;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DialogStateFactory {
    private final MainState mainState;

    public DialogState getState(DialogMode mode) {
        return switch (mode) {
            case MAIN -> mainState;
        };
    }
}
