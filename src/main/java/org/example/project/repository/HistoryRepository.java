package org.example.project.repository;

import org.example.project.domain.History;
import org.example.project.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    @Query(value = "select * from history", nativeQuery = true)
    List<History> getAllRecords();
}
