package com.kav.ordermanagementsvc.dao;

import com.kav.ordermanagementsvc.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    @Query("select c from Customer c where c.noOfOrders = :noOfOrders")
    List<Customer> findByNoOfOrders(@Param("noOfOrders") @Nullable int noOfOrders);

}
