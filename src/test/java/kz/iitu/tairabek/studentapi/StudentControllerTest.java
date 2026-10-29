package kz.iitu.tairabek.studentapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    @Autowired
    private MockMvc mvc;

    private static final String AKERKE =
            "{\"firstName\":\"Akerke\",\"lastName\":\"Tairabek\",\"email\":\"akerke.test@iitu.kz\","
          + "\"groupName\":\"IT2-2307\",\"age\":20}";

    @Test
    void infoShowsStudentName() throws Exception {
        mvc.perform(get("/"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.author", is("Wrong Name")));
    }

    @Test
    void fullCrudFlow() throws Exception {
        String body = mvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON).content(AKERKE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lastName", is("Tairabek")))
                .andReturn().getResponse().getContentAsString();
        long id = Long.parseLong(body.replaceAll(".*\"id\":(\\d+).*", "$1"));

        mvc.perform(get("/api/students/" + id))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.firstName", is("Akerke")));

        mvc.perform(put("/api/students/" + id).contentType(MediaType.APPLICATION_JSON)
                        .content(AKERKE.replace("\"age\":20", "\"age\":21")))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.age", is(21)));

        mvc.perform(delete("/api/students/" + id)).andExpect(status().isNoContent());
        mvc.perform(get("/api/students/" + id)).andExpect(status().isNotFound());
    }

    @Test
    void nonexistentStudentReturns404() throws Exception {
        mvc.perform(get("/api/students/99999"))
           .andExpect(status().isNotFound())
           .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    void invalidStudentReturns400() throws Exception {
        mvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"\",\"lastName\":\"Tairabek\",\"email\":\"bad\"}"))
           .andExpect(status().isBadRequest());
    }
}
