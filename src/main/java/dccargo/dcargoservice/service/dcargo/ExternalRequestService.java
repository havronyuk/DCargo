package dccargo.dcargoservice.service.dcargo;

import dccargo.dcargoservice.dto.dcargo.TruckAndUserDTO;
import dccargo.dcargoservice.dto.dcargo.mapper.TruckAndUserDTOMapper;
import dccargo.dcargoservice.dto.dcargo.mapper.TruckDTOMapper;
import dccargo.dcargoservice.model.dcargo.Truck;
import dccargo.dcargoservice.model.dcargo.TruckUserAssignment;
import dccargo.dcargoservice.model.dcargo.User;
import dccargo.dcargoservice.repository.dcargo.TruckRepository;
import dccargo.dcargoservice.repository.dcargo.TruckUserAssignmentRepository;
import dccargo.dcargoservice.repository.dcargo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExternalRequestService {

    private final TruckRepository truckRepository;
    private final UserRepository userRepository;
    private final TruckUserAssignmentRepository truckUserAssignmentRepository;
    private final TruckAndUserDTOMapper truckAndUserDTOMapper;
    private final OrderTruckService orderTruckService;


    public List<TruckAndUserDTO> getTruckUsersByWorkDate(LocalDate workDate) {

        List<TruckUserAssignment> assignments =
                truckUserAssignmentRepository.findByDateFrom(
                        workDate.atStartOfDay(),
                        workDate.plusDays(1).atStartOfDay()
                );

        if (assignments.isEmpty()) {
            return List.of();
        }

        List<Long> userIds = assignments.stream()
                .map(TruckUserAssignment::getUserId)
                .distinct()
                .toList();

        List<Long> truckIds = assignments.stream()
                .map(TruckUserAssignment::getTruckId)
                .distinct()
                .toList();

        Map<Long, User> users = userRepository.findAllByIdUserIn(userIds)
                .stream()
                .collect(Collectors.toMap(
                        User::getIdUser,
                        Function.identity()
                ));

        Map<Long, Truck> trucks = truckRepository.findAllByIdIn(truckIds)
                .stream()
                .collect(Collectors.toMap(
                        Truck::getId,
                        Function.identity()
                ));

        return assignments.stream()
                .map(assignment -> truckAndUserDTOMapper.toDTO(
                        assignment,
                        users.get(assignment.getUserId()),
                        trucks.get(assignment.getTruckId())
                ))
                .toList();
    }



}
