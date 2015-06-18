package hr.courseman.controllers

import grails.converters.JSON

import static org.springframework.http.HttpStatus.BAD_REQUEST

class UtilController {

    // Helper methods
    public void respondError(String message) {
        respondError([:], message)
    }

    public void respondError(Map model, String message) {
        response.setStatus BAD_REQUEST.value()
        respondJson(model + [error: message])
    }

    public void respondMessage(String message) {
        respondMessage([:], message)
    }

    public void respondMessage(Map model, String message) {
        respondJson(model + [message: message])
    }

    public void respondJson(def model) {
        render(model as JSON)
    }

}
