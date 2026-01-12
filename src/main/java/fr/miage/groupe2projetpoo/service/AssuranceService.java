package fr.miage.groupe2projetpoo.service;

import fr.miage.groupe2projetpoo.entity.assurance.Assurance;
import fr.miage.groupe2projetpoo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssuranceService {

    private final UserRepository userRepository;

    @Autowired
    public AssuranceService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Assurance> getAllAssurances() {
        return userRepository.getAllAssurances();
    }

    public Optional<Assurance> getAssuranceById(int id) {
        return userRepository.findAssuranceById(id);
    }
}
