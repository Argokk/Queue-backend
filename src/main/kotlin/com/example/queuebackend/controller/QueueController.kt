package com.example.queuebackend.controller

import com.example.queuebackend.CreateNewQueue
import com.example.queuebackend.entity.EnterpriseEntity
import com.example.queuebackend.entity.QueueEntity
import com.example.queuebackend.repo.EnterpriseRepo
import com.example.queuebackend.repo.QueueEntryRepo
import com.example.queuebackend.repo.QueueRepo
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.Optional

@RestController
@RequestMapping("/api/queue")
class QueueController(private val queueRepo: QueueRepo,private val enterpriseRepo: EnterpriseRepo) {
    @GetMapping
    fun findAllEnt(): List<QueueEntity>{
        return queueRepo.findAll()}

    @GetMapping("/{enterpriseId}")
    fun findById(@PathVariable("enterpriseId") id:Long): Optional<QueueEntity>{ return queueRepo.findByEnterpriseId(id)}

    @PostMapping
    fun create(@RequestBody newQueue : CreateNewQueue): ResponseEntity<QueueEntity> {
        if(queueRepo.existsByEnterpriseId(newQueue.enterpriseId?:-1)){throw RuntimeException()
        }
        else{
            val newQueueEntity = QueueEntity().apply {
                isEnabled = newQueue.isEnabled
                averageServiceSeconds = newQueue.averageServiceSeconds
                enterprise = enterpriseRepo.findById(newQueue.enterpriseId?:-1).orElse(null)
            }
            val obj = queueRepo.save(newQueueEntity)
            return ResponseEntity.ok(obj)
        }
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable("id") id:Long){queueRepo.deleteById(id)}

    @DeleteMapping
    fun deleteAll(){queueRepo.deleteAll()}

    @PutMapping("/{id}")
    fun updateById(@PathVariable("id") id:Long, @RequestBody queue: QueueEntity):ResponseEntity<QueueEntity> {
        if(queueRepo.existsById(id)){
            queue.id = id
            queueRepo.save(queue)
        }
        return ResponseEntity.ok(queue)
    }
}