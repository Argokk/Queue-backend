package com.example.queuebackend.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.springframework.context.annotation.Primary

@Entity
@Table(name = "enterprise")
class EnterpriseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = 0
    var name: String? = null
    var url: String? = null
    var locationX: Double? = null
    var locationY: Double? = null
    @OneToOne(mappedBy = "enterprise", fetch = FetchType.LAZY)
    var queue: QueueEntity? = null
}