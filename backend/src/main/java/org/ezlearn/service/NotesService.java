package org.ezlearn.service;

import java.util.List;
import java.util.Optional;

import org.ezlearn.model.Courses;
import org.ezlearn.model.Lessons;
import org.ezlearn.model.Notes;
import org.ezlearn.model.Users;
import org.ezlearn.repository.LessonsRepository;
import org.ezlearn.repository.NotesRepository;
import org.ezlearn.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;

@Service
public class NotesService {
	
	@Autowired
	private NotesRepository notesRepository;
	
	@Autowired
	private LessonsRepository lessonsRepository;
	
	@Autowired
	private HttpSession httpSession;
	
	public List<Notes> getNotesByCourses(Long courseId) {
		Courses courses = new Courses();
		courses.setCourseId(courseId);
		
		List<Lessons> lessons = lessonsRepository.findByCourses(courses);
		Users user = (Users) httpSession.getAttribute("user");
		return notesRepository.findByLessonsInAndUsersUserId(lessons, user.getUserId());
	}
	
	public Notes createNote(Notes note) {
		Users user = (Users) httpSession.getAttribute("user");
		note.setUsers(user);
		return notesRepository.save(note);
	}
	
	public Notes updateNote(Long noteId, String noteTitle, String noteContent) {
		Notes note = notesRepository.findById(noteId).orElseThrow(() -> new RuntimeException("Note not found"));
		note.setNoteTitle(noteTitle);
		note.setNoteContent(noteContent);
		return notesRepository.save(note);
	}
	
	@Transactional
	public Integer deleteNotesByNoteId(Long noteId) {
		return notesRepository.deleteByNoteId(noteId);
	}
	
	public Boolean isUnauthorized(HttpSession session, Long noteId) {
		Users loginUser = (Users) session.getAttribute("user");
		Notes note = notesRepository.findById(noteId).orElseThrow(() -> new RuntimeException("Note not found"));
		
		if(loginUser.getUserId() != note.getUsers().getUserId()) {
			return true;
		} else {
			return false;
		}
	}

}
