package  com.kav.ordermanagementsvc.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "orders")
@Getter
@Setter
@RequiredArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    private double payment;


    @ManyToOne(fetch = FetchType.EAGER,cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.REMOVE})
    @JoinColumn(name = "customer_id")
    private Customer customer;


    @Column(name = "discount_generated", nullable = false)
    private boolean discountGenerated;


}
