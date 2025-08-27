package com.inmobiliaria.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.inmobiliaria.backend.dto.ClienteRequest;
import com.inmobiliaria.backend.dto.ClienteResponse;
import com.inmobiliaria.backend.exception.ClienteNoEncontradoException;
import com.inmobiliaria.backend.model.Cliente;
import com.inmobiliaria.backend.repository.ClienteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public List<ClienteResponse> listaClientes() {
        return clienteRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ClienteResponse obtenerClientePorId(Integer id) throws ClienteNoEncontradoException {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNoEncontradoException("Cliente con ID " + id + " no encontrado."));
        return mapToResponse(cliente);
    }

    public ClienteResponse crearCliente(ClienteRequest request) {
        Cliente cliente = Cliente.builder()
                .nombre(request.getNombre())
                .direccion(request.getDireccion())
                .iva(request.getIva())
                .cuit(request.getCuit())
                .localidad(request.getLocalidad())
                .build();
        cliente = clienteRepository.save(cliente);
        return mapToResponse(cliente);
    }

    public ClienteResponse modificarCliente(Integer id, ClienteRequest request) throws ClienteNoEncontradoException {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNoEncontradoException("Cliente con ID " + id + " no encontrado."));
        if(request.getNombre() != null){
            cliente.setNombre(request.getNombre());
        }
        if (request.getDireccion() != null) {
            cliente.setDireccion(request.getDireccion());
        }
        if (request.getIva() != null){
            cliente.setIva(request.getIva());
        }
        if (request.getCuit() != null){
            cliente.setCuit(request.getCuit());
        }
        if (request.getLocalidad() != null){
            cliente.setLocalidad(request.getLocalidad());
        }
        
        cliente = clienteRepository.save(cliente);
        return mapToResponse(cliente);
    }

    public void eliminarCliente(Integer id) throws ClienteNoEncontradoException {
        if (!clienteRepository.existsById(id)) {
            throw new ClienteNoEncontradoException("Cliente con ID " + id + " no encontrado.");
        }
        clienteRepository.deleteById(id);
    }

    private ClienteResponse mapToResponse(Cliente cliente) {
        return ClienteResponse.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .direccion(cliente.getDireccion())
                .iva(cliente.getIva())
                .cuit(cliente.getCuit())
                .localidad(cliente.getLocalidad())
                .build();
    }
}