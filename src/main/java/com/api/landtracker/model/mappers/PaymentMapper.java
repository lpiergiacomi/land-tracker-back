package com.api.landtracker.model.mappers;

import com.api.landtracker.model.dto.PaymentDTO;
import com.api.landtracker.model.entities.Payment;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.name", target = "clientName")
    @Mapping(source = "file.id", target = "file.id")
    PaymentDTO paymentToPaymentDTO(Payment payment);

    @InheritInverseConfiguration
    Payment paymentDTOToPayment(PaymentDTO paymentDTO);

    List<PaymentDTO> paymentsToPaymentsDTO(List<Payment> payments);

}

