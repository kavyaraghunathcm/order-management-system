package com.kav.ordermanagementsvc.service;

import com.kav.ordermanagementsvc.constants.AppConstants;
import com.kav.ordermanagementsvc.constants.Level;
import com.kav.ordermanagementsvc.dao.CustomerRepository;
import com.kav.ordermanagementsvc.dao.DiscountRepository;
import com.kav.ordermanagementsvc.dao.OrderRepository;
import com.kav.ordermanagementsvc.dto.DiscountDto;
import com.kav.ordermanagementsvc.dto.ListResponseDto;
import com.kav.ordermanagementsvc.entity.Customer;
import com.kav.ordermanagementsvc.entity.Discount;
import com.kav.ordermanagementsvc.entity.Order;
import com.kav.ordermanagementsvc.exception.DiscountAlreadyClaimedException;
import com.kav.ordermanagementsvc.exception.ResourceAccessException;
import com.kav.ordermanagementsvc.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    DiscountRepository discountRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public DiscountDto claimDiscount(long discountId, long customerId, DiscountDto discountDto) {

        Discount discount=discountRepository.findById(discountId).orElseThrow(()->new ResourceNotFoundException("Discount","id",discountId));
        if(discount.getCustomer().getId()!=customerId){
            throw new ResourceAccessException("Customer","Discount","id",customerId,discountId);
        }
        if(discount.isClaimed()){
            throw new DiscountAlreadyClaimedException("id",discountId);
        }
        discount.setClaimed(discountDto.isClaimed());
        return modelMapper.map(discountRepository.save(discount),DiscountDto.class);
    }

    @Override
    @Async("asyncExecutor")
    public Future<Discount> generateDiscount(long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            Customer customer =customerRepository.findById(order.getCustomer().getId()).orElseThrow(()->new ResourceNotFoundException("Customer","id",order.getCustomer().getId()));
            int noOfOrders=customer.getNoOfOrders();
            if (noOfOrders== Level.GOLD.orderThreshold) {
                customer.setLevel(Level.GOLD);
            } else if (noOfOrders == AppConstants.PLATINUM_THRESHOLD) {
                customer.setLevel(Level.PLATINUM);
            }
            order.setCustomer(customer);
            customerRepository.save(customer);

            if (!customer.getLevel().equals(Level.REGULAR)) {
                try {
                    log.info("Starting discount generation");
                    order.setDiscountGenerated(true);
                    Discount discount = new Discount();
                    discount.setOrder(order);
                    discount.setCustomer(customer);
                    discount.setAmount(order.getPayment() * customer.getLevel().discount);
                    discountRepository.save(discount);
                    orderRepository.save(order);
                    return new AsyncResult<Discount>(discount);
                } catch (Exception e) {
                    System.out.println("Exception occurred while generating discount for order with id " + orderId
                            + "\n Sending Email with error details: " + e);
                }
            }
        }
        return null;
    }

    @Override
    public ListResponseDto<DiscountDto> findAll(Long customerId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));
        Sort sort = sortDir.equals(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Page<Discount> discountPage = discountRepository.findAllByCustomer(customer, PageRequest.of(pageNo, pageSize, sort));
        List<DiscountDto> content = discountPage
                .getContent()
                .stream()
                .map(discount -> modelMapper.map(discount, DiscountDto.class)).collect(Collectors.toList());
        return new ListResponseDto<>(content, discountPage.getNumber(), discountPage.getSize(), discountPage.getTotalElements(), discountPage.getTotalPages(), discountPage.isLast());

    }

    @Override
    public DiscountDto findById(long customerId, long id ) {
        Discount discount =discountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Discount", "id", id));
        if(discount.getCustomer().getId()!=customerId){
            throw new ResourceAccessException("Customer","Discount","id",customerId,id);
        }
        return modelMapper.map(discount, DiscountDto.class);
    }
}
