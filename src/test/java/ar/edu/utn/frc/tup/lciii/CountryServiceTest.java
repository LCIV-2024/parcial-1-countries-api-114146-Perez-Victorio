package ar.edu.utn.frc.tup.lciii;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.utn.frc.tup.lciii.controllers.CountryController;
import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(CountryController.class)
public class CountryServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    @Test
    public void testGetCountries() throws Exception {
        CountryDTO country = new CountryDTO("Argentina", "AR");
        List<CountryDTO> countries = Arrays.asList(country);

        when(countryService.getAllCountries(null, null)).thenReturn(countries);

        mockMvc.perform(get("/api/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Argentina"));
    }

    @Test
    public void testGetCountriesByContinent() throws Exception {
        CountryDTO country = new CountryDTO("Argentina", "AR");
        List<CountryDTO> countries = Arrays.asList(country);

        when(countryService.getCountriesByContinent("South America")).thenReturn(countries);

        mockMvc.perform(get("/api/countries/South America/continent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Argentina"));
    }

    @Test
    public void testGetCountriesByLanguage() throws Exception {
        CountryDTO country = new CountryDTO("Argentina", "AR");
        List<CountryDTO> countries = Arrays.asList(country);

        when(countryService.getCountriesByLanguage("Spanish")).thenReturn(countries);

        mockMvc.perform(get("/api/countries/Spanish/language"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Argentina"));
    }

    @Test
    public void testGetCountryWithMostBorders() throws Exception {
        CountryDTO country = new CountryDTO("Brazil", "BR");

        when(countryService.getCountryWithMostBorders()).thenReturn(country);

        mockMvc.perform(get("/api/countries/most-borders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Brazil"));
    }

    @Test
    public void testSaveCountries() throws Exception {
        CountryDTO country1 = new CountryDTO("Argentina", "AR");
        CountryDTO country2 = new CountryDTO("Brazil", "BR");
        List<CountryDTO> savedCountries = Arrays.asList(country1, country2);

        when(countryService.saveCountries(2)).thenReturn(savedCountries);

        mockMvc.perform(post("/api/countries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amountOfCountryToSave\": 2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Argentina"))
                .andExpect(jsonPath("$[1].name").value("Brazil"));
    }
}
