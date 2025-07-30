package com.se.user_service.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.se.user_service.dto.SignupRequestDTO;
import com.se.user_service.helper.UUIDUtil;
import com.se.user_service.mapper.GenericRowMapper;
import com.se.user_service.model.Users;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public Optional<Users> findByUsername (String username){
        List<Users> list = jdbcTemplate.query("SELECT * FROM users WHERE username = ?",
                new Object[]{username},
                new GenericRowMapper<>(Users.class));
        return list.stream().findFirst();
    }

    public Boolean existsByUsername (String username){
        List<Users> list = jdbcTemplate.query("SELECT * FROM users WHERE username = ?",
                new Object[] { username },
                new GenericRowMapper<>(Users.class));
        return list.size() > 0;
    }

    public Boolean existsByEmail (String email){
        List<Users> list = jdbcTemplate.query("SELECT * FROM users WHERE email = ?",
                new Object[] { email },
                new GenericRowMapper<>(Users.class));
        return list.size() > 0;
    }

    public Optional<Users> findById(UUID userId) {
        List<Users> list = jdbcTemplate.query("SELECT * FROM users WHERE user_id = ?",
				ps -> ps.setBytes(1, UUIDUtil.uuidToBytes(userId)),
				new GenericRowMapper<>(Users.class));
		return list.stream().findFirst();
    }

    public boolean insert (SignupRequestDTO dto, String passwordHash){
        UUID userId = UUIDUtil.generateUuidV7();
        Object[] params = {
            userId,
            dto.getUsername(),
            dto.getEmail(),
            passwordHash,
            LocalTime.now()
        };
        int row = jdbcTemplate.update("insert into users (user_id, username, email, password_hash,created_at)", params);
        return row > 0;
    }
}
