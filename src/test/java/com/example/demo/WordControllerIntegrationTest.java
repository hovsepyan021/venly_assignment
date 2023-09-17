package com.example.demo;

import com.example.demo.models.WordRelation;
import com.example.demo.models.WordRelationInversed;
import com.example.demo.requests.AddWordRequest;
import com.example.demo.requests.GetPathRequest;
import com.example.demo.validators.ValidationErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.repositories.WordRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@AutoConfigureMockMvc
public class WordControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private WordRelationRepository wordRelationRepository;

	@Test
	public void testAddWord() throws Exception {
		AddWordRequest request = new AddWordRequest();
		request.setWord1("hello");
		request.setWord2("goodbye");
		request.setRelation("antonym");

		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/words/add")
				.content(request.toString())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void getWords() throws Exception {
		AddWordRequest request = new AddWordRequest();
		request.setWord1("hello");
		request.setWord2("goodbye");
		request.setRelation("antonym");

		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/words/all")
				.content(request.toString())
				.accept(MediaType.APPLICATION_JSON));


		result.andExpect(MockMvcResultMatchers.status().isOk());
	}


	@Test
	public void testGetAllWords() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/words/all"))
				.andReturn();

		String content = result.getResponse().getContentAsString();
		List<WordRelation> wordRelations = objectMapper.readValue(content, new TypeReference<List<WordRelation>>() {});
		assertEquals(6, wordRelations.size());
		WordRelation firstWordRelation = wordRelations.get(0);
		assertEquals("son", firstWordRelation.getWord());
		assertEquals("daughter", firstWordRelation.getRelatedWord());
		assertEquals("antonym", firstWordRelation.getRelation());
	}

	@Test
	public void testGetAllWordsByFilter() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/words/all?relation=synonym"))
				.andReturn();

		String content = result.getResponse().getContentAsString();
		List<WordRelation> wordRelations = objectMapper.readValue(content, new TypeReference<List<WordRelation>>() {});
		assertEquals(1, wordRelations.size());
		WordRelation firstWordRelation = wordRelations.get(0);
		assertEquals("road", firstWordRelation.getWord());
		assertEquals("street", firstWordRelation.getRelatedWord());
		assertEquals("synonym", firstWordRelation.getRelation());
	}

	@Test
	public void testGetAllWordsInversed() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/words/all/inverse"))
				.andReturn();

		String content = result.getResponse().getContentAsString();
		List<WordRelationInversed> wordRelations = objectMapper.readValue(content, new TypeReference<List<WordRelationInversed>>() {});
		assertEquals(12, wordRelations.size());
		WordRelationInversed firstWordRelation = wordRelations.get(0);
		assertEquals("son", firstWordRelation.getWord());
		assertEquals("daughter", firstWordRelation.getRelatedWord());
		assertEquals("antonym", firstWordRelation.getRelation());
		assertFalse(firstWordRelation.isInversed());

		WordRelationInversed firstInversedWordRelation = wordRelations.get(6);
		assertEquals("son", firstInversedWordRelation.getRelatedWord());
		assertEquals("daughter", firstInversedWordRelation.getWord());
		assertEquals("antonym", firstInversedWordRelation.getRelation());
		assertTrue(firstInversedWordRelation.isInversed());
	}

	@Test
	public void testAddWordLowercaseAndTrim() throws Exception {
		AddWordRequest request = new AddWordRequest();
		request.setWord1("Car ");
		request.setWord2("Truck ");
		request.setRelation("related");

		ResultActions addResult = mockMvc.perform(MockMvcRequestBuilders.post("/words/add")
				.content(request.toString())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		addResult.andExpect(MockMvcResultMatchers.status().isOk());

		MvcResult getResult = mockMvc.perform(MockMvcRequestBuilders.get("/words/all"))
				.andReturn();

		String content = getResult.getResponse().getContentAsString();
		List<WordRelation> wordRelations = objectMapper.readValue(content, new TypeReference<List<WordRelation>>() {});
		WordRelation latestWordRelation = wordRelations.get(wordRelations.size() - 1);
		assertEquals("car", latestWordRelation.getWord());
		assertEquals("truck", latestWordRelation.getRelatedWord());
		assertEquals("related", latestWordRelation.getRelation());
	}

	@Test
	public void testAddWordCharacterValidation() throws Exception {
		AddWordRequest request = new AddWordRequest();
		request.setWord1("Car1");
		request.setWord2("Truck");
		request.setRelation("related");

		ResultActions addResult = mockMvc.perform(MockMvcRequestBuilders.post("/words/add")
				.content(request.toString())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		addResult.andExpect(MockMvcResultMatchers.status().isBadRequest());
		String content = addResult.andReturn().getResponse().getContentAsString();
		List<ValidationErrorResponse> validationErrorResponseList = objectMapper.readValue(content, new TypeReference<List<ValidationErrorResponse>>() {});
		assertEquals(1, validationErrorResponseList.size());
		ValidationErrorResponse validationErrorResponse = validationErrorResponseList.get(0);
		assertEquals("word1", validationErrorResponse.getInvalidField());
		assertEquals("field contains invalid characters", validationErrorResponse.getValidationMessage());
	}

	@Test
	public void testAddEmptyFieldValidation() throws Exception {
		AddWordRequest request = new AddWordRequest();
		request.setWord1("Car");
		request.setWord2("");
		request.setRelation("related");

		ResultActions addResult = mockMvc.perform(MockMvcRequestBuilders.post("/words/add")
				.content(request.toString())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		addResult.andExpect(MockMvcResultMatchers.status().isBadRequest());
		String content = addResult.andReturn().getResponse().getContentAsString();
		List<ValidationErrorResponse> validationErrorResponseList = objectMapper.readValue(content, new TypeReference<List<ValidationErrorResponse>>() {});
		assertEquals(1, validationErrorResponseList.size());
		ValidationErrorResponse validationErrorResponse = validationErrorResponseList.get(0);
		assertEquals("word2", validationErrorResponse.getInvalidField());
		assertEquals("field is empty", validationErrorResponse.getValidationMessage());
	}

	@Test
	public void testAddDuplicateValidation() throws Exception {
		AddWordRequest request = new AddWordRequest();
		request.setWord1("son");
		request.setWord2("daughter");
		request.setRelation("antonym");

		ResultActions addResult = mockMvc.perform(MockMvcRequestBuilders.post("/words/add")
				.content(request.toString())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		addResult.andExpect(MockMvcResultMatchers.status().isBadRequest());
		String content = addResult.andReturn().getResponse().getContentAsString();
		List<ValidationErrorResponse> validationErrorResponseList = objectMapper.readValue(content, new TypeReference<List<ValidationErrorResponse>>() {});
		assertEquals(1, validationErrorResponseList.size());
		ValidationErrorResponse validationErrorResponse = validationErrorResponseList.get(0);
		assertEquals("relation/word1/word2", validationErrorResponse.getInvalidField());
		assertEquals("Relation already exists", validationErrorResponse.getValidationMessage());
	}

	@Test
	public void testAddDuplicateInversedValidation() throws Exception {
		AddWordRequest request = new AddWordRequest();
		request.setWord1("daughter");
		request.setWord2("son");
		request.setRelation("antonym");

		ResultActions addResult = mockMvc.perform(MockMvcRequestBuilders.post("/words/add")
				.content(request.toString())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		addResult.andExpect(MockMvcResultMatchers.status().isBadRequest());
		String content = addResult.andReturn().getResponse().getContentAsString();
		List<ValidationErrorResponse> validationErrorResponseList = objectMapper.readValue(content, new TypeReference<List<ValidationErrorResponse>>() {});
		assertEquals(1, validationErrorResponseList.size());
		ValidationErrorResponse validationErrorResponse = validationErrorResponseList.get(0);
		assertEquals("relation/word1/word2", validationErrorResponse.getInvalidField());
		assertEquals("Inverse relation already exists", validationErrorResponse.getValidationMessage());
	}

	@Test
	public void testFindPath() throws Exception {
		GetPathRequest request = new GetPathRequest();
		request.setWord1("avenue");
		request.setWord2("street");

		ResultActions addResult = mockMvc.perform(MockMvcRequestBuilders.post("/words/path")
				.content(request.toString())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		String content = addResult.andReturn().getResponse().getContentAsString();
		assertEquals("avenue --> (related) --> road --> (synonym) --> street", content);
	}
}

