package com.se.user_service.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

    public Optional<Users> findByUsername(String username) {
        List<Users> list = jdbcTemplate.query("SELECT * FROM users WHERE username = ?",
                new Object[] { username },
                new GenericRowMapper<>(Users.class));
        return list.stream().findFirst();
    }

    public Boolean existsByUsername(String username) {
        List<Users> list = jdbcTemplate.query("SELECT * FROM users WHERE username = ?",
                new Object[] { username },
                new GenericRowMapper<>(Users.class));
        return list.size() > 0;
    }

    public Boolean existsByEmail(String email) {
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

    public boolean insert(SignupRequestDTO dto, String passwordHash) {
        UUID userId = UUIDUtil.generateUuidV7();
        Object[] params = {
                UUIDUtil.uuidToBytes(userId),
                dto.getUsername(),
                dto.getEmail(),
                passwordHash,
                LocalDateTime.now()
        };
        int row = jdbcTemplate.update(
                "insert into users (user_id, username, email, password_hash,created_at) values (?, ?, ?, ?, ?)",
                params);
        return row > 0;
    }

    public List<GrantedAuthority> findAuthoritiesByUserId(UUID userId) {
        String sql = """
                    SELECT r.role_name
                    FROM user_roles ur
                    JOIN roles r ON ur.role_id = r.role_id
                    WHERE ur.user_id = ?
                """;
        return jdbcTemplate.query(sql, new Object[] { UUIDUtil.uuidToBytes(userId) },
                (rs, rowNum) -> new SimpleGrantedAuthority(rs.getString("role_name")));
    }

    public List<GrantedAuthority> findPermissionNamesByUserId(UUID id){
        String sql = """
                    SELECT p.permission_name
                    FROM user_roles ur
                    JOIN roles r ON ur.role_id = r.role_id
                    JOIN role_permissions rp on rp.role_id = r.role_id
                    JOIN permissions p on p.permission_id = rp.permission_id
                    WHERE ur.user_id = ?
                """;
        return jdbcTemplate.query(sql, new Object[] { UUIDUtil.uuidToBytes(id) },
                (rs, rowNum) -> new SimpleGrantedAuthority(rs.getString("permission_name")));
    }

    public List<String> getRolesByUserId(UUID userId) {
        String sql = """
                    SELECT r.role_name
                    FROM user_roles ur
                    JOIN roles r ON ur.role_id = r.role_id
                    WHERE ur.user_id = ?
                """;
        return jdbcTemplate.queryForList(sql, String.class, new Object[] { UUIDUtil.uuidToBytes(userId) });
    }

}
