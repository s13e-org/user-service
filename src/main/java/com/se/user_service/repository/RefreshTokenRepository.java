package com.se.user_service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.se.user_service.helper.UUIDUtil;
import com.se.user_service.model.RefreshToken;
import com.se.user_service.model.Users;

@Repository
public class RefreshTokenRepository {
    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;
    
    public RefreshTokenRepository(JdbcTemplate jdbcTemplate, UserRepository userRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
    }

    public Optional<RefreshToken> findByToken(String token){
        return Optional.empty();
    }

    public int deleteByUser(Users user){
        return 0;
    }

    public void delete(RefreshToken token) {
        jdbcTemplate.update("delete from refresh_token where token = ?", token);
    }

    public RefreshToken save(RefreshToken refreshToken) {
        UUID id = UUIDUtil.generateUuidV7();
        Object[] params = {
                UUIDUtil.uuidToBytes(id),
                refreshToken.getToken(), 
                refreshToken.getExpiryDate(), 
                UUIDUtil.uuidToBytes(refreshToken.getUserId())
        };
        int row = jdbcTemplate.update("insert into refresh_token (id, token, expiry_date, user_id) values(?, ?, ?, ?)", params);

        return row > 0 ? refreshToken : null;
    }
}
