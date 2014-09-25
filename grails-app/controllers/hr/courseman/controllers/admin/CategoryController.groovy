package hr.courseman.controllers.admin

import grails.converters.JSON
import grails.transaction.Transactional
import hr.courseman.domain.Category

import static org.springframework.http.HttpStatus.BAD_REQUEST

@Transactional(readOnly = true)
class CategoryController {

    static namespace = 'admin'
    static allowedMethods = [show: 'GET', save: 'POST', update: 'PUT', delete: 'DELETE']

    def show(Category category) {
        if (category == null) {
            respondError('Category not found for given ID.')
            return
        }

        JSON.use('adminCategoryShow') {
            render category as JSON
        }
    }

    @Transactional
    def save(Category category) {
        if (!category.validate()) {
            respondError('Validation error. TODO detailed message.')
        }

        category.save()
        render respondMessage('Category saved with ID: ' + category.id + '.')
    }

    @Transactional
    def update(Category category) {
        if (category == null) {
            respondError(String.format('Category with id: %s does not exist.', params.id))
        }

        if (!category.validate()) {
            respondError('Validation error. TODO detailed message.')
        }

        category.save()
        render respondMessage('Course updated.')
    }

    @Transactional
    def delete(Category category) {
        if (category == null) {
            respondError('Category not found for given ID.')
        }

        category.delete()
        render respondMessage('Category deleted.')
    }

    // Query actions
    def list() {
        def categoryList = Category.all
        JSON.use('adminDisplayCourseDefinition') {
            render categoryList as JSON
        }
    }
    // End of Query actions

    // Helper methods
    private void respondError(String message) {
        response.setStatus BAD_REQUEST.value()
        respondJson([error: message])
    }

    private void respondMessage(String message) {
        respondJson([message: message])
    }

    private void respondJson(def model) {
        render (model as JSON)
    }
}
