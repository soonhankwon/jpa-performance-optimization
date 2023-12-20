package dev.jpaperformanceoptimization.repository.order.query;

import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> result = findOrders(); //query 1번 -> 2개(N)
        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId()); //query 2번(N)
            o.setOrderItems(orderItems);
        });

        return result;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                        "select new dev.jpaperformanceoptimization.repository.order.query.OrderItemQueryDto(oi.order.id, oi.item.name, oi.orderPrice, oi.count)"
                                + "from OrderItem oi "
                                + "join oi.item i "
                                + "where oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    public List<OrderQueryDto> findOrders() {
        return em.createQuery(
                        "select new dev.jpaperformanceoptimization.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, m.address) "
                                + "from Order o "
                                + "join o.member m "
                                + "join o.delivery d ", OrderQueryDto.class)
                .getResultList();
    }
}
