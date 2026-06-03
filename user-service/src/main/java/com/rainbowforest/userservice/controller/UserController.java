package com.rainbowforest.userservice.controller;

import com.rainbowforest.userservice.entity.User;
import com.rainbowforest.userservice.entity.UserRole;
import com.rainbowforest.userservice.http.header.HeaderGenerator;
import com.rainbowforest.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private HeaderGenerator headerGenerator;

    // ==========================================
    // ROLE ENDPOINTS
    // POST /roles → tạo role mới
    // GET /roles → lấy tất cả role
    // ==========================================

    @PostMapping(value = "/roles")
    public ResponseEntity<UserRole> addRole(@RequestBody UserRole role, HttpServletRequest request) {
        if (role != null && role.getRoleName() != null) {
            try {
                UserRole saved = userService.saveRole(role);
                return new ResponseEntity<>(
                        saved,
                        headerGenerator.getHeadersForSuccessPostMethod(request, saved.getId()),
                        HttpStatus.CREATED);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/roles")
    public ResponseEntity<List<UserRole>> getAllRoles() {
        List<UserRole> roles = userService.getAllRoles();
        if (!roles.isEmpty()) {
            return new ResponseEntity<>(roles, headerGenerator.getHeadersForSuccessGetMethod(), HttpStatus.OK);
        }
        return new ResponseEntity<>(headerGenerator.getHeadersForError(), HttpStatus.NOT_FOUND);
    }

    // ==========================================
    // USER ENDPOINTS (ROLE_USER)
    // GET /users → lấy tất cả user
    // GET /users?name=xxx → tìm theo tên
    // GET /users/{id} → lấy theo id
    // POST /users → tạo user thường
    // PUT /users/{id} → cập nhật user
    // DELETE /users/{id} → xóa user
    // ==========================================

    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (!users.isEmpty()) {
            return new ResponseEntity<>(users, headerGenerator.getHeadersForSuccessGetMethod(), HttpStatus.OK);
        }
        return new ResponseEntity<>(headerGenerator.getHeadersForError(), HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/users", params = "name")
    public ResponseEntity<User> getUserByName(@RequestParam("name") String userName) {
        User user = userService.getUserByName(userName);
        if (user != null) {
            return new ResponseEntity<>(user, headerGenerator.getHeadersForSuccessGetMethod(), HttpStatus.OK);
        }
        return new ResponseEntity<>(headerGenerator.getHeadersForError(), HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, headerGenerator.getHeadersForSuccessGetMethod(), HttpStatus.OK);
        }
        return new ResponseEntity<>(headerGenerator.getHeadersForError(), HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/users")
    public ResponseEntity<User> addUser(@RequestBody User user, HttpServletRequest request) {
        if (user != null) {
            try {
                userService.saveUser(user);
                return new ResponseEntity<>(
                        user,
                        headerGenerator.getHeadersForSuccessPostMethod(request, user.getId()),
                        HttpStatus.CREATED);
            } catch (RuntimeException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        try {
            User updated = userService.updateUser(id, user);
            return new ResponseEntity<>(updated, headerGenerator.getHeadersForSuccessGetMethod(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(headerGenerator.getHeadersForError(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(headerGenerator.getHeadersForSuccessGetMethod(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(headerGenerator.getHeadersForError(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ==========================================
    // ADMIN ENDPOINTS (ROLE_ADMIN)
    // POST /users/admin → tạo tài khoản admin
    // GET /users/admins → lấy danh sách admin
    // ==========================================

    @PostMapping(value = "/users/admin")
    public ResponseEntity<User> addAdmin(@RequestBody User user, HttpServletRequest request) {
        if (user != null) {
            try {
                userService.saveUserWithRole(user, "ROLE_ADMIN");
                return new ResponseEntity<>(
                        user,
                        headerGenerator.getHeadersForSuccessPostMethod(request, user.getId()),
                        HttpStatus.CREATED);
            } catch (RuntimeException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/users/admins")
    public ResponseEntity<List<User>> getAllAdmins() {
        List<User> admins = userService.getUsersByRole("ROLE_ADMIN");
        if (!admins.isEmpty()) {
            return new ResponseEntity<>(admins, headerGenerator.getHeadersForSuccessGetMethod(), HttpStatus.OK);
        }
        return new ResponseEntity<>(headerGenerator.getHeadersForError(), HttpStatus.NOT_FOUND);
    }

    // ==========================================
    // STAFF ENDPOINTS (ROLE_STAFF)
    // POST /users/staff → tạo tài khoản staff
    // GET /users/staffs → lấy danh sách staff
    // ==========================================

    @PostMapping(value = "/users/staff")
    public ResponseEntity<User> addStaff(@RequestBody User user, HttpServletRequest request) {
        if (user != null) {
            try {
                userService.saveUserWithRole(user, "ROLE_STAFF");
                return new ResponseEntity<>(
                        user,
                        headerGenerator.getHeadersForSuccessPostMethod(request, user.getId()),
                        HttpStatus.CREATED);
            } catch (RuntimeException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/users/staffs")
    public ResponseEntity<List<User>> getAllStaffs() {
        List<User> staffs = userService.getUsersByRole("ROLE_STAFF");
        if (!staffs.isEmpty()) {
            return new ResponseEntity<>(staffs, headerGenerator.getHeadersForSuccessGetMethod(), HttpStatus.OK);
        }
        return new ResponseEntity<>(headerGenerator.getHeadersForError(), HttpStatus.NOT_FOUND);
    }

    // ==========================================
    // CHANGE PASSWORD
    // POST /users/{id}/change-password
    // Body: { "currentPassword": "...", "newPassword": "..." }
    // ==========================================

    @PostMapping(value = "/users/{id}/change-password")
    public ResponseEntity<?> changePassword(
            @PathVariable("id") Long id,
            @RequestBody java.util.Map<String, String> body) {
        String currentPassword = body.get("currentPassword");
        String newPassword = body.get("newPassword");

        if (currentPassword == null || newPassword == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(java.util.Map.of("message", "Vui lòng nhập đủ mật khẩu cũ và mật khẩu mới"));
        }
        if (newPassword.length() < 4) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(java.util.Map.of("message", "Mật khẩu mới phải có ít nhất 4 ký tự"));
        }

        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(java.util.Map.of("message", "Không tìm thấy tài khoản"));
        }
        if (!user.getUserPassword().equals(currentPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(java.util.Map.of("message", "Mật khẩu hiện tại không đúng"));
        }

        user.setUserPassword(newPassword);
        userService.updateUser(id, user);
        return ResponseEntity.ok(java.util.Map.of("message", "Đổi mật khẩu thành công"));
    }
}