package com.example.queuebackend

import com.example.queuebackend.entity.UserEntity
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class JwtService(
    private val jwtEncoder: JwtEncoder
) {

    fun generateToken(user: UserEntity): String {
        val now = Instant.now()

        val claims = JwtClaimsSet.builder()
            .issuer("queue-backend")
            .issuedAt(now)
            .expiresAt(now.plus(24, ChronoUnit.HOURS))
            .subject(user.username)
            .claim("userId", user.id)
            .build()

        val header = JwsHeader.with(MacAlgorithm.HS256)
            .build()

        return jwtEncoder.encode(
            JwtEncoderParameters.from(header, claims)
        ).tokenValue
    }
}