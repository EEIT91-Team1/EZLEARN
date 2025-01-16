package org.ezlearn.repository;

import java.util.List;

import org.ezlearn.model.Lessons;
import org.ezlearn.model.Questions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionsRepository extends JpaRepository<Questions, Long>{

	List<Questions> findByLesson(Lessons lesson);
	
	List<Questions> findByLessonIn(List<Lessons> lessons);
	
	Integer deleteByQuestionId(Long questionId);
	
}
