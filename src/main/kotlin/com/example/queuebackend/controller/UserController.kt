package com.example.queuebackend.controller

import com.example.queuebackend.AuthDto
import com.example.queuebackend.JwtService
import com.example.queuebackend.Token
import com.example.queuebackend.entity.UserEntity
import com.example.queuebackend.repo.UserRepo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class UserController(
    private val repo: UserRepo,
    private val jwtService: JwtService,
    private val encoder: PasswordEncoder
) {
    @PostMapping("/register")
    fun registerUser(@RequestBody dto: AuthDto): ResponseEntity<Token> {
        if (repo.existsByUsername(dto.username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
        val newUser = UserEntity().apply {
            username = dto.username
            password = encoder.encode(dto.password)
        }
        val savedUser = repo.save(newUser)
        val token = jwtService.generateToken(savedUser)
        return ResponseEntity.ok(Token(token))
    }
    @PostMapping("/login")
    fun loginUser(@RequestBody dto: AuthDto): ResponseEntity<Token> {
        val user = repo.findByUsername(dto.username)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        val passwordCorrect = encoder.matches(
            dto.password,
            user.password
        )
        if (!passwordCorrect) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
        val token = jwtService.generateToken(user)
        return ResponseEntity.ok(Token(token))
    }
    @GetMapping("/users")
    fun getUsers(): ResponseEntity<List<UserEntity>> {
        return ResponseEntity.ok(repo.findAll())
    }
}