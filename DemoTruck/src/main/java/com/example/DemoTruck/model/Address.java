package com.example.DemoTruck.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ADDRESS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Address {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "address_id")
  private Long id;

  private String city;

  @Column(name = "postal_code")
  private String postalCode;

  private String street;
  private String number;

  private Double latitude;
  private Double longitude;
}