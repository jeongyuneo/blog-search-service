package com.jeongyuneo.blogsearchservice.global.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@EnableMockMvc
@AutoConfigureRestDocs
public abstract class ApiDocument {

    protected static final String CONTEXT_PATH = "/api/v1";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("직렬화 오류");
        }
    }

    protected void printAndMakeSnippet(ResultActions resultActions, String title) throws Exception {
        resultActions.andDo(print())
                .andDo(toDocument(title));
    }

    protected RestDocumentationResultHandler toDocument(String title) {
        return document(title, getDocumentRequest(), getDocumentResponse());
    }

    private OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(
                modifyUris()
                        .scheme("https")
                        .host("blog-search-service")
                        .removePort(),
                prettyPrint());
    }

    private OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(prettyPrint());
    }
}
