package com.api.landtracker.init;

import com.api.landtracker.model.entities.*;
import com.api.landtracker.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final LotRepository lotRepository;
    private final ClientRepository clientRepository;
    private final ReserveRepository reserveRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        cargarDesdeJson("clients.json", Client.class, clientRepository);
        cargarDesdeJson("lots.json", Lot.class, lotRepository);
        cargarDesdeJson("users.json", User.class, userRepository);
        cargarDesdeJson("reserves.json", Reserve.class, reserveRepository);
        cargarDesdeJson("payments.json", Payment.class, paymentRepository);
    }

    private <T> void cargarDesdeJson(String jsonFilePath, Class<?> clase,  JpaRepository<T, ?> repository) {
        try {
            ClassPathResource resource = new ClassPathResource(jsonFilePath);

            List<T> objetos = objectMapper.readValue(resource.getInputStream(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, clase));
            repository.saveAll(objetos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
