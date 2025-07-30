package com.se.user_service.repository;

import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.se.user_service.model.RefreshToken;
import com.se.user_service.model.Users;

@Repository
public class RefreshTokenRepository {
    private final JdbcTemplate jdbcTemplate;
    
    public RefreshTokenRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
        int row = jdbcTemplate.update("insert into refresh_token (id, token, expiry_date, user_id) values(?, ?, ?, ?)", refreshToken.getId(), refreshToken.getToken(), refreshToken.getExpiryDate(), refreshToken.getUserId());

        return row > 0 ? refreshToken : null;
    }
}
