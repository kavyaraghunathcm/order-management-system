package  com.kav.ordermanagementsvc.entity;


import com.kav.ordermanagementsvc.constants.Level;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private String email;

    @Column(name = "level", columnDefinition = "ENUM('REGULAR', 'GOLD', 'PLATINUM')", nullable = false)
    @Enumerated(EnumType.STRING)
    private Level level= Level.REGULAR;

    @OneToMany(targetEntity = Order.class, fetch = FetchType.LAZY, mappedBy = "customer")
    private List<Order> orders;

    @OneToMany(targetEntity = Discount.class, fetch = FetchType.LAZY, mappedBy = "customer")
    private List<Discount> discounts;

    private int noOfOrders=0;

}
