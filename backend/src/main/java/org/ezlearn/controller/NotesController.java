package org.ezlearn.controller;

import java.util.List;

import org.ezlearn.model.Notes;
import org.ezlearn.service.NotesService;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@GetMapping("/{courseId}/notes")
	public List<Notes> getNotesByCourses(@PathVariable Long courseId) {
		return notesService.getNotesByCourses(courseId);
	}
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@PostMapping("/notes")
	public Notes createNote(@RequestBody Notes note) {
		return notesService.createNote(note);
	}
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@PutMapping("/notes/{noteId}")
	public Notes updateNote(@PathVariable Long noteId ,@RequestBody Notes note) {
		return notesService.updateNote(noteId , note.getNoteTitle(), note.getNoteContent());
	}
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@DeleteMapping("/notes/{noteId}")
	public Integer deleteNotesByNoteId(@PathVariable Long noteId) {
		return notesService.deleteNotesByNoteId(noteId);
	}
	
	@PostMapping("/login") //模擬登入後，設定userId
    public String login(HttpSession session) {
        session.setAttribute("userId", 3L); //Long型別 => 3L
        return "User logged in with userId: 3";
    }

}
