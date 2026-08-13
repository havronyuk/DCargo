package dccargo.dcargoservice.service.dcargo;


import dccargo.dcargoservice.enums.OrderTruckAssigmentStatus;
import dccargo.dcargoservice.model.dcargo.OrderTruck;
import dccargo.dcargoservice.repository.dcargo.OrderTruckRepository;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public OrderTruck assignTruckUserToOrder(Long idOrder) {

        List<OrderTruck> existAssigments = orderTruckRepository.findByIdOrderAndStatus(idOrder, OrderTruckAssigmentStatus.ACTIVE);

        if(!existAssigments.isEmpty()){

        }
        return null;

    }
}
