package com.example.queuebackend.repo

import com.example.queuebackend.entity.EnterpriseEntity
import org.springframework.data.jpa.repository.JpaRepository

interface EnterpriseRepo : JpaRepository<EnterpriseEntity, Long>