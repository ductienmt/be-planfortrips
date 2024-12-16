package com.be_planfortrips.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "coupons")
public class Coupon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "code", length = 50)
    String code;

    @Column(name = "discount_type")
    @Enumerated(EnumType.STRING)
    DiscountType discountType;

    @Column(name = "discount_value", precision = 10, scale = 2)
    BigDecimal discountValue;

    @Column(name = "start_date")
    LocalDate startDate;

    @Column(name = "end_date")
    LocalDate endDate;

    @Column(name = "use_limit")
    int useLimit;

    @Column(name = "use_count")
    int useCount = 0;

    @Column(name = "is_active")
    boolean isActive = true;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonManagedReference
    List<CouponRoom> couponRooms;

    @ManyToOne
    @JsonIgnore
    AccountEnterprise accountEnterprise;

    @ManyToMany(mappedBy = "coupons", cascade = CascadeType.REMOVE)
    @JsonIgnore
    List<Ticket> tickets;
}
