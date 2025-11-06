package com.example.DemoTruck.model;

import java.math.BigDecimal;

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
@Table(name = "TARIFF")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Tariff {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "tariff_id")
  private Long id;

  private String description;

  @Column(name = "value")
  private BigDecimal value;
}
