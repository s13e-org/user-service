package com.se.user_service.repository;

import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.se.user_service.helper.UUIDUtil;

@Repository
public class PermissionsRepository {
    private final JdbcTemplate jdbcTemplate;

    public PermissionsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public String findById(UUID roleId){
        Object[]  params = {
                UUIDUtil.uuidToBytes(roleId)
        };
        String list = jdbcTemplate.queryForObject("SELECT p.permission_name FROM permissions p inner join role_permissions rp on p.permission_id = rp.permission_id WHERE rp.role_id = ?",params,String.class);
		return list;
    }

    public UUID findByName (String name){
        String sql = """
                SELECT p.permission_id FROM permissions p p.permission_name = ?
                """;
        Object[] params = {
            name
        };
        UUID list = jdbcTemplate.queryForObject(sql,params, UUID.class);
        return list;
    }

    

}
