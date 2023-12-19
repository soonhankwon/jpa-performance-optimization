package dev.jpaperformanceoptimization.api;

import static java.util.stream.Collectors.toList;

import dev.jpaperformanceoptimization.domain.Address;
import dev.jpaperformanceoptimization.domain.Order;
import dev.jpaperformanceoptimization.domain.OrderStatus;
import dev.jpaperformanceoptimization.repository.OrderRepository;
import dev.jpaperformanceoptimization.repository.order.simplequery.OrderSimpleQueryDto;
import dev.jpaperformanceoptimization.repository.order.simplequery.OrderSimpleQueryRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;
    private final EntityManager em;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> orderV1() {
        List<Order> all = orderRepository.findAll();
        for (Order order : all) {
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress();
        }
        return all;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> orderV2() {
        //ORDER 2개
        //N + 1 -> 1 + 회원N + 배송N
        List<Order> orders = orderRepository.findAll();

        //루프
        return orders.stream()
                .map(SimpleOrderDto::new)
                .collect(toList());
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> orderV3() {
        List<Order> orders = em.createQuery(
                "select o from Order o "
                        + "join fetch o.member m "
                        + "join fetch o.delivery d",
                Order.class
        ).getResultList();

        return orders.stream()
                .map(SimpleOrderDto::new)
                .collect(toList());
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> orderV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); //LAZY 초기화
        }
    }
}
