package com.swodd.interactmap.controllers;

import com.swodd.interactmap.entities.Apartment;
import com.swodd.interactmap.repositories.ApartmentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/apartments")
public class ApartmentsController {

    @Autowired
    ApartmentsRepository apartmentsRepository;

    @PostMapping
    public ResponseEntity<Apartment> createApartment(@RequestBody Apartment apartment) {
        Apartment result = apartmentsRepository.save(apartment);
        if (result != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/pages/{pageId}/size/{size}")
    public ResponseEntity<List<Apartment>> getAllApartments(@PathVariable Integer pageId, @PathVariable Integer size) {
        Pageable pageable = PageRequest.of(pageId - 1, size);
        Page<Apartment> result = apartmentsRepository.findAll(pageable);
        if (result != null) {
            return ResponseEntity.status(HttpStatus.OK).body(result.getContent());
        } else return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("coordinates/{coordinates}/radius/{radius}/pages/{pageId}/size/{size}")
    public ResponseEntity<List<Apartment>> getAllApartmentsByCoordinates(@PathVariable String coordinates, @PathVariable Integer radius,
                                                                         @PathVariable Integer pageId, @PathVariable Integer size) {
        String[] parts = coordinates.split("_");
        int givenX;
        int givenY;
        try {
            givenX = Integer.parseInt(parts[0]);
            givenY = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Pageable pageable = PageRequest.of(pageId - 1, size);
        List<Apartment> result = apartmentsRepository.findAll(pageable).getContent().stream().filter(apartment ->
                isInside(givenX, givenY, radius, apartment.getCoordinates().getX(), apartment.getCoordinates().getY())).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Apartment> geApartmentById(@PathVariable Integer id) {
        Optional<Apartment> apartment = apartmentsRepository.findById(id);
        if (apartment.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(apartment.get());
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping
    public ResponseEntity<Object> updateApartmentById(@RequestBody Apartment apartment) {
        if (apartmentsRepository.existsById(apartment.getId())) {
            Apartment result = apartmentsRepository.save(apartment);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteApartmentById(@PathVariable Integer id) {
        if (apartmentsRepository.existsById(id)) {
            apartmentsRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private boolean isInside(int given_x, int given_y, int radius, int apartment_x, int apartment_y) {
        return (apartment_x - given_x) * (apartment_x - given_x) + (apartment_y - given_y) * (apartment_y - given_y) <= radius * radius;
    }
}

