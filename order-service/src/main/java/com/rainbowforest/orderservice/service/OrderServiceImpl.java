package com.rainbowforest.orderservice.service;

import com.rainbowforest.orderservice.domain.Order;
import com.rainbowforest.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order saveOrder(Order order) {
        Order saved = orderRepository.save(order);
        // Force-initialize lazy associations trước khi rời transaction
        // để tránh LazyInitializationException khi Jackson serialize response
        initOrder(saved);
        return saved;
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        orders.forEach(this::initOrder);
        return orders;
    }

    @Override
    public Order getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null)
            initOrder(order);
        return order;
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findAll().stream()
                .filter(o -> userId.equals(o.getUserId()))
                .collect(Collectors.toList());
    }

    @Override
    public Order updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng id: " + id));
        order.setStatus(status);
        Order saved = orderRepository.save(order);
        initOrder(saved);
        return saved;
    }

    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id))
            throw new RuntimeException("Không tìm thấy đơn hàng id: " + id);
        orderRepository.deleteById(id);
    }

    /** Force-initialize tất cả lazy associations trong transaction */
    private void initOrder(Order o) {
        if (o.getItems() != null) {
            o.getItems().forEach(item -> {
                if (item.getProduct() != null)
                    item.getProduct().getProductName();
            });
        }
        if (o.getDiningTable() != null)
            o.getDiningTable().getNumber();
        // User is now stored as userId/userName columns - no lazy init needed
    }
}
