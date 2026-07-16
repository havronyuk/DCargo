package dccargo.dcargoservice.service.dcargo;


import dccargo.dcargoservice.model.dcargo.Truck;
import dccargo.dcargoservice.repository.dcargo.TruckRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor  // генерирует конструктор для всех final полей
public class TruckService {

    private final TruckRepository truckRepository;
    

}
