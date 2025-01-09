package org.ezlearn.repository;

import java.util.List;

import org.ezlearn.model.NotifyList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface NotifyListRepository extends JpaRepository<NotifyList, Long> {
	
	List<NotifyList> findByUserId(Long userId);
	
	@Modifying
	@Query(value="UPDATE `notify_list` SET `checked` = '1' WHERE `user_id` = ?1",nativeQuery = true)
	int checked(Long userId);
}
