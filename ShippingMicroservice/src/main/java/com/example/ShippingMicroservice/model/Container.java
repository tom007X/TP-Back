package com.example.ShippingMicroservice.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CONTAINER")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Container {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "container_id")
  private Long id;

  private BigDecimal weight;
  private BigDecimal volume;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "container_state_id")
  private ContainerStatus status;

  // External microservice reference
  @Column(name = "client_id")
  private Long clientId;
}