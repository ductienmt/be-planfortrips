package com.be_planfortrips.models;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class EnterpriseLinkType {
    @Id
    int id;
    @ManyToOne
            @JoinColumn(name = "account_enterprise_id")
    AccountEnterprise accountEnterprise;
    @ManyToOne
    @JoinColumn(name = "type_enterprise_detail_id")
    TypeEnterpriseDetail typeEnterpriseDetail;
}
