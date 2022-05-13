package com.example.project.springbootproject;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import com.example.project.springbootproject.domain.BoardDTO;
import com.example.project.springbootproject.domain.BoardDTO.InsertDto;
import com.example.project.springbootproject.domain.UserDTO;
import com.example.project.springbootproject.util.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(properties = "spring.config.location=" +
    "classpath:/application.properties")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
class SpringBootProjectApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

//    @Test
//    @Rollback(value = false)
    void contextLoads() throws Exception {
        for (long i = 0; i < 30; i++) {
            ResultActions result = this.mockMvc.perform(post("/post/insert")
                .content(objectMapper.writeValueAsString(makeRequest()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            );
            System.out.println(i);
        }

    }

    @Test
    void insertPost() throws Exception {
        ResultActions result = this.mockMvc.perform(post("/post/insert")
            .content(objectMapper.writeValueAsString(makeRequest()))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
            .andDo(document("post-insert", preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("title").type(JsonFieldType.STRING)
                        .description("게시글 제목"),
                    fieldWithPath("content").type(JsonFieldType.STRING)
                        .description("게시글 내용"),
                    fieldWithPath("username").type(JsonFieldType.STRING)
                        .description("게시글 등록자")
                ),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("isSuccess"),
                    fieldWithPath("response").type(JsonFieldType.STRING)
                        .description("Response Body"),
                    subsectionWithPath("error").type(JsonFieldType.OBJECT).description("Error Body")
                        .optional()
                )
            ));
    }

    private InsertDto makeRequest() {
        InsertDto InsertDto = new InsertDto("title", "content", "userTest");

        return InsertDto;
    }

    @Test
    void getBoardList() throws Exception {
        ResultActions result = this.mockMvc.perform(get("/post-list").param("size", "10").param("page", "0")
            .header("userToken", getUserToken())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        );

        makeDocsGetBoardList(result);
    }

    private void makeDocsGetBoardList(ResultActions result) throws Exception {
        result.andExpect(status().isOk())
            .andDo(document("board-list", preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                    parameterWithName("page").description("조회할 페이지"),
                    parameterWithName("size").description("한 페이지당 크기")
                ),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("isSuccess"),
                    subsectionWithPath("response").type(JsonFieldType.OBJECT)
                        .description("Response Body"),
                    fieldWithPath("response.content.[].boardId").type(JsonFieldType.NUMBER)
                        .description("게시글 아이디"),
                    fieldWithPath("response.content.[].content").type(JsonFieldType.STRING)
                        .description("게시글 내용"),
                    fieldWithPath("response.content.[].delete").type(JsonFieldType.NUMBER)
                        .description("게시글 삭제 여부"),
                    fieldWithPath("response.content.[].title").type(JsonFieldType.STRING)
                        .description("게시글 제목"),
                    fieldWithPath("response.content.[].username").type(JsonFieldType.STRING)
                        .description("게시글 등록자"),
                    subsectionWithPath("error").type(JsonFieldType.OBJECT).description("Error Body")
                        .optional()
                )
            ));
    }

    String getUserToken() throws Exception {
        ResultActions result = this.mockMvc.perform(post("/login")
            .content(objectMapper.writeValueAsString(makeRequestForLogIn()))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        );

        String resultString = result.andReturn().getResponse().getContentAsString();
        Map<String, Object> map = new JacksonJsonParser().parseMap(resultString);
        String response = (String) map.get("response");

        return response;
    }

    private UserDTO makeRequestForLogIn() {
        return UserDTO.builder()
            .username("aceenter")
            .password("12345")
            .email("aceenter@aceenter.com")
            .phone("010-5555-7895")
            .build();
    }

    @Test
    void getMyBoardList() throws Exception {
        ResultActions result = this.mockMvc.perform(get("/my-post").param("size", "10").param("page", "0")
            .header("userToken", getUserToken())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        );

        makeDocsGetMyBoardList(result);
    }

    private void makeDocsGetMyBoardList(ResultActions result) throws Exception {
        result.andExpect(status().isOk())
            .andDo(document("myBoard-list", preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                    parameterWithName("page").description("조회할 페이지"),
                    parameterWithName("size").description("한 페이지당 크기")
                ),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("isSuccess"),
                    subsectionWithPath("response").type(JsonFieldType.OBJECT)
                        .description("Response Body"),
                    fieldWithPath("response.content.[].boardId").type(JsonFieldType.NUMBER)
                        .description("게시글 아이디"),
                    fieldWithPath("response.content.[].content").type(JsonFieldType.STRING)
                        .description("게시글 내용"),
                    fieldWithPath("response.content.[].delete").type(JsonFieldType.NUMBER)
                        .description("게시글 삭제 여부"),
                    fieldWithPath("response.content.[].title").type(JsonFieldType.STRING)
                        .description("게시글 제목"),
                    fieldWithPath("response.content.[].username").type(JsonFieldType.STRING)
                        .description("게시글 등록자"),
                    subsectionWithPath("error").type(JsonFieldType.OBJECT).description("Error Body")
                        .optional()
                )
            ));
    }

    @Test
    void login() throws Exception {
        ResultActions result = this.mockMvc.perform(post("/login")
            .content(objectMapper.writeValueAsString(makeRequestForLogIn()))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
            .andDo(document("login", preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("username").type(JsonFieldType.STRING)
                        .description("사용자 이름"),
                    fieldWithPath("password").type(JsonFieldType.STRING)
                        .description("사용자 비밀번호"),
                    fieldWithPath("email").type(JsonFieldType.STRING)
                        .description("사용자 이메일"),
                    fieldWithPath("phone").type(JsonFieldType.STRING)
                        .description("사용자 번호")
                ),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("isSuccess"),
                    subsectionWithPath("response").type(JsonFieldType.STRING)
                        .description("Response Body"),
                    subsectionWithPath("error").type(JsonFieldType.OBJECT).description("Error Body")
                        .optional()
                )
            ));
    }

    @Test
    void logout() throws Exception {
        ResultActions result = this.mockMvc.perform(get("/user/logout")
            .header("userToken", getUserToken())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
            .andDo(document("logout", preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("isSuccess"),
                    subsectionWithPath("response").type(JsonFieldType.NULL)
                        .description("Response Body"),
                    subsectionWithPath("error").type(JsonFieldType.OBJECT).description("Error Body")
                        .optional()
                )
            ));
    }

    @Test
    void register() throws Exception {
        ResultActions result = this.mockMvc.perform(post("/register")
            .content(objectMapper.writeValueAsString(makeRequestForRegister()))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
            .andDo(document("register", preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("username").type(JsonFieldType.STRING)
                        .description("사용자 이름"),
                    fieldWithPath("password").type(JsonFieldType.STRING)
                        .description("사용자 비밀번호"),
                    fieldWithPath("email").type(JsonFieldType.STRING)
                        .description("사용자 이메일"),
                    fieldWithPath("phone").type(JsonFieldType.STRING)
                        .description("사용자 번호")
                ),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("isSuccess"),
                    subsectionWithPath("response").type(JsonFieldType.STRING)
                        .description("Response Body"),
                    subsectionWithPath("error").type(JsonFieldType.OBJECT).description("Error Body")
                        .optional()
                )
            ));
    }

    private UserDTO makeRequestForRegister() throws Exception {
        return UserDTO.builder()
            .username("0503Test")
            .password("12345")
            .email("0503Test@aceenter.com")
            .phone("010-1235-8965")
            .build();
    }

    @Test
    void postDetail() throws Exception {
        ResultActions result = this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/detail/{boardId}", 14)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
            .andDo(document("post-detail", preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("boardId").description("게시글 ID")
                ),
                responseFields(
                    fieldWithPath("boardId").type(JsonFieldType.NUMBER)
                        .description("게시글 아이디"),
                    fieldWithPath("content").type(JsonFieldType.STRING)
                        .description("게시글 내용"),
                    fieldWithPath("delete").type(JsonFieldType.NUMBER)
                        .description("게시글 삭제 여부"),
                    fieldWithPath("title").type(JsonFieldType.STRING)
                        .description("게시글 제목"),
                    fieldWithPath("username").type(JsonFieldType.STRING)
                        .description("게시글 등록자")
                )
            ));
    }

    @Test
    void myPostDetail() throws Exception {
        ResultActions result = this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/post/update/{boardId}", 14)
                .header("userToken", getUserToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
            .andDo(document("myPost-detail", preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("boardId").description("게시글 ID")
                ),
                responseFields(
                    fieldWithPath("boardId").type(JsonFieldType.NUMBER)
                        .description("게시글 아이디"),
                    fieldWithPath("content").type(JsonFieldType.STRING)
                        .description("게시글 내용"),
                    fieldWithPath("delete").type(JsonFieldType.NUMBER)
                        .description("게시글 삭제 여부"),
                    fieldWithPath("title").type(JsonFieldType.STRING)
                        .description("게시글 제목"),
                    fieldWithPath("username").type(JsonFieldType.STRING)
                        .description("게시글 등록자")
                )
            ));
    }

    @Test
    void postUpdate() throws Exception {
        ResultActions result = this.mockMvc.perform(
            RestDocumentationRequestBuilders.put("/update/{boardId}/{title}/{content}", 14,
                    "0510title", "0510testcode")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
            .andDo(document("post-update", preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("boardId").description("게시글 ID"),
                    parameterWithName("title").description("게시글 제목"),
                    parameterWithName("content").description("게시글 내용")
                ),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("isSuccess"),
                    subsectionWithPath("response").type(JsonFieldType.STRING)
                        .description("Response Body"),
                    subsectionWithPath("error").type(JsonFieldType.OBJECT).description("Error Body")
                        .optional()
                )
            ));
    }

    @Test
    void postDelete() throws Exception {
        ResultActions result = this.mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/post/delete/{boardId}", 14)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
            .andDo(document("post-delete", preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("boardId").description("게시글 ID")
                ),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("isSuccess"),
                    subsectionWithPath("response").type(JsonFieldType.STRING)
                        .description("Response Body"),
                    subsectionWithPath("error").type(JsonFieldType.OBJECT).description("Error Body")
                        .optional()
                )
            ));
    }

    @Test
    void usernameCheck() throws Exception {
        ResultActions result = this.mockMvc.perform(
            RestDocumentationRequestBuilders.post("/usernameCheck").param("username", "testUser")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
            .andDo(document("username-check", preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("isSuccess"),
                    subsectionWithPath("response").type(JsonFieldType.BOOLEAN)
                        .description("Response Body"),
                    subsectionWithPath("error").type(JsonFieldType.OBJECT).description("Error Body")
                        .optional()
                )
            ));
    }

    private String makeRequestForUsername() throws Exception {
        return "testUser";
    }

    @Test
    void postUserToken() throws Exception {
        ResultActions result = this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/post/userToken")
                .header("userToken", getUserToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
            .andDo(document("user-token", preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("isSuccess"),
                    subsectionWithPath("response").type(JsonFieldType.STRING)
                        .description("Response Body"),
                    subsectionWithPath("error").type(JsonFieldType.OBJECT).description("Error Body")
                        .optional()
                )
            ));
    }
}
