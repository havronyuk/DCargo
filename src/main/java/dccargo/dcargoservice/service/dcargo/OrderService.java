package dccargo.dcargoservice.service.dcargo;


import dccargo.dcargoservice.model.dcargo.Order;
import dccargo.dcargoservice.model.dcargo.OrderPoint;
import dccargo.dcargoservice.repository.dcargo.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;


    public Order getOrderById(Long idOrder) {
        return orderRepository.getByIdOrder(idOrder);
    }


    @Transactional
    public Order createOrderFromShipment(Order order) {
        log.info("Received order: {}", order.toString());

        boolean isExist = orderRepository.existsByIdVehicleSession(order.getIdVehicleSession());

        if (!isExist) {
            orderRepository.save(order);
            return order;
        }

        Order orderDB = orderRepository.getByIdVehicleSession(order.getIdVehicleSession());

        // Обновляем основные поля
        orderDB.setDeliveryDate(order.getDeliveryDate());
        orderDB.setFirmName(order.getFirmName());
        orderDB.setFirmPhoneNumber(order.getFirmPhoneNumber());
        orderDB.setIsCrossDock(order.getIsCrossDock());
        orderDB.setTypeSklad(order.getTypeSklad());
        orderDB.setTargetWeigth(order.getTargetWeigth());
        orderDB.setTargetPall(order.getTargetPall());
        orderDB.setShipmentPlanDate(order.getShipmentPlanDate());
        orderDB.setMaxWeigth(order.getMaxWeigth());
        orderDB.setMaxPall(order.getMaxPall());
        orderDB.setIsKep(order.getIsKep());

        // Обновляем OrderPoints
        updateOrderPoints(orderDB, order.getOrderPoints());

        Order savedOrder = orderRepository.save(orderDB);
        return savedOrder;
    }

    private void updateOrderPoints(Order existingOrder, List<OrderPoint> newPoints) {
        if (newPoints == null) {
            // Если новых точек нет - удаляем все существующие
            if (existingOrder.getOrderPoints() != null) {
                existingOrder.getOrderPoints().clear();
            }
            return;
        }

        // Создаем Map для быстрого поиска новых точек по idVehicleSessionPoint
        Map<Integer, OrderPoint> newPointsMap = newPoints.stream()
                .filter(p -> p.getIdVehicleSessionPoint() != null)
                .collect(Collectors.toMap(
                        OrderPoint::getIdVehicleSessionPoint,
                        Function.identity(),
                        (existing, replacement) -> replacement
                ));

        // Получаем существующие точки
        List<OrderPoint> existingPoints = existingOrder.getOrderPoints();
        if (existingPoints == null) {
            existingPoints = new ArrayList<>();
            existingOrder.setOrderPoints(existingPoints);
        }

        // Удаляем точки, которых нет в новом списке
        existingPoints.removeIf(existingPoint ->
                existingPoint.getIdVehicleSessionPoint() != null &&
                        !newPointsMap.containsKey(existingPoint.getIdVehicleSessionPoint())
        );

        // Обновляем существующие и добавляем новые
        for (OrderPoint newPoint : newPoints) {
            if (newPoint.getIdVehicleSessionPoint() == null) {
                // Если нет idVehicleSessionPoint - пропускаем или можно добавить как новый
                continue;
            }

            OrderPoint existingPoint = existingPoints.stream()
                    .filter(p -> newPoint.getIdVehicleSessionPoint().equals(p.getIdVehicleSessionPoint()))
                    .findFirst()
                    .orElse(null);

            if (existingPoint != null) {
                // Обновляем существующую точку (сохраняем idOrderPoint)
                existingPoint.setWarehouseId(newPoint.getWarehouseId());
                existingPoint.setTonnage(newPoint.getTonnage());
                existingPoint.setPalls(newPoint.getPalls());
                existingPoint.setCleanings(newPoint.getCleanings());
                existingPoint.setRouteOrder(newPoint.getRouteOrder());
                existingPoint.setIsCleaned(newPoint.getIsCleaned());
                existingPoint.setCleaningInterval(newPoint.getCleaningInterval());
                existingPoint.setLat(newPoint.getLat());
                existingPoint.setLng(newPoint.getLng());
                existingPoint.setAddress(newPoint.getAddress());
                // idOrder не обновляем, так как это связь с заказом
                // idOrderPoint не обновляем
            } else {
                // Добавляем новую точку
                OrderPoint pointToAdd = new OrderPoint();
                pointToAdd.setIdVehicleSessionPoint(newPoint.getIdVehicleSessionPoint());
                pointToAdd.setWarehouseId(newPoint.getWarehouseId());
                pointToAdd.setTonnage(newPoint.getTonnage());
                pointToAdd.setPalls(newPoint.getPalls());
                pointToAdd.setCleanings(newPoint.getCleanings());
                pointToAdd.setRouteOrder(newPoint.getRouteOrder());
                pointToAdd.setIsCleaned(newPoint.getIsCleaned());
                pointToAdd.setCleaningInterval(newPoint.getCleaningInterval());
                pointToAdd.setLat(newPoint.getLat());
                pointToAdd.setLng(newPoint.getLng());
                pointToAdd.setAddress(newPoint.getAddress());

                existingPoints.add(pointToAdd);
            }
        }
    }


    public List<Order> getOrdersByDeliveryDateAndTypeSklad(LocalDate deliveryDate, String typeSklad) {
        return orderRepository.findAllByDeliveryDateAndTypeSklad(deliveryDate,typeSklad);

    }
}
