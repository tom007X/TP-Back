package com.example.ShippingMicroservice.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ROUTE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Route {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "route_id")
  private Long id;

  @Column(name = "num_sections")
  private Integer numSections;

  @Column(name = "num_deposit")
  private Integer numDeposit;

  @Column(name = "total_distance_km")
  private Double totalDistance;

  @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
  @JoinColumn(name = "shipping_request_id", nullable = false, unique = true)
  private ShippingRequest request;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "start_address_id")
  private Address startAddress;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "end_address_id")
  private Address endAddress;

  @Builder.Default
  @OneToMany(mappedBy = "route", cascade = { CascadeType.PERSIST, CascadeType.MERGE,
      CascadeType.REMOVE }, orphanRemoval = true)
  private List<Section> sections = new ArrayList<>();

  public void addSection(Section s) {
    if (s == null)
      return;
    sections.add(s);
    s.setRoute(this);
  }

  public void removeSection(Section s) {
    if (s == null)
      return;
    sections.remove(s);
    s.setRoute(null);
  }
}