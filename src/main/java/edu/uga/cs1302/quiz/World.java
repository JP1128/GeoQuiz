package edu.uga.cs1302.quiz;

import java.io.Serializable;
import java.util.*;

public class World extends ArrayList<World.Country> {
    private static final List<String> CONTINENTS = Collections.unmodifiableList(
            Arrays.asList(
                    "Africa", "Asia", "Europe",
                    "North America", "Oceania", "South America"
            ));

    // returns the specified num of random countries in random order
    public List<Country> getRandomCountries(int num) {
        if (num > size()) {
            throw new IllegalArgumentException(num + " > size()");
        } // if

        Set<Country> countries = new HashSet<>();

        Random rand = new Random();

        while (countries.size() < num) {
            countries.add(get(rand.nextInt(size())));
        } // while

        List<Country> list = new LinkedList<>(countries);
        Collections.shuffle(list);

        return list;
    } // getRandomCountries

    // returns a modifiable list of continents
    public static List<String> getContinents() {
        return new LinkedList<>(CONTINENTS);
    } // getContinents

    public static class Country implements Serializable {
        private final String name;
        private final String continent;

        public Country(String name, String continent) {
            this.name = name;
            this.continent = continent;
        } // Country

        public String getName() {
            return name;
        } // getName

        public String getContinent() {
            return continent;
        } // getContinent

    } // Country

} // Countries
