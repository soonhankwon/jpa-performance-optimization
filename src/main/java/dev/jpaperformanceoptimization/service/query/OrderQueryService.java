package dev.jpaperformanceoptimization.service.query;

import static java.util.stream.Collectors.toList;

import dev.jpaperformanceoptimization.api.OrderApiController.OrderDto;
import dev.jpaperformanceoptimization.domain.Order;
import dev.jpaperformanceoptimization.domain.OrderItem;
import dev.jpaperformanceoptimization.repository.OrderRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public List<Order> getOrderV1() {
        List<Order> all = orderRepository.findAll();
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();

            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }

    @Transactional(readOnly = true)
    public List<OrderDto> getOrderV2() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderDto::new)
                .collect(toList());
    }
}
