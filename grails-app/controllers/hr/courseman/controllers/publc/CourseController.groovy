package hr.courseman.controllers.publc

import grails.converters.JSON
import grails.transaction.Transactional
import hr.courseman.controllers.UtilController
import hr.courseman.domain.CourseDefinition
import hr.courseman.domain.CourseInstance
import hr.courseman.domain.CourseInstance.Status

@Transactional(readOnly = true)
class CourseController {

    static namespace = 'public'
    static allowedMethods = [show: 'GET', fetchUpcoming: 'GET', list: 'GET', search: 'GET', instance: 'GET']

    UtilController utilCtrl = new UtilController()

    def show(CourseDefinition courseDefinition) {
        if (!courseDefinition) {
            utilCtrl.respondError('Course not found for given ID.')
            return
        }

        JSON.use('publicDisplay') {
            render courseDefinition as JSON
        }
    }

    def fetchUpcoming() {
        int max = params.max?.isNumber() ? params.max as Integer : 5
        def courseInstanceList = CourseInstance.findAllByStatus(Status.UPCOMING, [max: max, sort: 'startTime'])

        JSON.use('publicListDisplay') {
            render courseInstanceList as JSON
        }
    }

    def list() {
        JSON.use('publicListDisplay') {
            render CourseDefinition.all as JSON
        }
    }

    def search() {
        String query = (params.query ?: '').trim()
        if (query.isEmpty()) {
            utilCtrl.respondError('You must provide search query.')
            return
        }
        query = '%' + query.toLowerCase() + '%'

        def courseResults = []

        courseResults = CourseDefinition.executeQuery(
            'select distinct cd from CourseDefinition cd inner join cd.category c inner join cd.keywords k ' +
            'where lower(cd.name) like :query or lower(c.name) like :query or lower(k) like :query',
            [query: query]
        )

        JSON.use('publicListDisplay') {
            render courseResults as JSON
        }
    }

    def instance(CourseInstance courseInstance) {
        if (!courseInstance) {
            utilCtrl.respondError('Course instance not found.')
            return
        }

        JSON.use('publicDisplay') {
            render courseInstance as JSON
        }
    }

}
