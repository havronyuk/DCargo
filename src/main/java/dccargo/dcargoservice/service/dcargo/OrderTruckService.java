package dccargo.dcargoservice.service.dcargo;


import dccargo.dcargoservice.repository.dcargo.OrderTruckRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderTruckService {

    private final OrderTruckRepository orderTruckRepository;

}
