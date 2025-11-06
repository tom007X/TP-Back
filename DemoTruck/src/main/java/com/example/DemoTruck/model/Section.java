package com.example.DemoTruck.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "SECTION")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString(exclude = "deposits")
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

  // External microservice reference
  @Column(name = "truck_id")
  private Long truckId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "section_type_id")
  private SectionType type;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "section_status_id", referencedColumnName = "section_state_id")
  private SectionStatus status;

  // ---- Pure M:N (inverse side) ----
  @ManyToMany(mappedBy = "sections", fetch = FetchType.LAZY)
  @Builder.Default
  private Set<Deposit> deposits = new LinkedHashSet<>();
}