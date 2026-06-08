package com.example.queuebackend.repo

import com.example.queuebackend.QueueEntryStatus
import com.example.queuebackend.entity.QueueEntryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface QueueEntryRepo: JpaRepository<QueueEntryEntity,Long>{
fun countByQueue_IdAndStatus(
    queueId: Long,
    status: QueueEntryStatus
): Int

fun findFirstByQueue_IdAndStatusOrderBySequenceNumberAsc(
    queueId: Long,
    status: QueueEntryStatus
): QueueEntryEntity?

@Query(
    """
        select coalesce(max(e.sequenceNumber), -1)
        from QueueEntryEntity e
        where e.queue.id = :queueId
        """
)
fun findMaxSequenceNumberByQueueId(
    @Param("queueId") queueId: Long
): Int
}