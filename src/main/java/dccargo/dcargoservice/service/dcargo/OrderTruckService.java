package dccargo.dcargoservice.service.dcargo;


import dccargo.dcargoservice.enums.OrderTruckAssigmentStatus;
import dccargo.dcargoservice.enums.RouteSheetStatus;
import dccargo.dcargoservice.model.dcargo.OrderTruck;
import dccargo.dcargoservice.model.dcargo.RouteSheet;
import dccargo.dcargoservice.repository.dcargo.OrderTruckRepository;
import dccargo.dcargoservice.repository.dcargo.RouteSheetRepository;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderTruckService {

    private final OrderTruckRepository orderTruckRepository;
    private final RouteSheetRepository routeSheetRepository;


    public OrderTruck create(OrderTruck orderTruck){

        if(orderTruck.getIdTruck() == null){
            throw new MainServiceException("ID авто не может быть null");
        }

        orderTruck.setStatus(OrderTruckAssigmentStatus.ACTIVE);

        orderTruckRepository.save(orderTruck);

        return orderTruck;

    }

    public OrderTruck assignTruckUserToOrder(Long idOrder, Long idTruck, Long idUser, String userAdd, Long idTruckUserAssigment) {

        // 1. Находим активные назначения
        List<OrderTruck> activeAssignments = orderTruckRepository.findByIdOrderAndStatus(idOrder, OrderTruckAssigmentStatus.ACTIVE);

        // 2. Закрываем старые активные назначения
        if (!activeAssignments.isEmpty()) {
            activeAssignments.forEach(item -> {
                item.setStatus(OrderTruckAssigmentStatus.REASSIGNED);
            });
            orderTruckRepository.saveAll(activeAssignments);
        }

        // 3. Создаем новое назначение
        OrderTruck newAssignment = new OrderTruck();
        newAssignment.setIdOrder(idOrder);
        newAssignment.setIdTruck(idTruck);
        newAssignment.setIdUser(idUser);
        newAssignment.setIdTruckUserAssigment(idTruckUserAssigment);
        newAssignment.setStatus(OrderTruckAssigmentStatus.ACTIVE);
        newAssignment.setFromSystem("Yard");
        newAssignment.setCreatedAt(LocalDateTime.now());
        newAssignment.setCreatedBy(userAdd);


        orderTruckRepository.save(newAssignment);

        RouteSheet routeSheet = routeSheetRepository.findByIdTruckUserAssignmentAndStatus(idTruckUserAssigment, RouteSheetStatus.ACTIVE);

        if(routeSheet == null){
            routeSheet = new RouteSheet();
            routeSheet.setIdTruckUserAssignment(idTruckUserAssigment);
            routeSheet.setIdOrder(idOrder);

            routeSheetRepository.save(routeSheet);
        }else {
            routeSheet.setIdOrder(idOrder);
            routeSheetRepository.save(routeSheet);
        }


        // 4. Сохраняем и возвращаем
        return newAssignment;
    }
}
