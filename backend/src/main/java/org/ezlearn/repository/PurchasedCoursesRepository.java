package org.ezlearn.repository;

import java.util.List;

import org.ezlearn.model.PurchasedCourses;
import org.ezlearn.model.PurchasedCoursesId;
import org.ezlearn.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchasedCoursesRepository extends JpaRepository<PurchasedCourses, PurchasedCoursesId> {

	List<PurchasedCourses> findByUsers(Users users);

}
