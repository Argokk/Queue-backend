package com.example.queuebackend.repo

import com.example.queuebackend.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepo: JpaRepository<UserEntity, Long> {
    fun findByUsername(username: String): UserEntity?
    fun existsByUsername(username: String): Boolean
}