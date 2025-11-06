package com.example.ShippingMicroservice.model;

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
@Table(name = "ROUTE")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Route {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "route_id")
  private Long id;

  @Column(name = "num_sections")
  private Integer numSections;

  @Column(name = "num_deposit")
  private Integer numDeposit;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "request_id")
  private ShippingRequest request;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "start_address_id")
  private Address startAddress;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "end_address_id")
  private Address endAddress;
}