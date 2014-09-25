package hr.courseman.controllers.restricted

import grails.transaction.Transactional
import hr.courseman.controllers.UtilController
import hr.courseman.domain.CourseInstance
import hr.courseman.domain.CourseUser
import hr.courseman.domain.CourseUser.Type
import hr.courseman.domain.Notification
import hr.courseman.domain.Notification.Receiver

@Transactional(readOnly = true)
class CourseUserController {

    static namespace = 'restricted'
    static allowedMethods = [register: 'POST', isRegistered: 'GET']

    def springSecurityService
    UtilController utilCtrl = new UtilController()

    @Transactional
    def register(CourseUser courseUser) {
        if (courseUser == null) {
            utilCtrl.respondError('Failed to create registraion.')
            return
        }

        if (!courseUser.validate() || courseUser.type != Type.REGISTRATION) {
            utilCtrl.respondError([errors: courseUser.errors], 'Error while validating registration.')
            return
        }

        def currentUser = springSecurityService.getCurrentUser()
        if (!currentUser || currentUser.id != courseUser.user.id) {
            utilCtrl.respondError('You can only register yourself.')
            return
        }

        CourseUser preRegistration = CourseUser.findByUserAndCourseInstanceAndType(courseUser.user, courseUser.courseInstance, Type.PRE_REGISTRATION)
        if (preRegistration) {
            preRegistration.setActive(false)
            preRegistration.save()
        }

        courseUser.setActive(true)
        courseUser.save()

        new Notification(
            text: String.format('User %s %s registered for %s course.', courseUser.user.firstName, courseUser.user.lastName, courseUser.courseInstance.courseDefinition.name),
            comment: courseUser.comment,
            receiver: Receiver.ADMIN,
            user: courseUser.user,
            courseInstance: courseUser.courseInstance
        ).save()

        utilCtrl.respondMessage('You have successfully registered to ' + courseUser.courseInstance.courseDefinition.name + ' course.')
    }

    def isRegistered(CourseInstance courseInstance) {
        def user = springSecurityService.getCurrentUser()

        if (!courseInstance || !user) {
            utilCtrl.respondError('Invalid course or user is not found')
            return
        }

        def isRegistered = CourseUser.findByUserAndCourseInstanceAndType(user, courseInstance, Type.REGISTRATION) != null

        utilCtrl.respondJson([isRegistered: isRegistered])
    }

    def isAttending(CourseInstance courseInstance) {
        def user = springSecurityService.getCurrentUser()

        if (!courseInstance || !user) {
            utilCtrl.respondError('Invalid course or user is not found')
            return
        }

        def isRegistered = CourseUser.findByActiveAndUserAndCourseInstanceAndType(true, user, courseInstance, Type.ACCEPTED) != null

        utilCtrl.respondJson([isAttending: isRegistered])
    }

}
