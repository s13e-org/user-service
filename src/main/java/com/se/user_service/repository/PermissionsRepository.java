package com.se.user_service.repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.se.user_service.helper.AuthTokenFilter;
import com.se.user_service.helper.JwtUtils;
import com.se.user_service.helper.UUIDUtil;
import com.se.user_service.mapper.GenericRowMapper;
import com.se.user_service.model.PermissionEndpoints;

import jakarta.servlet.http.HttpServletRequest;

@Repository
public class PermissionsRepository {
    private final JdbcTemplate jdbcTemplate;
    private JwtUtils jwtUtils;
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    public PermissionsRepository(JdbcTemplate jdbcTemplate, JwtUtils jwtUtils) {
        this.jdbcTemplate = jdbcTemplate;
        this.jwtUtils = jwtUtils;
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

    public Set<String> getPermissionsByRole(String roleName) {
        String sql = """
            SELECT p.name FROM permissions p
            JOIN role_permissions rp ON rp.permission_id = p.id
            JOIN roles r ON rp.role_id = r.id
            WHERE r.name = ?
        """;

        return new HashSet<>(jdbcTemplate.queryForList(sql, String.class, roleName));
    }

    public boolean hasPermission(String[] roles, String permission) {
        for (String role : roles) {
            Set<String> perms = getPermissionsByRole(role);
            if (perms.contains(permission)) return true;
        }
        return false;
    }

    public Set<PermissionEndpoints> getEndpoint (HttpServletRequest request){
        try {
            
            String header = request.getHeader("Authorization");
            logger.error("HEADER: " + header);
            if (header == null || !header.startsWith("Bearer ")) {
                return Collections.emptySet();
            }
    
            String token = header.substring(7);
            Map<String, Object> claims = jwtUtils.extractClaims(token);
    
            String username = (String) claims.get("sub");
            
            List<String> permissionName = (List<String>) claims.get("permissions");
    
            String inSql = String.join(",", Collections.nCopies(permissionName.size(), "?"));
            logger.error("CONDITION: " + inSql);
            logger.error("permissionName: " + permissionName);
    
            Set<PermissionEndpoints> endpoints = new HashSet<>();
            Object[] params = permissionName.toArray();
            logger.error("params: " + Arrays.toString(params));
    
            String sql = """
                    select pe.* from permissions p
                    inner join permission_endpoints pe on p.permission_id = pe.permission_id
                    where p.permission_name in (%s)
                    """.formatted(inSql);
            
            List<PermissionEndpoints> endpoint = jdbcTemplate.query(sql, params, new GenericRowMapper<>(PermissionEndpoints.class));
           
            if (endpoint == null) {
                logger.error("endpoints is null");
            } else if (endpoint.isEmpty()) {
                logger.error("endpoints is empty");
            } else {
                endpoint.forEach(e -> {
                    logger.error("PermissionEndpoints:");
                    logger.error("  id: " + e.getId());
                    logger.error("  permissionId: " + e.getPermissionId());
                    logger.error("  httpMethod: " + e.getHttpMethod());
                    logger.error("  endpointPath: " + e.getEndpointPath());
                });
            }

            // logger.error("sql: " + sql);
            // logger.error("endpoint: " + endpoint.toString());
            endpoints.addAll(endpoint);
    
            return endpoints;
        } catch (Exception e) {
            logger.error("Error querying permissions", e);
            throw e;
        }
    }

}
