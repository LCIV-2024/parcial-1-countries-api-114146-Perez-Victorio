package ar.edu.utn.frc.tup.lciii.controllers;
import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.SaveCountryRequestDTO;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/api")
public class CountryController {

    private final CountryService countryService;

    @GetMapping("/countries")
    public List<CountryDTO> getCountries(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code) {
        List<CountryDTO> countries = countryService.getAllCountries(name, code);
        System.out.println("Filtered Countries: " + countries);
        return countries;
    }

    @GetMapping("/countries/{continent}/continent")
    public List<CountryDTO> getCountriesByContinent(@PathVariable String continent) {
        return countryService.getCountriesByContinent(continent);
    }

    @GetMapping("countries/{language}/language")
    public ResponseEntity<List<CountryDTO>> getCountriesByLanguage(@PathVariable String language) {
        List<CountryDTO> countries = countryService.getCountriesByLanguage(language);
        return new ResponseEntity<>(countries, countries.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @GetMapping("countries/most-borders")
    public ResponseEntity<CountryDTO> getCountryWithMostBorders() {
        CountryDTO country = countryService.getCountryWithMostBorders();
        if (country == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(country, HttpStatus.OK);
    }

    @PostMapping("/countries")
    public ResponseEntity<List<CountryDTO>> saveCountries(@RequestBody Map<String, Integer> request) {
        int amountOfCountries = request.getOrDefault("amountOfCountryToSave", 0);
        List<CountryDTO> savedCountries = countryService.saveCountries(amountOfCountries);
        return ResponseEntity.ok(savedCountries);
    }



}