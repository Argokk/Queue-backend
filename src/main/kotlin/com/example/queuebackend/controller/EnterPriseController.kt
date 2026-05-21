package com.example.queuebackend.controller

import com.example.queuebackend.entity.EnterpriseEntity
import com.example.queuebackend.repo.EnterpriseRepo
import org.springframework.http.ResponseEntity
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
@RequestMapping("/api/enterprise")
class EnterPriseController(private val EnterpriseRepo: EnterpriseRepo){
    @GetMapping
    fun findAllEnt(): List<EnterpriseEntity>{return EnterpriseRepo.findAll()}

    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id:Long): Optional<EnterpriseEntity>{ return EnterpriseRepo.findById(id)}

    @PostMapping
    fun create(@RequestBody enterprise : EnterpriseEntity): ResponseEntity<EnterpriseEntity> {
        val obj = EnterpriseRepo.save(enterprise)
        return ResponseEntity.ok(obj)
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable("id") id:Long){EnterpriseRepo.deleteById(id)}

    @DeleteMapping
    fun deleteAll(){EnterpriseRepo.deleteAll()}

    @PutMapping("/{id}")
    fun updateById(@PathVariable("id") id:Long, @RequestBody enterprise: EnterpriseEntity):ResponseEntity<EnterpriseEntity> {
        if(EnterpriseRepo.existsById(id)){
            enterprise.id = id
            EnterpriseRepo.save(enterprise)
        }
        return ResponseEntity.ok(enterprise)
    }
}