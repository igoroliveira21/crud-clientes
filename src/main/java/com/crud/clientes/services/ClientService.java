package com.crud.clientes.services;

import com.crud.clientes.entities.Client;
import com.crud.clientes.repositorys.ClientRepository;
import com.crud.clientes.services.exceptions.DatabaseException;
import com.crud.clientes.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;


@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public Client findById(Long id) {
        Client client = clientRepository.findById(id).get();
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
            Client entity = clientRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado id: " + id));

            entity.setName(obj.getName());
            entity.setIncome(obj.getIncome());
            entity.setChildren(obj.getChildren());

            return clientRepository.save(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }

    }


    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if(!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado id:" + id);
        }

        try{
            clientRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }
}
