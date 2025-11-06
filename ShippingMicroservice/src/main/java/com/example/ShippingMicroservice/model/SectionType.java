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
@Table(name = "SECTION_TYPE")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SectionType {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "section_type_id")
  private Long id;

  @Column(name = "type")
  private String type;
}