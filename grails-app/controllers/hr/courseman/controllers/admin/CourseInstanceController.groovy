package hr.courseman.controllers.admin

import grails.converters.JSON
import grails.transaction.Transactional
import hr.courseman.controllers.UtilController
import hr.courseman.domain.CourseInstance
import hr.courseman.domain.CourseInstance.Status
import hr.courseman.domain.CourseUser
import hr.courseman.domain.CourseUser.Type
import hr.courseman.domain.Notification
import hr.courseman.domain.Notification.Receiver

@Transactional(readOnly = true)
class CourseInstanceController {

    static namespace = 'admin'
    static allowedMethods = [show: 'GET', save: 'POST', update: 'PUT', delete: 'DELETE', cancel: 'POST', held: 'POST']

    def mailService
    def UtilController utilCtrl = new UtilController()

    def show(CourseInstance courseInstance) {
        if (courseInstance == null) {
            utilCtrl.respondError('CourseInstance not found for given ID.')
            return
        }

        JSON.use('adminDisplayCourseInstance') {
            render courseInstance as JSON
        }
    }

    @Transactional
    def save(CourseInstance courseInstance) {
        if (!courseInstance.validate()) {
            utilCtrl.respondError('Validation error. TODO detailed message.')
            return
        }

        courseInstance.save()
        utilCtrl.respondMessage([id: courseInstance.id], 'CourseInstance saved with ID: ' + courseInstance.id + '.')
    }

    @Transactional
    def update(CourseInstance courseInstance) {
        if (courseInstance == null) {
            utilCtrl.respondError(String.format('CourseInstance with id: %s does not exist.', params.id))
            return
        }

        if (!courseInstance.validate()) {
            utilCtrl.respondError('Validation error. TODO detailed message.')
            return
        }

        courseInstance.save()
        utilCtrl.respondMessage('Course updated.')
    }

    @Transactional
    def delete(CourseInstance courseInstance) {
        if (courseInstance == null) {
            utilCtrl.respondError('CourseInstance not found for given ID.')
            return
        }

        notifyCourseCancellation(courseInstance)

        courseInstance.delete()
        utilCtrl.respondMessage('CourseInstance deleted.')
    }


    def list() {
        def courseList = CourseInstance.all
        JSON.use('adminDisplayCourseInstance') {
            render courseList as JSON
        }
    }

    @Transactional
    def cancel(CourseInstance courseInstance) {
        if (!courseInstance) {
            utilCtrl.respondError('Something went wrong, cannot cancel this course.')
            return
        }

        notifyCourseCancellation(courseInstance)

        courseInstance.setStatus(Status.CANCELLED)
        courseInstance.save()

        utilCtrl.respondMessage([status: Status.CANCELLED.toString()], 'Course cancelled successfully.')
    }

    @Transactional
    def held(CourseInstance courseInstance) {
        if (!courseInstance) {
            utilCtrl.respondError('Something went wrong, cannot mark this course as held.')
            return
        }

        if (new Date().compareTo(courseInstance.startTime) < 0) {
            utilCtrl.respondError('Cannot mark current course as held since course did not start yet.')
            return
        }

        def courseUsers = CourseUser.findAllByActiveAndCourseInstanceAndType(true, courseInstance, Type.ACCEPTED)
        courseUsers.each { CourseUser courseUser ->
            if (courseUser.type == Type.ACCEPTED) {
                String notificationText = 'Thank you for participating in ' + courseUser.courseInstance.courseDefinition.name + ' course.'
                String notificationComment = 'Please rate this course.'
                new Notification(
                    text: notificationText,
                    comment: notificationComment,
                    receiver: Receiver.USER,
                    user: courseUser.user,
                    courseInstance: courseUser.courseInstance
                ).save()

                String mailSubject = 'CourseMan - ' + courseUser.courseInstance.courseDefinition.name + ' held'
                String mailBody = '<b>' + notificationText + '</b>' + '<br>' + notificationComment
                mailService.sendMail {
                    async true
                    to courseUser.user.email
                    subject mailSubject
                    html mailBody
                }
            }
        }

        courseInstance.setStatus(Status.HELD)
        courseInstance.save()

        utilCtrl.respondMessage([status: Status.HELD.toString()], 'Course marked as held successfully.')
    }

    private void notifyCourseCancellation(CourseInstance courseInstance) {
        def courseUsers = CourseUser.findAllByActiveAndCourseInstanceAndTypeNotEqual(true, courseInstance, Type.DECLINED)
        courseUsers.each { CourseUser courseUser ->
            if (courseUser.type != Type.DECLINED) {
                String notificationText = 'Unfortunately, ' + courseUser.courseInstance.courseDefinition.name + ' has been cancelled.'
                new Notification(
                    text: notificationText,
                    receiver: Receiver.USER,
                    user: courseUser.user,
                    courseInstance: courseUser.courseInstance
                ).save()

                String mailSubject = 'CourseMan - ' + courseUser.courseInstance.courseDefinition.name + ' cancelled'
                String mailBody = '<b>' + notificationText + '</b>'
                mailService.sendMail {
                    async true
                    to courseUser.user.email
                    subject mailSubject
                    html mailBody
                }
            }
        }
    }

}
