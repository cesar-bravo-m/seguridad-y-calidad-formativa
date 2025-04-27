package com.example.formativa.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.example.formativa.models.Event;
import com.example.formativa.services.EventService;

@WebMvcTest(controllers = RouteController.class, useDefaultFilters = false)
@Import(RouteController.class)
public class RouteControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private EventService eventService;
    
    private Event testEvent;
    
    @BeforeEach
    public void setup() {
        testEvent = new Event();
        testEvent.setId(1L);
        testEvent.setDescription("Test Event");
        testEvent.setCategory("Test Category");
        testEvent.setTime("2023-01-01");
        testEvent.setAddress("Test Location");
        testEvent.setOrganizers("Test Organizers");
        testEvent.setAvailableServices("Test Services");
        testEvent.setAttractions("Test Attractions");
        testEvent.setImage("test-image.jpg");
    }
    
    @Test
    @DisplayName("El endpoint Home debe devolver no autorizado cuando no está autenticado")
    public void testHomeEndpointUnauthorized() throws Exception {
        mockMvc.perform(get("/home"))
               .andExpect(status().isUnauthorized());
    }
    
    @Test
    @DisplayName("El endpoint Home debe devolver la vista home cuando está autenticado")
    @WithMockUser
    public void testHomeEndpointAuthorized() throws Exception {
        // Just verify the controller returns the correct view name without rendering
        ResultActions resultActions = mockMvc.perform(get("/home"));
        MvcResult result = resultActions.andReturn();
        String viewName = result.getModelAndView().getViewName();
        assert viewName.equals("home");
    }
    
    @Test
    @DisplayName("El endpoint Details debe devolver no autorizado cuando no está autenticado")
    public void testDetailsEndpointUnauthorized() throws Exception {
        mockMvc.perform(get("/details/1"))
               .andExpect(status().isUnauthorized());
    }
    
    @Test
    @DisplayName("El endpoint Details debe devolver los detalles del evento cuando está autenticado y el evento existe")
    @WithMockUser
    public void testDetailsEndpointWithValidId() throws Exception {
        when(eventService.getEventById(1L)).thenReturn(testEvent);
        
        // Just verify the controller returns the correct view name and model attributes without rendering
        ResultActions resultActions = mockMvc.perform(get("/details/1"));
        MvcResult result = resultActions.andReturn();
        String viewName = result.getModelAndView().getViewName();
        assert viewName.equals("details");
        assert result.getModelAndView().getModel().containsKey("event");
        assert result.getModelAndView().getModel().get("event") == testEvent;
    }
    
    @Test
    @DisplayName("El endpoint Details debe devolver un mensaje de error cuando está autenticado pero el evento no existe")
    @WithMockUser
    public void testDetailsEndpointWithInvalidId() throws Exception {
        when(eventService.getEventById(999L)).thenReturn(null);
        
        // Just verify the controller returns the correct view name and model attributes without rendering
        ResultActions resultActions = mockMvc.perform(get("/details/999"));
        MvcResult result = resultActions.andReturn();
        String viewName = result.getModelAndView().getViewName();
        assert viewName.equals("details");
        assert result.getModelAndView().getModel().containsKey("error");
        assert result.getModelAndView().getModel().get("error").equals("El evento solicitado no existe o no está disponible.");
    }
    
    @Test
    @DisplayName("El endpoint Search debe devolver no autorizado cuando no está autenticado")
    public void testSearchEndpointUnauthorized() throws Exception {
        mockMvc.perform(get("/search"))
               .andExpect(status().isUnauthorized());
    }
    
    @Test
    @DisplayName("El endpoint Search debe devolver resultados de búsqueda cuando está autenticado y usa el parámetro query")
    @WithMockUser
    public void testSearchEndpointWithQuery() throws Exception {
        List<Event> searchResults = Arrays.asList(testEvent);
        when(eventService.searchEvents(anyString(), anyString(), anyString(), anyString()))
            .thenReturn(searchResults);
        
        // Just verify the controller returns the correct view name and model attributes without rendering
        ResultActions resultActions = mockMvc.perform(get("/search")
                .param("query", "test"));
        MvcResult result = resultActions.andReturn();
        String viewName = result.getModelAndView().getViewName();
        assert viewName.equals("search");
        assert result.getModelAndView().getModel().containsKey("searchResults");
        assert result.getModelAndView().getModel().get("searchResults") == searchResults;
        assert result.getModelAndView().getModel().get("query").equals("test");
    }
    
    @Test
    @DisplayName("El endpoint Search debe devolver resultados de búsqueda cuando está autenticado y usa el parámetro description")
    @WithMockUser
    public void testSearchEndpointWithDescription() throws Exception {
        List<Event> searchResults = Arrays.asList(testEvent);
        when(eventService.searchEvents(eq("description"), anyString(), anyString(), anyString()))
            .thenReturn(searchResults);
        
        // Just verify the controller returns the correct view name and model attributes without rendering
        ResultActions resultActions = mockMvc.perform(get("/search")
                .param("description", "description"));
        MvcResult result = resultActions.andReturn();
        String viewName = result.getModelAndView().getViewName();
        assert viewName.equals("search");
        assert result.getModelAndView().getModel().containsKey("searchResults");
        assert result.getModelAndView().getModel().get("description").equals("description");
    }
    
    @Test
    @DisplayName("El endpoint Search debe devolver resultados de búsqueda cuando está autenticado y usa el parámetro category")
    @WithMockUser
    public void testSearchEndpointWithCategory() throws Exception {
        List<Event> searchResults = Arrays.asList(testEvent);
        when(eventService.searchEvents(anyString(), eq("category"), anyString(), anyString()))
            .thenReturn(searchResults);
        
        // Just verify the controller returns the correct view name and model attributes without rendering
        ResultActions resultActions = mockMvc.perform(get("/search")
                .param("category", "category"));
        MvcResult result = resultActions.andReturn();
        String viewName = result.getModelAndView().getViewName();
        assert viewName.equals("search");
        assert result.getModelAndView().getModel().containsKey("searchResults");
        assert result.getModelAndView().getModel().get("category").equals("category");
    }
    
    @Test
    @DisplayName("El endpoint Search debe devolver resultados de búsqueda cuando está autenticado y usa el parámetro date")
    @WithMockUser
    public void testSearchEndpointWithDate() throws Exception {
        List<Event> searchResults = Arrays.asList(testEvent);
        when(eventService.searchEvents(anyString(), anyString(), eq("2023-01-01"), anyString()))
            .thenReturn(searchResults);
        
        // Just verify the controller returns the correct view name and model attributes without rendering
        ResultActions resultActions = mockMvc.perform(get("/search")
                .param("date", "2023-01-01"));
        MvcResult result = resultActions.andReturn();
        String viewName = result.getModelAndView().getViewName();
        assert viewName.equals("search");
        assert result.getModelAndView().getModel().containsKey("searchResults");
    }
    
    @Test
    @DisplayName("El endpoint Search debe devolver resultados de búsqueda cuando está autenticado y usa el parámetro location")
    @WithMockUser
    public void testSearchEndpointWithLocation() throws Exception {
        List<Event> searchResults = Arrays.asList(testEvent);
        when(eventService.searchEvents(anyString(), anyString(), anyString(), eq("location")))
            .thenReturn(searchResults);
        
        // Just verify the controller returns the correct view name and model attributes without rendering
        ResultActions resultActions = mockMvc.perform(get("/search")
                .param("location", "location"));
        MvcResult result = resultActions.andReturn();
        String viewName = result.getModelAndView().getViewName();
        assert viewName.equals("search");
        assert result.getModelAndView().getModel().containsKey("searchResults");
        assert result.getModelAndView().getModel().get("location").equals("location");
    }
    
    @Test
    @DisplayName("El endpoint Search debe devolver resultados de búsqueda cuando está autenticado y usa múltiples parámetros")
    @WithMockUser
    public void testSearchEndpointWithMultipleParameters() throws Exception {
        List<Event> searchResults = Arrays.asList(testEvent);
        when(eventService.searchEvents(eq("description"), eq("category"), eq("2023-01-01"), eq("location")))
            .thenReturn(searchResults);
        
        // Just verify the controller returns the correct view name and model attributes without rendering
        ResultActions resultActions = mockMvc.perform(get("/search")
                .param("description", "description")
                .param("category", "category")
                .param("date", "2023-01-01")
                .param("location", "location"));
        MvcResult result = resultActions.andReturn();
        String viewName = result.getModelAndView().getViewName();
        assert viewName.equals("search");
        assert result.getModelAndView().getModel().containsKey("searchResults");
        assert result.getModelAndView().getModel().get("searchResults") == searchResults;
        assert result.getModelAndView().getModel().get("description").equals("description");
        assert result.getModelAndView().getModel().get("category").equals("category");
        assert result.getModelAndView().getModel().get("date").equals("2023-01-01");
        assert result.getModelAndView().getModel().get("location").equals("location");
    }
    
    @Test
    @DisplayName("El endpoint Search debe devolver resultados vacíos cuando está autenticado sin parámetros")
    @WithMockUser
    public void testSearchEndpointWithNoParameters() throws Exception {
        // Just verify the controller returns the correct view name without rendering
        ResultActions resultActions = mockMvc.perform(get("/search"));
        MvcResult result = resultActions.andReturn();
        String viewName = result.getModelAndView().getViewName();
        assert viewName.equals("search");
    }
    
    @Test
    @DisplayName("El endpoint Search debe manejar resultados de búsqueda vacíos")
    @WithMockUser
    public void testSearchEndpointWithEmptyResults() throws Exception {
        when(eventService.searchEvents(anyString(), anyString(), anyString(), anyString()))
            .thenReturn(Collections.emptyList());
        
        // Just verify the controller returns the correct view name and model attributes without rendering
        ResultActions resultActions = mockMvc.perform(get("/search")
                .param("query", "nonexistent"));
        MvcResult result = resultActions.andReturn();
        String viewName = result.getModelAndView().getViewName();
        assert viewName.equals("search");
        assert result.getModelAndView().getModel().containsKey("searchResults");
        assert result.getModelAndView().getModel().get("searchResults") == Collections.emptyList();
        assert result.getModelAndView().getModel().get("query").equals("nonexistent");
    }
    
    @Test
    @DisplayName("El endpoint Search debe manejar resultados de búsqueda nulos")
    @WithMockUser
    public void testSearchEndpointWithNullResults() throws Exception {
        when(eventService.searchEvents(anyString(), anyString(), anyString(), anyString()))
            .thenReturn(null);
        
        // Just verify the controller returns the correct view name and model attributes without rendering
        ResultActions resultActions = mockMvc.perform(get("/search")
                .param("query", "test"));
        MvcResult result = resultActions.andReturn();
        String viewName = result.getModelAndView().getViewName();
        assert viewName.equals("search");
        assert result.getModelAndView().getModel().containsKey("searchResults");
        assert result.getModelAndView().getModel().get("searchResults") == null;
        assert result.getModelAndView().getModel().get("query").equals("test");
    }
    
    @Test
    @DisplayName("El endpoint Search debe manejar parámetros de cadena vacía")
    @WithMockUser
    public void testSearchEndpointWithEmptyStringParameters() throws Exception {
        // Just verify the controller returns the correct view name without rendering
        ResultActions resultActions = mockMvc.perform(get("/search")
                .param("description", "")
                .param("category", "")
                .param("date", "")
                .param("location", ""));
        MvcResult result = resultActions.andReturn();
        String viewName = result.getModelAndView().getViewName();
        assert viewName.equals("search");
    }
}
