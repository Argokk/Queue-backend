package com.example.queuebackend.repo

import com.example.queuebackend.entity.QueueEntity
import com.example.queuebackend.entity.QueueEntryEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface QueueRepo: JpaRepository<QueueEntity,Long>{
    fun findByEnterpriseId(enterpriseId: Long): Optional<QueueEntity>
    fun existsByEnterpriseId(enterpriseId: Long): Boolean
}