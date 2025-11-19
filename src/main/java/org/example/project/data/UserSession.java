package org.example.project.data;

import com.plexpt.chatgpt.entity.chat.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.project.service.state.DialogMode;

import java.math.BigDecimal;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSession {
    private DialogMode dialogMode;
    private String anotherPerson;
    private BigDecimal amount;
    private Boolean debtType; // true - я должен, false - мне должны
}
