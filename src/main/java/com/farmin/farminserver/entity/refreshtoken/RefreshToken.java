package com.farmin.farminserver.entity.refreshtoken;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
@Getter
@AllArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 2592000)
public class RefreshToken {
    @Id
    private String username;

    @Indexed
    private String refreshToken;
}
