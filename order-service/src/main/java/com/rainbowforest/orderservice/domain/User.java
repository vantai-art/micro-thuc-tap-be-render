package com.rainbowforest.orderservice.domain;

/**
 * FIX: Đã bỏ @Entity — User KHÔNG còn là bảng trong orders_db
 *
 * Lý do: User được quản lý bởi user-service (DB riêng).
 * Nếu giữ @Entity, Hibernate sẽ cố JOIN vào bảng "users" trong orders_db
 * → schema mismatch / bảng không tồn tại → 500 khi gọi GET /order.
 *
 * Thay vào đó, Order lưu userId và userName trực tiếp dưới dạng cột riêng.
 * User object này chỉ dùng để nhận dữ liệu từ user-service qua Feign Client.
 */
public class User {

    private Long id;
    private String userName;

    public User() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}
