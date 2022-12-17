package com.kav.ordermanagementsvc.dao;

import com.kav.ordermanagementsvc.entity.Customer;
import com.kav.ordermanagementsvc.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query("select o from Order o where o.customer = :customer")
    Page<Order> findAllByCustomer(@Param("customer") Customer customer, Pageable pageable);
}
