package org.example.project.repository;

import org.example.project.domain.Users;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    @NotNull
    Optional<Users> findById(@NotNull Long id);
    Users findByName(String name);
    Users findByChatId(Long chatId);
    @Query(value = "select * from users", nativeQuery = true)
    List<Users> getAllUsers();
}
