package hr.courseman.controllers.admin

import grails.transaction.Transactional
import hr.courseman.controllers.UtilController
import hr.courseman.domain.CourseUser
import hr.courseman.domain.CourseUser.Type
import hr.courseman.domain.Notification
import hr.courseman.domain.Notification.Receiver

@Transactional
class CourseUserController {

    static namespace = "admin"
    static allowedMethods = [accept: "POST", decline: "POST"]

    def mailService
    UtilController utilCtrl = new UtilController()

    def accept(CourseUser courseUser) {
        if (courseUser == null) {
            utilCtrl.respondError("Failed to create CourseUser object.")
            return
        }

        if (!courseUser.validate() || courseUser.type != Type.ACCEPTED) {
            utilCtrl.respondError([errors: courseUser.errors], "Error while validating CourseUser.")
            return
        }

        int accepted = CourseUser.findAllByCourseInstanceAndType(courseUser.courseInstance, Type.ACCEPTED).size()
        if (accepted >= courseUser.courseInstance.maxNumberOfParticipants) {
            utilCtrl.respondError("Limit of participents exceeded for current course instance.")
            return
        }

        CourseUser registration = CourseUser.findByUserAndCourseInstanceAndType(courseUser.user, courseUser.courseInstance, Type.REGISTRATION)
        if (registration == null) {
            utilCtrl.respondError(
                String.format("Unable to find registration for user: (%d) and course instance: (%d).", courseUser.user.id, courseUser.courseInstance.id))
            return
        }

        registration.setActive(false)
        courseUser.setActive(true)
        registration.save()
        courseUser.save()

        String notificationText = 'Congratulations, you are accepted at ' + courseUser.courseInstance.courseDefinition.name + ' course.'
        new Notification(
            text: notificationText,
            comment: courseUser.comment,
            receiver: Receiver.USER,
            user: courseUser.user,
            courseInstance: courseUser.courseInstance
        ).save()

        String mailSubject = 'CourseMan - ' + courseUser.courseInstance.courseDefinition.name
        String mailBody = '<b>' + notificationText + '</b>' + '<br>' + (courseUser.comment ?: '')
        mailService.sendMail {
            async true
            to courseUser.user.email
            subject mailSubject
            html mailBody
        }

        utilCtrl.respondMessage([courseUser: courseUser], "User accepted successfully.")
    }

    def decline(CourseUser courseUser) {
        if (courseUser == null) {
            utilCtrl.respondError("Failed to create CourseUser object.")
            return
        }

        if (!courseUser.validate() || courseUser.type != Type.DECLINED) {
            utilCtrl.respondError([errors: courseUser.errors], "Error while validating CourseUser.")
            return
        }

        CourseUser registration = CourseUser.findByUserAndCourseInstanceAndType(courseUser.user, courseUser.courseInstance, Type.REGISTRATION)
        if (registration == null) {
            utilCtrl.respondError(
                    String.format("Unable to find registration for user: (%d) and course instance: (%d).", courseUser.user.id, courseUser.courseInstance.id))
            return
        }

        registration.setActive(false)
        courseUser.setActive(true)
        registration.save()
        courseUser.save()

        String notificationText = 'Unfortunately, you are not accepted at ' + courseUser.courseInstance.courseDefinition.name + ' course.'
        new Notification(
            text: notificationText,
            comment: courseUser.comment,
            receiver: Receiver.USER,
            user: courseUser.user,
            courseInstance: courseUser.courseInstance
        ).save()

        String mailSubject = 'CourseMan - ' + courseUser.courseInstance.courseDefinition.name
        String mailBody = '<b>' + notificationText + '</b>' + '<br>' + (courseUser.comment ?: '')

        mailService.sendMail {
            async true
            to courseUser.user.email
            subject mailSubject
            html mailBody
        }

        utilCtrl.respondMessage([courseUser: courseUser], "User declined successfully.")
    }

}
