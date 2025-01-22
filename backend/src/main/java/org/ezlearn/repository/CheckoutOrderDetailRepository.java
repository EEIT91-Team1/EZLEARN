package org.ezlearn.repository;

import org.ezlearn.model.CheckoutOrderDetail;
import org.ezlearn.model.CheckoutOrderDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckoutOrderDetailRepository extends JpaRepository<CheckoutOrderDetail, CheckoutOrderDetailId> {
} 