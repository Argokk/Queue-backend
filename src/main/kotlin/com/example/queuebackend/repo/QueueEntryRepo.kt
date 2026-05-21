package com.example.queuebackend.repo

import com.example.queuebackend.entity.QueueEntryEntity
import org.springframework.data.jpa.repository.JpaRepository

interface QueueEntryRepo: JpaRepository<QueueEntryEntity,Long>