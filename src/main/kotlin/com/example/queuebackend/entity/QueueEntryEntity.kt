package com.example.queuebackend.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Table
import com.example.queuebackend.QueueEntryStatus
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import java.time.Instant
import java.util.Queue

@Entity
@Table(name = "queue_entries")
class QueueEntryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = 0
    var userId: Long? = null
    var sequenceNumber: Int? = null
    @Enumerated(EnumType.STRING)
    var status: QueueEntryStatus = QueueEntryStatus.WAITING
    var joinedAt: Instant? = null
    var calledAt: Instant? = null
    var finishedAt: Instant? = null
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "queue_id")
    var queue: QueueEntity? = null
}