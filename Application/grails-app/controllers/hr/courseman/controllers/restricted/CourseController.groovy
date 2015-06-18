package hr.courseman.controllers.restricted

import grails.converters.JSON
import grails.transaction.Transactional
import hr.courseman.controllers.UtilController
import hr.courseman.domain.CourseGrade
import hr.courseman.domain.CourseInstance
import hr.courseman.domain.CourseInstance.Status
import hr.courseman.domain.CourseUser
import hr.courseman.domain.user.User

@Transactional(readOnly = true)
class CourseController {

    static namespace = 'restricted'
    static allowedMethods = [userUpcoming: 'GET', userRegistrations: 'GET', grade: 'POST']

    def springSecurityService
    def UtilController utilCtrl = new UtilController()

    def userUpcoming() {
        User user = springSecurityService.getCurrentUser()

        def courseInstanceList = CourseInstance.executeQuery(
            'select ci from CourseUser cu inner join cu.courseInstance ci where cu.user = :user and cu.active = true and cu.type = :accepted',
            [user: user, accepted: CourseUser.Type.ACCEPTED]
        )

        def courseList = courseInstanceList.collect { CourseInstance courseInstance -> [
            id: courseInstance.id,
            name: courseInstance.courseDefinition.name,
            location: courseInstance.location,
            startTime: courseInstance.startTime
        ] }

        render courseList as JSON
    }

    def userRegistrations() {
        User user = springSecurityService.getCurrentUser()

        def courseInstanceList = CourseInstance.executeQuery(
            'select ci from CourseUser cu inner join cu.courseInstance ci where cu.user = :user and cu.active = true and cu.type = :registration',
            [user: user, registration: CourseUser.Type.REGISTRATION]
        )

        def courseList = courseInstanceList.collect { CourseInstance courseInstance -> [
            id: courseInstance.id,
            name: courseInstance.courseDefinition.name,
            location: courseInstance.location,
            startTime: courseInstance.startTime
        ] }

        render courseList as JSON
    }

    @Transactional
    def grade(CourseGrade courseGrade) {
        if (!courseGrade || !courseGrade.validate()) {
            utilCtrl.respondError('Bad request')
            return
        }
        if (courseGrade.courseInstance.status != Status.HELD) {
            utilCtrl.respondError('This course cannot be graded because it did not finish yet.')
            return
        }

        boolean isAccepted = CourseUser.findByActiveAndCourseInstanceAndUser(true, courseGrade.courseInstance, courseGrade.user) != null
        if (!isAccepted) {
            utilCtrl.respondError('You cannot grade this course.')
            return
        }

        boolean isGraded = CourseGrade.findByUserAndCourseInstance(courseGrade.user, courseGrade.courseInstance) != null
        if (isGraded) {
            utilCtrl.respondError('You already graded this course.')
            return
        }

        courseGrade.save()

        utilCtrl.respondMessage('Course graded successfully.')
    }

    // TODO refactor!
    def getGrade(CourseInstance courseInstance) {
        def user = springSecurityService.getCurrentUser()

        CourseGrade courseGrade = CourseGrade.findByUserAndCourseInstance(user, courseInstance) ?: new CourseGrade()

        render courseGrade as JSON
    }

}
