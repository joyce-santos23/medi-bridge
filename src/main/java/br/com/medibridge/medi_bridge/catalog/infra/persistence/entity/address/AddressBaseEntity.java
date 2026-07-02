package br.com.medibridge.medi_bridge.catalog.infra.persistence.entity.address;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "address_bases")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressBaseEntity {

    @Id
    private UUID id;
    private String zipCode;
    private String street;
    private String neighborhood;
    private String city;
    private String state;
}
