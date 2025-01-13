package org.ezlearn.repository;

import java.util.List;

import org.ezlearn.model.Users;
import org.ezlearn.model.WishList;
import org.ezlearn.model.WishListId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface WishListRepository extends JpaRepository<WishList,WishListId> {
	@Modifying
	@Query(value="DELETE FROM `wish_list` WHERE `wish_list`.`user_id` = ?1 AND `wish_list`.`course_id` = ?2",nativeQuery = true)
	int deleteWish(String userId,String courseId);
	
	public List<WishList> findByUsers(Users users);
}
