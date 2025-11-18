package org.example.project.repository;

import org.example.project.domain.Debts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DebtRepository extends JpaRepository<Debts, Long> {
    @Query(value = "select * from debts where from_user_id = :fromUserId and to_user_id = :toUserId", nativeQuery = true)
    Optional<Debts> getDebtByFromAndToIds(Long fromUserId, Long toUserId);

    @Query(value = "select * from debts", nativeQuery = true)
    List<Debts> getAllDebts();
}
