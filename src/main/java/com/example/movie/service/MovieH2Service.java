package com.example.movie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.movie.model.Movie;
import com.example.movie.repository.MovieRepository;
import com.example.movie.model.MovieRowMapper;
import java.util.*;

@Service
public class MovieH2Service implements MovieRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public ArrayList<Movie> getAllMovies() {
        List<Movie> movieLists = jdbcTemplate.query("select * from MOVIELIST", new MovieRowMapper());
        ArrayList<Movie> movies = new ArrayList<>(movieLists);
        return movies;
    }

    @Override
    public Movie getMovieById(int movieId) {
        try {
            return jdbcTemplate.queryForObject("select * from MOVIELIST where movieId=?", new MovieRowMapper(),
                    movieId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Movie addMovie(Movie movie) {
        jdbcTemplate.update("insert into MOVIELIST(movieName, leadActor) values(?, ?)",
                movie.getMovieName(), movie.getLeadActor());
        Movie savedMovie = jdbcTemplate.queryForObject("select * from MOVIELIST where movieName=? and leadActor=?",
                new MovieRowMapper(), movie.getMovieName(), movie.getLeadActor());
        return savedMovie;
    }

    @Override
    public Movie updateMovie(int movieId, Movie movie) {
        if (movie.getMovieName() != null) {
            jdbcTemplate.update("update MOVIELISTset movieName=? where movieId=?", movie.getMovieName(), movieId);
        }
        if (movie.getLeadActor() != null) {
            jdbcTemplate.update("update MOVIELIST set leadActor=? where movieId=?", movie.getLeadActor(), movieId);
        }
        return getMovieById(movieId);
    }

    @Override
    public void deleteMovie(int movieId) {
        jdbcTemplate.update("delete from MOVIELIST where movieId=?", movieId);
    }
}
