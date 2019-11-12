package com.swodd.interactmap.entities;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class Apartment {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(nullable = false)
    private Integer rooms;
    @Column(nullable = false)
    private Double quadrature;
    @Column(nullable = false)
    private String description;
    @Size(min = 1, max = 10)
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(nullable=false)
    private List<Image> images;
    @Column(nullable = false)
    private String address;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(nullable=false)
    private Point coordinates;

    public Apartment() {
    }

    public Apartment(Integer rooms, Double quadrature, String description, List<Image> images, String address, Point coordinates) {
        this.rooms = rooms;
        this.quadrature = quadrature;
        this.description = description;
        this.images = images;
        this.address = address;
        this.coordinates = coordinates;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public Double getQuadrature() {
        return quadrature;
    }

    public void setQuadrature(Double quadrature) {
        this.quadrature = quadrature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return "Apartment{" +
                "id=" + id +
                ", rooms=" + rooms +
                ", quadrature=" + quadrature +
                ", description='" + description + '\'' +
                ", images=" + images +
                ", address='" + address + '\'' +
                ", coordinates=" + coordinates +
                '}';
    }
}
