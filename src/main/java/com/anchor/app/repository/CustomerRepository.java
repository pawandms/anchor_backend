package com.anchor.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.anchor.app.model.Customer;


public interface CustomerRepository extends MongoRepository<Customer, String>{
	Slice<Customer> findAllBySalary (double salary, Pageable pageable);
	Page<Customer> findAllByAgeGreaterThan(int age, Pageable pageable);
	
	Page<Customer> findByFirstnameLike(String firstname, Pageable pageable);
	
}