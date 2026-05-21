package com.example.queuebackend.entity


import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "queue")
class QueueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long? = 0
    var isEnabled: Boolean? = true
    var averageServiceSeconds: Int? = null
    @OneToMany(mappedBy = "queue")
    var entries: List<QueueEntryEntity>? = mutableListOf()
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id")
    @JsonIgnore
    var enterprise: EnterpriseEntity? = null
}