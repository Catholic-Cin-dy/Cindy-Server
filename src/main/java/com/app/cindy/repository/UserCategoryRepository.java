package com.app.cindy.repository;

import com.app.cindy.domain.user.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {

    @Transactional
    void deleteByIdUserId(Long userId);

}