## Các layer

| Layer               | Class                     | Vai trò                                    |
| ------------------- | ------------------------- | ------------------------------------------ |
| 🛡 Filter           | `JwtAuthenticationFilter` | Lấy JWT, xác thực và tạo `Authentication`  |
| 🧠 Auth Service     | `UserDetailsServiceImpl`  | Load user & permission từ DB               |
| 🔐 Auth Model       | `UserDetailsImpl`         | Gói user và permission trong `UserDetails` |
| 🧾 Token            | `JwtUtils`                | Sinh và xác thực JWT                       |
| 🎯 Controller       | `@RestController`         | Sử dụng @PreAuthorize, @Secured...         |
| 🧠 Security Context | `SecurityContextHolder`   | Giữ thông tin authenticated user           |

> - Request gửi kèm JWT ở header → Authorization: Bearer <token>

> - JwtAuthenticationFilter kiểm tra, giải mã token → lấy username và permission

> - Gọi UserDetailsServiceImpl.loadUserByUsername(username)

> - Dữ liệu trả về là UserDetailsImpl, chứa: 
>> username
>> password (nếu cần)
>> List<String> permission → chuyển thành GrantedAuthority

> -  SecurityContextHolder.getContext().setAuthentication(...)

> - Spring Security tự kiểm tra quyền khi controller gọi:
>>  @PreAuthorize("hasAuthority('READ_USER')") 
>> hoặc dùng SecurityContextHolder để lấy Authentication

## Authentication & Authorization Flow 

```shell
Spring Security Filter Chain
│
├──> JwtAuthenticationFilter (OncePerRequestFilter)
│    ├── Extract JWT from Authorization header
│    ├── Validate JWT (parse, check expiration, signature)
│    ├── Extract username from token
│    ├── Call UserDetailsService.loadUserByUsername(username)
│    │    └──> Load user info from DB (Users, Permissions)
│    │         ├── Get List<Permission> from DB
│    │         ├── Convert to List<GrantedAuthority>
│    │         └── Return new UserDetailsImpl(...)
│    ├── Set Authentication object to SecurityContext
│    └── Continue filter chain
│
├──> Controller or Resource Handler
│    └──> Uses SecurityContextHolder.getContext().getAuthentication()
│         └──> Authorization decision via:
│              ├── @PreAuthorize("hasAuthority(...)")
│              ├── @Secured("ROLE_ADMIN")
│              └── AccessDecisionManager
│
└──> ExceptionTranslationFilter
     └── Catch access denied or unauthenticated
         └── Return 401 or 403 error

```

## 

```
Client
  |
  |-- (1) Authorization: Bearer <token>
  ↓
Gateway (Spring Boot)
  |
  |-- (2) Gửi token sang AuthService để introspect
  ↓
AuthService (Spring Boot + JDBC)
  |
  |-- (3) Trả về: user info + roles + scopes
  ↓
Gateway kiểm tra quyền → nếu OK thì route đến Service con

```