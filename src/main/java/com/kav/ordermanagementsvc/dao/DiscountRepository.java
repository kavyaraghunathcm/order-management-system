package com.kav.ordermanagementsvc.dao;

import com.kav.ordermanagementsvc.entity.Customer;
import com.kav.ordermanagementsvc.entity.Discount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DiscountRepository extends JpaRepository<Discount,Long> {

    @Query("select d from Discount d where d.customer = :customer")
    Page<Discount> findAllByCustomer(@Param("customer") Customer customer, Pageable pageable);

    @Query("select d from Discount d where d.order.id = ?1")
    Discount findByOrderId(Long orderId);


}
