package com.todoapp;

import spark.Request;
import spark.Response;
import spark.Route;

import static spark.Spark.*;


/**
 * Created by nohorbee on 26/08/16.
 */
public class TodoResource {
    private static final String API_CONTEXT = "/api/v1";

    private final TodoService todoService;

    public TodoResource(TodoService todoService) {
        this.todoService = todoService;
        setupEndpoints();
    }

    private void setupEndpoints() {



        post(API_CONTEXT + "/todos", "application/json", (Request req, Response res) -> {
            todoService.createNewTodo(req.body());
            res.status(201);
            return res;
        });

        get(API_CONTEXT + "/todos/:id", "application/json", (Request req, Response res) -> todoService.find(req.params(":id")), new JsonTransformer());

        get(API_CONTEXT + "/todos", "application/json", (Request req, Response res) -> todoService.findAll(), new JsonTransformer());

        put(API_CONTEXT + "/todos/:id", "application/json", (Request req, Response res) -> todoService.update(req.params(":id"), req.body()), new JsonTransformer());

    }

}

