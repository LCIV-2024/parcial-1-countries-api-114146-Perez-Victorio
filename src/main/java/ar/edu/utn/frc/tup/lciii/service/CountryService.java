package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.entity.CountryEntity;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {
        private final RestTemplate restTemplate;
        private final CountryRepository countryRepository;


        public List<CountryDTO> saveCountries(int amountOfCountries) {
                String url = "https://restcountries.com/v3.1/all";
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);

                if (response == null || response.isEmpty()) {
                        return Collections.emptyList();
                }

                amountOfCountries = Math.min(amountOfCountries, 10);

                Collections.shuffle(response);
                List<Map<String, Object>> randomCountries = response.stream()
                        .limit(amountOfCountries)
                        .collect(Collectors.toList());

                List<CountryEntity> countriesToSave = randomCountries.stream()
                        .map(this::mapToCountryEntity)
                        .collect(Collectors.toList());

                countryRepository.saveAll(countriesToSave);

                return countriesToSave.stream()
                        .map(country -> new CountryDTO(country.getCode(), country.getName()))
                        .collect(Collectors.toList());
        }

        private CountryEntity mapToCountryEntity(Map<String, Object> countryData) {
                Map<String, Object> nameData = (Map<String, Object>) countryData.get("name");

                return CountryEntity.builder()
                        .code((String) countryData.get("cca3")) // Mapping
                        .name((String) nameData.get("common"))  // Mapping
                        .population(((Number) countryData.get("population")).longValue())
                        .area(((Number) countryData.get("area")).doubleValue())
                        .build();
        }



        public List<CountryDTO> getAllCountries(String name, String code) {
                String url = "https://restcountries.com/v3.1/all";
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);

                return response.stream()
                        .map(this::mapToDTO)
                        .filter(country -> filterCountry(country, name, code))
                        .collect(Collectors.toList());
        }

        private boolean filterCountry(CountryDTO country, String name, String code) {
                boolean nameMatch = name == null || country.getName().toLowerCase().contains(name.toLowerCase());
                boolean codeMatch = code == null || country.getCode().equalsIgnoreCase(code);
                return nameMatch && codeMatch;
        }

        /**
         * Agregar mapeo de campo cca3 (String)
         * Agregar mapeo campos borders ((List<String>))
         */
        private Country mapToCountry(Map<String, Object> countryData) {
                Map<String, Object> nameData = (Map<String, Object>) countryData.get("name");

                return Country.builder()
                        .code((String) countryData.get("cca3")) // Mapeo
                        .name((String) nameData.get("common")) // Mapeo
                        .population(((Number) countryData.get("population")).longValue())
                        .area(((Number) countryData.get("area")).doubleValue())
                        .region((String) countryData.get("region"))
                        .languages((Map<String, String>) countryData.get("languages"))
                        .build();
        }

        private CountryDTO mapToDTO(Map<String, Object> countryData) {
                String code = (String) countryData.get("cca3");
                String commonName = (String) ((Map<String, Object>) countryData.get("name")).get("common");

                return new CountryDTO(code, commonName);
        }


        public List<CountryDTO> getCountriesByContinent(String continent) {
                String url = "https://restcountries.com/v3.1/all";
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);

                if (response == null) {
                        return Collections.emptyList();
                }

                return response.stream()
                        .filter(countryData -> ((List<String>) countryData.get("continents")).contains(continent))
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());
        }

        public List<CountryDTO> getCountriesByLanguage(String language) {
                String url = "https://restcountries.com/v3.1/all";
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);

                if (response == null) {
                        return Collections.emptyList();
                }

                return response.stream()
                        .filter(countryData -> {
                                Map<String, String> languages = (Map<String, String>) countryData.get("languages");
                                return languages != null && languages.values().stream()
                                        .anyMatch(lang -> lang.equalsIgnoreCase(language));
                        })
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());
        }

        public CountryDTO getCountryWithMostBorders() {
                String url = "https://restcountries.com/v3.1/all";
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
                if (response == null) {
                        return null;
                }
                Map<String, Object> countryWithMostBorders = response.stream()
                        .filter(countryData -> countryData.containsKey("borders")) // Solo los que tienen fronteras
                        .max(Comparator.comparingInt(countryData -> ((List<String>) countryData.get("borders")).size()))
                        .orElse(null);

                if (countryWithMostBorders == null) {
                        return null;
                }

                return mapToDTO(countryWithMostBorders);
        }






}