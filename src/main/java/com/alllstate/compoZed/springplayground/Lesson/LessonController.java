package com.alllstate.compoZed.springplayground.Lesson;

import org.springframework.web.bind.annotation.*;


/**
 * Created by localadmin on 7/18/17.
 */
@RestController
@RequestMapping("/lessons")
final class LessonsController {

    private final LessonRepository repository;

    public LessonsController(LessonRepository repository) {
        this.repository = repository;
    }

    @GetMapping("")
    public Iterable<LessonModel> all() {
        return this.repository.findAll();
    }

    @PostMapping("")
    public LessonModel create(@RequestBody LessonModel lesson) {
        return this.repository.save(lesson);


    }

    @GetMapping("/{id}")
    public LessonModel read(@PathVariable Long id) {

        return this.repository.findOne(id);

    }


    @PutMapping("/{id}")
    public LessonModel update (@PathVariable Long id, @RequestBody LessonModel lesson) {
         LessonModel foundLesson = repository.findOne(id);
         foundLesson.setTitle(lesson.getTitle());
         return repository.save(foundLesson);

    }

    @DeleteMapping("/{id}")
    public void delete (@PathVariable Long id) {
        this.repository.delete(id);
    }
}

