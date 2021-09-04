package edu.uga.cs1302.quiz;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionFactory {

    // prevent instantiation
    private QuestionFactory() {
    } // QuestionFactory

    public static List<Question> getQuestions(World world, int numberOfQuestions) {
        if (numberOfQuestions > world.size()) {
            throw new IllegalArgumentException("numberOfQuestions > world.size()");
        } // if

        return world.getRandomCountries(numberOfQuestions)
                .stream()
                .map(Question::new)
                .collect(Collectors.toList());

    } // getQuestions

    public static class Question {
        private final World.Country country; // question country
        private final List<String> options; // options including correct answer

        public Question(World.Country country) {
            this.country = country;

            List<String> possible = World.getContinents();
            List<String> tempOptions = new LinkedList<>();
            tempOptions.add(possible.remove(possible.indexOf(country.getContinent())));

            SecureRandom random = new SecureRandom();
            tempOptions.add(possible.remove(random.nextInt(possible.size())));
            tempOptions.add(possible.remove(random.nextInt(possible.size())));

            Collections.shuffle(tempOptions);

            options = Collections.unmodifiableList(tempOptions);
        } // Question

        public World.Country getCountry() {
            return country;
        } // getCountry

        public List<String> getOptions() {
            return options;
        } // getOptions

    } // Question

} // QuestionFactory
