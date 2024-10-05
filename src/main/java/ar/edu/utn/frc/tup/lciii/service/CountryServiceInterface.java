package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CountryServiceInterface {
    List<CountryDTO> getAllCountries(String name, String code);
    List<CountryDTO> getCountriesByContinent(String continent);
    List<CountryDTO> getCountriesByLanguage(String language);
    CountryDTO getCountryWithMostBorders();
    List<CountryDTO> saveCountries(int amountOfCountries);
}
