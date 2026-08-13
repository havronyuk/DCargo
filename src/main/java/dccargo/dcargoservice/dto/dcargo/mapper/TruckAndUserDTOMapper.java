package dccargo.dcargoservice.dto.dcargo.mapper;

import dccargo.dcargoservice.model.dcargo.TruckUserAssignment;
import org.springframework.stereotype.Component;

import dccargo.dcargoservice.dto.dcargo.TruckAndUserDTO;
import dccargo.dcargoservice.model.dcargo.Truck;
import dccargo.dcargoservice.model.dcargo.User;

@Component
public class TruckAndUserDTOMapper {

    public TruckAndUserDTO toDTO(TruckUserAssignment truckUserAssignment, User user, Truck truck) {

        TruckAndUserDTO dto = new TruckAndUserDTO();

        dto.setIdTruckUserAssigment(truckUserAssignment.getId());
        dto.setDateFrom(truckUserAssignment.getDateFrom());
        dto.setDateTo(truckUserAssignment.getDateTo());

        dto.setIdUser(user.getIdUser());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setPatronymic(user.getPatronymic());
        dto.setLoginTelephone(user.getLoginTelephone());

        dto.setIdTruck(truck.getId());
        dto.setRegistrationNumber(truck.getRegistrationNumber());
        dto.setModel(truck.getModel());
        dto.setMaxWeightKg(truck.getMaxWeightKg());
        dto.setMaxEuroPallets(truck.getMaxEuroPallets());

        return dto;
    }
}