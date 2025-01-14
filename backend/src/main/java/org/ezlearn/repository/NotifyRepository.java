package org.ezlearn.repository;

import org.ezlearn.model.Notify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface NotifyRepository extends JpaRepository<Notify, Long> {
	
}