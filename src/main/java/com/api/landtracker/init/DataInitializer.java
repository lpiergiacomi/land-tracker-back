package com.api.landtracker.init;

import com.api.landtracker.model.entities.Client;
import com.api.landtracker.model.entities.Lot;
import com.api.landtracker.model.entities.Reserve;
import com.api.landtracker.model.entities.User;
import com.api.landtracker.repository.ClientRepository;
import com.api.landtracker.repository.LotRepository;
import com.api.landtracker.repository.ReserveRepository;
import com.api.landtracker.repository.UserRepository;
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
    private final ObjectMapper objectMapper;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        cargarDesdeJson("clients.json", Client.class, clientRepository);
        cargarDesdeJson("lots.json", Lot.class, lotRepository);
        cargarDesdeJson("users.json", User.class, userRepository);
        cargarDesdeJson("reserves.json", Reserve.class, reserveRepository);
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
