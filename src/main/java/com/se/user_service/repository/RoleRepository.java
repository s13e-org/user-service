package com.se.user_service.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.se.user_service.dto.RolesRequestDTO;
import com.se.user_service.helper.UUIDUtil;
import com.se.user_service.mapper.GenericRowMapper;
import com.se.user_service.model.Roles;

@Repository
public class RoleRepository {
    private final JdbcTemplate jdbcTemplate;

    public RoleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public Optional<Roles> findByName (String name){
        List<Roles> list = jdbcTemplate.query("SELECT * FROM roles WHERE role_name = ?",
				new Object[]{
                        name},
				new GenericRowMapper<>(Roles.class));
		return list.stream().findFirst();
    }

    public boolean insert(RolesRequestDTO roles){
        Object[] params = {
            UUIDUtil.generateUuidV7(),
            roles.getRoleName(),
            roles.getDescription()
        };
        int row = jdbcTemplate.update("insert into roles (role_id, role_name, description) values (?, ?, ?)", params);
        return row > 0;
    }

    public boolean insertRoleToUser(UUID userId, UUID roleId){
        Object[] params = {
            UUIDUtil.uuidToBytes(userId),
            UUIDUtil.uuidToBytes(roleId)
        };
        int row = jdbcTemplate.update("insert into user_roles (user_id, role_id) values (?, ?)", params);
        return row > 0;
    }

    
}
