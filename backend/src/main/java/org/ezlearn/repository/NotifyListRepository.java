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
	int checkedAll(Long userId);
	
	@Modifying
	@Query(value="UPDATE `notify_list` SET `checked` = '1' WHERE `notify_id` = ?2 AND `user_id`=?1",nativeQuery = true)
	int checkedNotify(Long userId,Long notifyId);
	
	@Modifying
	@Query(value="UPDATE `notify_list` nl JOIN notify n ON nl.notify_id=n.notify_id SET `checked` = '1' WHERE nl.user_id = ?1 AND n.course_id=?2",nativeQuery = true)
	int checkedCourse(Long userId,Long courseId);
	
	@Modifying
	@Query(value="DELETE FROM `notify_list` WHERE `user_id` = ?1",nativeQuery = true)
	int deleteAll(Long userId);
	
	@Modifying
	@Query(value="DELETE FROM `notify_list` WHERE `notify_id` = ?2 AND `user_id`=?1",nativeQuery = true)
	int deleteNotify(Long userId,Long notifyId);
	
	@Modifying
	@Query(value="DELETE nl FROM `notify_list` nl JOIN notify n ON nl.notify_id=n.notify_id WHERE nl.user_id = ?1 AND n.course_id=?2",nativeQuery = true)
	int deleteCourse(Long userId,Long courseId);
}