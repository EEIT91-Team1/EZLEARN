package org.ezlearn.repository;

import java.util.List;

import org.ezlearn.model.Courses;
import org.ezlearn.model.Lessons;
import org.ezlearn.model.Notes;
import org.ezlearn.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotesRepository extends JpaRepository<Notes, Long> {
	
	List<Notes> findByLessonsInAndUsersUserId(List<Lessons> lessons, Long userId);
	
	Integer deleteByNoteId(Long noteId);
}
