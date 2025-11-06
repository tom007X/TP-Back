package com.example.ShippingMicroservice.model;

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
@Table(name = "CONTAINER_STATUS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ContainerStatus {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "container_state_id")
  private Long id;

  private String status;
}
