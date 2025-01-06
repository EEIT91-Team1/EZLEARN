package org.ezlearn.controller;

import java.util.List;

import org.ezlearn.model.Notes;
import org.ezlearn.service.NotesService;
import org.ezlearn.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/courses")
public class NotesController {
	
	@Autowired
	private NotesService notesService;
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private HttpSession httpSession;
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@GetMapping("/{courseId}/notes")
	public ResponseEntity<?> getNotesByCourses(@PathVariable Long courseId) {
		if (usersService.islogin(httpSession) == false) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthenticated");
		}
		return ResponseEntity.ok(notesService.getNotesByCourses(courseId));
	}
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@PostMapping("/notes")
	public ResponseEntity<?> createNote(@RequestBody Notes note) {
		if (usersService.islogin(httpSession) == false) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthenticated");
		}
		return ResponseEntity.ok(notesService.createNote(note));
	}
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@PutMapping("/notes/{noteId}")
	public ResponseEntity<?> updateNote(@PathVariable Long noteId ,@RequestBody Notes note) {
		if (usersService.islogin(httpSession) == false) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthenticated");
		}
		
		if(notesService.isUnauthorized(httpSession, noteId)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
		}
		
		return ResponseEntity.ok(notesService.updateNote(noteId, note.getNoteTitle(), note.getNoteContent()));
	}
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@DeleteMapping("/notes/{noteId}")
	public ResponseEntity<?> deleteNotesByNoteId(@PathVariable Long noteId) {
		if (usersService.islogin(httpSession) == false) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthenticated");
		}
		
		if(notesService.isUnauthorized(httpSession, noteId)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
		}
		
		return ResponseEntity.ok(notesService.deleteNotesByNoteId(noteId));
	}

}
