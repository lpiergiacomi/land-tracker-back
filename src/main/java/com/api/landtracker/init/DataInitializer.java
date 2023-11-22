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
        loadFromJson("clients.json", Client.class, clientRepository);
        List<Lot> lots = loadFromJson("lots.json", Lot.class, lotRepository);
        List<User> users = loadFromJson("users.json", User.class, userRepository);
        loadFromJson("reserves.json", Reserve.class, reserveRepository);
        loadFromJson("payments.json", Payment.class, paymentRepository);

        users.forEach(user -> {
            user.setAssignedLots(lots);
        });
        userRepository.saveAll(users);

    }

    private <T> List<T> loadFromJson(String jsonFilePath, Class<?> className, JpaRepository<T, ?> repository) {
        List<T> objects = null;
        try {
            ClassPathResource resource = new ClassPathResource(jsonFilePath);

            objects = objectMapper.readValue(resource.getInputStream(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, className));
            repository.saveAll(objects);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return objects;
    }
}
