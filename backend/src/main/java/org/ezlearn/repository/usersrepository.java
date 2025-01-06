package org.ezlearn.repository;

import java.util.List;
import java.util.Optional;

import org.ezlearn.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface usersrepository extends JpaRepository<Users, Long>{
	Optional<Users> findByEmail(String Email);
	Users findByUserId(Long id);
}
