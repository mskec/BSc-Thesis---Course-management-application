package hr.courseman.controllers.admin

import grails.transaction.Transactional
import hr.courseman.controllers.UtilController
import hr.courseman.domain.CourseUser
import hr.courseman.domain.CourseUser.Type
import hr.courseman.domain.Document
import hr.courseman.domain.Notification

@Transactional(readOnly = true)
class DocumentController {

    static namespace = 'admin'
    static allowedMethods = [upload: 'POST', delete: 'DELETE']

    UtilController utilCtrl = new UtilController()

    @Transactional
    def upload(Document document) {
        if (!document) {
            utilCtrl.respondError('Something is wrong with uploaded document.')
            return
        }

        if (!document.validate()) {
            utilCtrl.respondError('Document validation error.')
            return
        }

        def courseUserList = CourseUser.findAllByActiveAndCourseInstanceAndType(true, document.courseInstance, Type.ACCEPTED)
        courseUserList.each { CourseUser courseUser ->
            String notificationText = "${courseUser.courseInstance.courseDefinition.name} - New course material added"
            new Notification(
                text: notificationText,
                comment: document.fileName,
                receiver: Notification.Receiver.USER,
                user: courseUser.user,
                courseInstance: courseUser.courseInstance
            ).save()
        }
        
        document.save()

        utilCtrl.respondMessage([document: document], 'Document uploaded successfully.')
    }

    @Transactional
    def delete(Document document) {
        if (!document) {
            utilCtrl.respondError('Document not found.')
            return
        }

        document.delete()

        utilCtrl.respondMessage('Document deleted successfully.')
    }

}
