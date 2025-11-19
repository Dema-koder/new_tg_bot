package org.example.project.service;

import lombok.RequiredArgsConstructor;
import org.example.project.domain.History;
import org.example.project.repository.HistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;

    public String getAllRecords() {
        var records = historyRepository.getAllRecords();
        if (records.isEmpty())
            return "История пустая";
        StringBuilder builder = new StringBuilder();
        builder.append("История: \n");
        Integer k = 1;
        for (var record: records) {
            builder.append(k).append(") ").append(record.getNameFrom()).append(" - ").append(record.getAmount())
                    .append(" -> ").append(record.getNameTo()).append("\n").append("Описание: ").append(record.getDescription()).append("\n");
            k++;
        }
        return builder.toString();
    }
}
