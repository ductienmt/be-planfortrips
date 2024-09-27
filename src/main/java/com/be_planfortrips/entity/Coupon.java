package com.be_planfortrips.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
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
    Long id;

    @Column(name = "code", length = 50)
    String code;

    @ColumnDefault("Percent")
    @Column(name = "discount_type")
    DiscountType discountType;

    @Column(name = "discount_value", precision = 10, scale = 2)
    BigDecimal discountValue;

    @Column(name = "start_date")
    LocalDateTime startDate; // t nghĩ nó sẽ diễn ra từ mấy giờ nữa nên t sưẳ nha

    @Column(name = "end_date")
    LocalDateTime endDate;

    @Column(name = "use_limit")
    int useLimit;

    @ColumnDefault("0")
    @Column(name = "use_count")
    int useCount;

    @ColumnDefault("true")
    @Column(name = "is_active")
    boolean isActive;

    @OneToMany
    List<CouponRoom> couponRooms;
}
