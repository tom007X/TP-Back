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
@Table(name = "DEPOSIT")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Deposit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "deposit_id")
  private Long id;

  private String name;

  @Column(name = "daily_storage_cost")
  private BigDecimal dailyStorageCost;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "address_id")
  private Address address;

}
