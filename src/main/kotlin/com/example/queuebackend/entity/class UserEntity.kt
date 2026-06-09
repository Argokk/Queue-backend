package com.example.queuebackend.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
@Entity
@Table(name = "users")
class  UserEntity {
    @Id
    @GeneratedValue
    var id:Long = 0
    var username: String? = null
    var password: String? = null

}