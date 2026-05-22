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
        val entry = QueueEntryEntity().apply {
            this.queue = queue
            joinedAt = Instant.now()
            // безопасное присвоение sequenceNumber
            sequenceNumber = (queue.entries?.maxOfOrNull { it.sequenceNumber ?: 0 } ?: 0) + 1
            status = QueueEntryStatus.WAITING
        }
        queueEntryRepo.save(entry)

        // пересчитываем peopleCount
        queue.peopleCount = queue.entries?.count { it.status == QueueEntryStatus.WAITING } ?: 0
        queueRepo.save(queue)

        return ResponseEntity.ok(entry)
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable("id") id:Long){queueEntryRepo.deleteById(id)}

    @DeleteMapping
    fun deleteAll(){queueEntryRepo.deleteAll()}

    @PutMapping("/{id}")
    fun updateById(@PathVariable("id") id:Long, @RequestBody queueEntry: QueueEntryEntity):ResponseEntity<QueueEntryEntity> {
        if(queueEntryRepo.existsById(id)){
            queueEntry.id = id
            queueEntryRepo.save(queueEntry)
        }
        return ResponseEntity.ok(queueEntry)
    }
}