package org.ezlearn.repository;

import java.util.Optional;

import org.ezlearn.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
	Optional<Users> findByEmail(String Email);
	Users findByUserId(Long userId);
	Users findByresetToken(String resetToken);
}
