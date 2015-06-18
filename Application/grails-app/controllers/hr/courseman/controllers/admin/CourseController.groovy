package hr.courseman.controllers.admin

import static org.springframework.http.HttpStatus.BAD_REQUEST

import grails.converters.JSON
import grails.transaction.Transactional
import hr.courseman.domain.CourseDefinition

@Transactional(readOnly = true)
class CourseController {

    static namespace = 'admin'
    static allowedMethods = [show: 'GET', save: 'POST', update: 'PUT', delete: 'DELETE']

    def show(CourseDefinition courseDefinition) {
        if (courseDefinition == null) {
            respondError('CourseDefinition not found for given ID.')
            return
        }

        JSON.use('adminDisplayCourseDefinition') {
            render courseDefinition as JSON
        }
    }

    @Transactional
    def save(CourseDefinition courseDefinition) {
        if (!courseDefinition.validate()) {
            respondError('Validation error. TODO detailed message.')
        }

        courseDefinition.save()
        render respondMessage([id: courseDefinition.id], 'CourseDefinition saved with ID: ' + courseDefinition.id + '.')
    }

    @Transactional
    def update(CourseDefinition courseDefinition) {
        if (courseDefinition == null) {
            respondError(String.format('CourseDefinition with id: %s does not exist.', params.id))
        }

        if (!courseDefinition.validate()) {
            respondError('Validation error. TODO detailed message.')
        }

        courseDefinition.save()
        render respondMessage('Course updated.')
    }

    @Transactional
    def delete(CourseDefinition courseDefinition) {
        if (courseDefinition == null) {
            respondError('CourseDefinition not found for given ID.')
        }

        courseDefinition.delete()
        render respondMessage('CourseDefinition deleted.')
    }

    // Query actions
    def list() {
        def courseList = CourseDefinition.all
        JSON.use('adminListCourseDefinition') {
            render courseList as JSON
        }
    }
    // End of Query actions

    // Helper methods
    private void respondError(String message) {
        response.setStatus BAD_REQUEST.value()
        respondJson([error: message])
    }

    private void respondMessage(String message) {
        respondMessage([:], message)
    }

    private void respondMessage(Map model, String message) {
        respondJson(model + [message: message])
    }

    private void respondJson(def model) {
        render (model as JSON)
    }

}
