package com.example.queuebackend.controller

import com.example.queuebackend.CreateNewEntryDto
import com.example.queuebackend.QueueEntryStatus
import com.example.queuebackend.entity.QueueEntity
import com.example.queuebackend.entity.QueueEntryEntity
import com.example.queuebackend.repo.QueueEntryRepo
import com.example.queuebackend.repo.QueueRepo
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.Date
import java.util.Optional
import java.time.Instant

@RestController
@RequestMapping("/api/queueEntry")
class QueueEntryController(private val queueEntryRepo: QueueEntryRepo, private val queueRepo: QueueRepo) {
    @GetMapping
    fun findAllEnt(): List<QueueEntryEntity>{return queueEntryRepo.findAll()}

    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id:Long): Optional<QueueEntryEntity>{ return queueEntryRepo.findById(id)}

    @PostMapping
    @Transactional
    fun create(@RequestBody newEntry: CreateNewEntryDto): ResponseEntity<QueueEntryEntity> {
        val queue = queueRepo.findById(newEntry.queueId).orElseThrow()
        val queueId = queue.id ?: throw RuntimeException("Queue id is null")
        val shouldCallImmediately =
            queueEntryRepo.countByQueue_IdAndStatus(queueId, QueueEntryStatus.WAITING) == 0 &&
                queueEntryRepo.countByQueue_IdAndStatus(queueId, QueueEntryStatus.CALLED) == 0

        val entry = QueueEntryEntity().apply {
            this.queue = queue
            joinedAt = Instant.now()
            sequenceNumber = queueEntryRepo.findMaxSequenceNumberByQueueId(queueId) + 1
            status = if (shouldCallImmediately) QueueEntryStatus.CALLED else QueueEntryStatus.WAITING
            calledAt = if (shouldCallImmediately) Instant.now() else null
        }

        val savedEntry = queueEntryRepo.save(entry)

        queue.peopleCount = queueEntryRepo.countByQueue_IdAndStatus(
            queueId,
            QueueEntryStatus.WAITING
        )
        queueRepo.save(queue)

        return ResponseEntity.ok(savedEntry)
    }
    @PutMapping("/leave/{id}")
    @Transactional
    fun leaveQueue(@PathVariable id: Long): ResponseEntity<QueueEntryEntity> {
        val entry = queueEntryRepo.findById(id).orElseThrow()
        val queue = entry.queue ?: throw RuntimeException("Queue not found")
        val queueId = queue.id ?: throw RuntimeException("Queue id is null")

        entry.status = QueueEntryStatus.LEFT
        entry.finishedAt = Instant.now()

        val savedEntry = queueEntryRepo.save(entry)

        queue.peopleCount = queueEntryRepo.countByQueue_IdAndStatus(
            queueId,
            QueueEntryStatus.WAITING
        )
        queueRepo.save(queue)

        return ResponseEntity.ok(savedEntry)
    }
    @PutMapping("/call/{queueId}")
    @Transactional
    fun callNext(@PathVariable queueId: Long): ResponseEntity<QueueEntryEntity> {
        val queue = queueRepo.findById(queueId).orElseThrow()

        val nextEntry = queueEntryRepo.findFirstByQueue_IdAndStatusOrderBySequenceNumberAsc(
            queueId,
            QueueEntryStatus.WAITING
        ) ?: return ResponseEntity.notFound().build()

        nextEntry.status = QueueEntryStatus.CALLED
        nextEntry.calledAt = Instant.now()

        val savedEntry = queueEntryRepo.save(nextEntry)

        queue.peopleCount = queueEntryRepo.countByQueue_IdAndStatus(
            queueId,
            QueueEntryStatus.WAITING
        )
        queueRepo.save(queue)

        return ResponseEntity.ok(savedEntry)
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable("id") id:Long){queueEntryRepo.deleteById(id)}

    @DeleteMapping
    fun deleteAll(){
        queueEntryRepo.deleteAll()}

    @PutMapping("/{id}")
    fun updateById(@PathVariable("id") id:Long, @RequestBody queueEntry: QueueEntryEntity):ResponseEntity<QueueEntryEntity> {
        if(queueEntryRepo.existsById(id)){
            queueEntry.id = id
            queueEntryRepo.save(queueEntry)
        }
        return ResponseEntity.ok(queueEntry)
    }
}
