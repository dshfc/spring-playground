package com.alllstate.compoZed.springplayground.Lesson;


        import org.junit.Test;
        import org.junit.runner.RunWith;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
        import org.springframework.boot.test.context.SpringBootTest;
        import org.springframework.test.annotation.Rollback;
        import org.springframework.test.context.junit4.SpringRunner;
        import org.springframework.test.web.servlet.MockMvc;
        import org.springframework.test.web.servlet.ResultActions;
        import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

        import javax.transaction.Transactional;
        import java.util.Arrays;
        import java.util.Collections;

        import static org.hamcrest.Matchers.hasSize;
        import static org.hamcrest.Matchers.is;
        import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
        import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LessonControllerRealDatabaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LessonRepository repository;


    @Transactional
    @Rollback
    @Test
    public void listReturnsExistingLessons() throws Exception {

        // Setup
        final LessonModel lessonOne = new LessonModel();
        lessonOne.setTitle("Spelling 001 with Dale oLtts");

        final LessonModel lessonTwo = new LessonModel();
        lessonTwo.setTitle("ACID for CRUDL");

        repository.save(Arrays.asList(lessonOne, lessonTwo));

        // Exercise
        mockMvc.perform(get("/lessons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].id", is(lessonOne.getId().intValue())))
                .andExpect(jsonPath("$[0].title", is("Spelling 001 with Dale oLtts")));




    }

    @Transactional
    @Rollback
    @Test
    public void getReturnsExistingLesson() throws Exception {

        // Setup
        final LessonModel lessonOne = new LessonModel();
        lessonOne.setTitle("Java 101");
        repository.save(lessonOne);
        Long id = lessonOne.getId();

        // Exercise
        final MockHttpServletRequestBuilder getOneLesson = get("/lessons/{id}", id);
        final ResultActions response = mockMvc.perform(getOneLesson);

        response.andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(lessonOne.getId().intValue())))
        .andExpect(jsonPath("$.title", is("Java 101")));

    }
}


