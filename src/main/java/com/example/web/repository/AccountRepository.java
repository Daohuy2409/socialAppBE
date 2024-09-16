package com.example.web.repository;

import com.example.web.entity.Account;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findAccountByUsername(String username);

    @Modifying
    @Transactional
    @Query("update Account a set a.password = :password where a.username = :username")
    void changePass(@Param("username") String username, @Param("password") String password);
}
