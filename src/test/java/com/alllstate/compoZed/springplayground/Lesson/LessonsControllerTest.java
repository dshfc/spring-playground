package com.alllstate.compoZed.springplayground.Lesson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.bind.annotation.RestController;


import java.util.*;


import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by localadmin on 7/18/17.
 */
@WebMvcTest(LessonsController.class)
@RunWith(SpringRunner.class)
public class LessonsControllerTest {


    @Autowired
    public  MockMvc mockMvc;

    @MockBean
    public LessonRepository repository;

    @Test
    public void createDelegateToRepository() throws Exception{

        when(repository.save(any(LessonModel.class))).then(returnsFirstArg());
        final MockHttpServletRequestBuilder makeNewLesson = post("/lessons")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"Mock another one\"}");
        final ResultActions resultActions = mockMvc.perform(makeNewLesson);
                resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("$.title", is("Mock another one")));

                verify(repository).save(any(LessonModel.class));

    }


  //  test if return a list of lessonModel
    @Test
    public void listDelegateToRepository() throws Exception{
//        array = [lesson1, lesson2, ...]
        LessonModel lesson1 = new LessonModel();
        LessonModel lesson2 = new LessonModel();
        Long id1 = new Random().nextLong();
        Long id2 = new Random().nextLong();
        lesson1.setId(id1);
        lesson2.setId(id2);
        lesson1.setTitle("title one");
        lesson2.setTitle("title two");

        when(repository.findAll()).thenReturn(Arrays.asList(lesson1, lesson2));

        final MockHttpServletRequestBuilder allLessons = get("/lessons");
//                .contentType(MediaType.APPLICATION_JSON).content("{\"title\": \"Mock another one\"}");
        final ResultActions res = mockMvc.perform(allLessons)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title",is("title one")))
                .andExpect(jsonPath("$[1].id",is(id2)));

        verify(repository).findAll();
    }

    @Test
    public void getDelegateToRepository() throws Exception {

        //setup
        LessonModel lesson1 = new LessonModel();
        Long id1 = 1L;
        lesson1.setId(id1);
        lesson1.setTitle("This is lesson 1");

        when(repository.findOne(id1)).thenReturn(lesson1);

        //exercise
        final MockHttpServletRequestBuilder oneLesson = get("/lessons/1");
        final ResultActions response = mockMvc.perform(oneLesson);

        //assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("This is lesson 1")));

        verify(repository).findOne(id1);
    }

    @Test
    public void updateDelegateToRepository() throws Exception {

        //setup
        LessonModel lesson1 = new LessonModel();
        Long id1 = 1L;
        lesson1.setId(id1);
        lesson1.setTitle("Blue");

        LessonModel lesson2 = new LessonModel();
        Long id2 = 2L;
        lesson2.setId(id2);
        lesson1.setTitle("Green");

        when(repository.findOne(id2)).thenReturn(lesson2);
        when(repository.save(lesson2)).thenReturn(lesson2);


        //exercise
        final MockHttpServletRequestBuilder updateOneLesson = put("/lessons/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Red\"}");

        final ResultActions response = mockMvc.perform(updateOneLesson);

        //assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.title", is("Red")));

        verify(repository).save(lesson2);
    }

    @Test
    public void deleteDelegateToRepository() throws Exception {

        LessonModel lesson1 = new LessonModel();
        Long id1 = 1L;
        lesson1.setId(id1);
        lesson1.setTitle("Blue");

        LessonModel lesson2 = new LessonModel();
        Long id2 = 2L;
        lesson2.setId(id2);
        lesson1.setTitle("Green");

        final MockHttpServletRequestBuilder deleteOneLesson = delete("/lessons/2");
        final ResultActions response = mockMvc.perform(deleteOneLesson);

        response.andExpect(status().isOk());

        verify(repository).delete(id2);

    }

}