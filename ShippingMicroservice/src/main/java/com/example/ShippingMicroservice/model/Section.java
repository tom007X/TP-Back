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
@Table(name = "SECTION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Section {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "section_id")
  private Long id;

  @Column(name = "datetime_start")
  private LocalDateTime datetimeStart;

  @Column(name = "datetime_end")
  private LocalDateTime datetimeEnd;

  @Column(name = "estimate_cost")
  private BigDecimal estimateCost;

  @Column(name = "real_cost")
  private BigDecimal realCost;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "route_id")
  private Route route;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "start_address_id")
  private Address startAddress;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "end_address_id")
  private Address endAddress;

  @Column(name = "truck_id")
  private Long truckId;

  @Column(name = "section_type_id", nullable = false)
  private SectionType type;

  @Column(name = "section_status_id", nullable = false)
  private SectionStatus status;

  @ManyToOne
  @JoinColumn(name = "deposit_at_start_id")
  private Deposit depositAtStart;

  @ManyToOne
  @JoinColumn(name = "deposit_at_end_id")
  private Deposit depositAtEnd;

  @Column(name = "distance")
  private Double distance;
}