package com.example.ShippingMicroservice.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;


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
@Table(name = "SHIPPING_REQUEST")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ShippingRequest {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "request_id")
  private Long id;

  @Column(name = "request_datetime")
  private LocalDateTime requestDatetime;

  @Column(name = "estimated_cost")
  private BigDecimal estimatedCost;

  @Column(name = "estimated_time")
  private String estimatedTime;

  @Column(name = "final_cost")
  private BigDecimal finalCost;

  @Column(name = "real_time")
  private String realTime;

  @Column(name = "request_status_id", nullable = false)
  private ShippingRequestStatus status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "container_id")
  private Container container;

  @Column(name = "client_id")
  private Long clientId;
}