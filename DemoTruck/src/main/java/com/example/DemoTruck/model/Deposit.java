package com.example.DemoTruck.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "DEPOSIT")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString(exclude = "sections")
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

  // ---- Pure M:N (owning side) ----
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "DEPOSIT_SECTION",
      joinColumns = @JoinColumn(name = "deposit_id"),
      inverseJoinColumns = @JoinColumn(name = "section_id")
  )
  @Builder.Default
  private Set<Section> sections = new LinkedHashSet<>();

  // Optional: convenience methods to keep both sides in sync
  public void addSection(Section section) {
    sections.add(section);
    section.getDeposits().add(this);
  }

  public void removeSection(Section section) {
    sections.remove(section);
    section.getDeposits().remove(this);
  }
}
