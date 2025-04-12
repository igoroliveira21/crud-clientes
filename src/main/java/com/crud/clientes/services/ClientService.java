package com.crud.clientes.services;

import com.crud.clientes.entities.Client;
import com.crud.clientes.repositorys.ClientRepository;
import com.crud.clientes.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public Client findById(Long id) {
            Client client = clientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
            return client;
    }

    @Transactional(readOnly = true)
    public Page<Client> findAll(Pageable pageable) {
        Page<Client> clients = clientRepository.findAll(pageable);
        return clients;
    }

    @Transactional
    public Client insert(Client client) {
        Client newClient = new Client();

        newClient.setName(client.getName());
        newClient.setCpf(client.getCpf());
        newClient.setIncome(client.getIncome());
        newClient.setBirthDate(client.getBirthDate());
        newClient.setChildren(client.getChildren());

        return clientRepository.save(newClient);
    }

    @Transactional
    public Client update(Long id, Client obj) {

        try {
            // Pega a referência do client com o ID
            Client entity = clientRepository.getReferenceById(id);

            // Atualiza os campos com base no objeto recebido
            entity.setName(obj.getName());
            entity.setCpf(obj.getCpf());
            entity.setIncome(obj.getIncome());
            entity.setBirthDate(obj.getBirthDate());
            entity.setChildren(obj.getChildren());


            return clientRepository.save(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado!");
        }

    }


    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            clientRepository.deleteById(id);
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

