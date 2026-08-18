package dccargo.dcargoservice.service.dcargo;


import dccargo.dcargoservice.enums.OrderTruckAssigmentStatus;
import dccargo.dcargoservice.model.dcargo.OrderTruck;
import dccargo.dcargoservice.repository.dcargo.OrderTruckRepository;
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


    public OrderTruck create(OrderTruck orderTruck){

        if(orderTruck.getIdTruck() == null){
            throw new MainServiceException("ID авто не может быть null");
        }

        orderTruck.setStatus(OrderTruckAssigmentStatus.ACTIVE);

        orderTruckRepository.save(orderTruck);

        return orderTruck;

    }

        public OrderTruck assignTruckUserToOrder(Long idOrder, Long idTruck, Long idUser, String userAdd, Long idTruckUserAssigment) {

        List<OrderTruck> existAssigments = orderTruckRepository.findByIdOrderAndStatus(idOrder, OrderTruckAssigmentStatus.ACTIVE);

        if(!existAssigments.isEmpty()){
            OrderTruck orderTruck = new OrderTruck();
            orderTruck.setIdOrder(idOrder);
            orderTruck.setIdTruck(idTruck);
            orderTruck.setCreatedAt(LocalDateTime.now());
            orderTruck.setFromSystem("Yard");
            orderTruck.setStatus(OrderTruckAssigmentStatus.ACTIVE);
            orderTruck.setIdUser(idUser);
            orderTruck.setCreatedBy(userAdd);
            orderTruck.setIdTruckUserAssigment(idTruckUserAssigment);

        }


        return null;

    }
}
